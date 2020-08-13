/**
 * Copyright 2015 Ralph Jones
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.totalchange.discodj.catalogue;

import com.totalchange.discodj.catalogue.sync.MediaEntitySync;
import com.totalchange.discodj.catalogue.sync.MediaEntitySyncHandler;
import com.totalchange.discodj.server.media.Media;
import com.totalchange.discodj.server.media.MediaEntity;
import com.totalchange.discodj.server.media.MediaSource;
import com.totalchange.discodj.server.search.SearchPopulator;
import com.totalchange.discodj.server.search.SearchProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class CatalogueSource {
    private static final Logger logger = LoggerFactory.getLogger(CatalogueSource.class);

    private final Executor executor;
    private final MediaSource mediaSource;
    private final SearchProvider searchProvider;

    private volatile SearchPopulator searchPopulator;

    public CatalogueSource(Executor executor, MediaSource mediaSource, SearchProvider searchProvider) {
        this.executor = executor;
        this.mediaSource = mediaSource;
        this.searchProvider = searchProvider;
    }

    public CompletableFuture<Void> refresh() {
        return sync();
    }

    public void close() {
        mediaSource.close();
    }

    private CompletableFuture<Void> sync() {
        final CompletableFuture<Void> syncer = new CompletableFuture<>();

        executor.execute(() -> {
            try {
                logger.info("Beginning sync for media source '{}'", mediaSource.getId());

                final CompletableFuture<List<MediaEntity>> sourceMedia = mediaSource.getAllMediaEntities();
                final CompletableFuture<List<MediaEntity>> destinationMedia = searchProvider.getAllMediaEntities(mediaSource.getId());

                syncAllTheMedia(syncer, sourceMedia, destinationMedia);
            } catch (Exception ex) {
                syncer.completeExceptionally(ex);
            }
        });

        return syncer;
    }

    private void syncAllTheMedia(final CompletableFuture<Void> syncer,
            final CompletableFuture<List<MediaEntity>> sourceMedia,
            final CompletableFuture<List<MediaEntity>> destinationMedia) {
        sourceMedia.thenAcceptBothAsync(destinationMedia, (s, d) -> {
            try {
                final List<CompletableFuture<Void>> populatedFutures = new ArrayList<>();

                MediaEntitySync.sync(s, d, new MediaEntitySyncHandler() {
                    @Override
                    public void add(String id) {
                        addMedia(id, populatedFutures);
                    }

                    @Override
                    public void update(String id) {
                        updateMedia(id, populatedFutures);
                    }

                    @Override
                    public void delete(String id) {
                        deleteMedia(id);
                    }
                });

                commitIfApplicableAfterMediaHasSynced(syncer, populatedFutures);
            } catch (Exception ex) {
                syncer.completeExceptionally(ex);
            }
        }, executor).exceptionally((ex) -> {
            syncer.completeExceptionally(ex);
            return null;
        });
    }

    private void addMedia(final String id, final List<CompletableFuture<Void>> populatedFutures) {
        logger.debug("Adding '{}' from '{}'", id, mediaSource.getId());
        final CompletableFuture<Void> f = new CompletableFuture<>();
        populatedFutures.add(f);
        try {
            mediaSource.getMedia(id).handleAsync((m, ex) -> {
                if (ex == null) {
                    logger.debug("Got media to add '{}' from '{}'", m, mediaSource.getId());
                    try {
                        lazyLoadSearchPopulator().addMedia(m);
                        f.complete(null);
                    } catch(Exception searchEx) {
                        logger.error("Search populator failed when adding media {}", m, searchEx);
                        f.completeExceptionally(searchEx);
                    }
                    return null;
                } else {
                    logger.error("Failed in callback to add media with id '{}' from '{}', skipping it", id,
                            mediaSource.getId(), ex);
                    f.complete(null);
                    return null;
                }
            }, executor);
        } catch (Exception ex) {
            logger.error("Failed to add media with id '{}' from '{}', skipping it", id, mediaSource.getId(), ex);
            f.complete(null);
        }
    }

    private void updateMedia(final String id, final List<CompletableFuture<Void>> populatedFutures) {
        logger.debug("Updating '{}' from '{}'", id, mediaSource.getId());
        final CompletableFuture<Void> f = new CompletableFuture<>();
        populatedFutures.add(f);
        try {
            mediaSource.getMedia(id).handleAsync((m, ex) -> {
                if (ex == null) {
                    logger.debug("Got media to update '{}' from '{}'", m, mediaSource.getId());
                    try {
                        lazyLoadSearchPopulator().updateMedia(m);
                        f.complete(null);
                        return null;
                    } catch (Exception searchEx) {
                        logger.error("Search populator failed when updating media {}", m, searchEx);
                        f.completeExceptionally(searchEx);
                        return null;
                    }
                } else {
                    logger.error("Failed in callback to update media with id '{}' from '{}', skipping it", id,
                            mediaSource.getId(), ex);
                    f.complete(null);
                    return null;
                }
            }, executor).exceptionally((ex) -> {
                logger.error("Failed in callback to update media with id '{}' from '{}', skipping it", id,
                        mediaSource.getId(), ex);
                f.complete(null);
                return null;
            });
        } catch (Exception ex) {
            logger.error("Failed to update media with id '{}' from '{}', skipping it", id, mediaSource.getId(), ex);
            f.complete(null);
        }
    }

    private void deleteMedia(final String id) {
        logger.debug("Deleting '{}' from '{}'", id, mediaSource.getId());
        lazyLoadSearchPopulator().deleteMedia(id);
    }

    private void commitIfApplicableAfterMediaHasSynced(final CompletableFuture<Void> syncer,
            List<CompletableFuture<Void>> populatedFutures) {
        logger.debug("Fired off {} futures from '{}', waiting for them all to complete", populatedFutures.size(),
                mediaSource.getId());
        final CompletableFuture<Media>[] arr = new CompletableFuture[populatedFutures.size()];
        CompletableFuture.allOf(populatedFutures.toArray(arr)).thenAcceptAsync((m) -> {
            logger.debug("Completed all {} futures from '{}', committing and completing", populatedFutures.size(),
                    mediaSource.getId());

            if (searchPopulator != null) {
                logger.debug("Looks like there were some changes from '{}', committing them", mediaSource.getId());
                searchPopulator.commit();
            } else {
                logger.debug("No changes");
            }

            logger.info("Sync for media source '{}' complete", mediaSource.getId());
            syncer.complete(null);
        }, executor).exceptionally((ex) -> {
            syncer.completeExceptionally(ex);
            return null;
        });
    }

    private SearchPopulator lazyLoadSearchPopulator() {
        if (searchPopulator == null) {
            synchronized (this) {
                if (searchPopulator == null) {
                    searchPopulator = searchProvider.createPopulator();
                }
            }
        }

        return searchPopulator;
    }
}

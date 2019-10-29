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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class CatalogueSource {
    private static final Logger logger = LoggerFactory.getLogger(CatalogueSource.class);

    private final Executor executor;
    private final MediaSource mediaSource;
    private final SearchProvider searchProvider;

    private SearchPopulator searchPopulator;

    public CatalogueSource(Executor executor, MediaSource mediaSource, SearchProvider searchProvider) {
        this.executor = executor;
        this.mediaSource = mediaSource;
        this.searchProvider = searchProvider;
    }

    public CompletableFuture<Void> refresh() {
        return sync();
    }

    private CompletableFuture<Void> sync() {
        final CompletableFuture<Void> syncer = new CompletableFuture<>();

        executor.execute(() -> {
            try {
                logger.info("Beginning sync for media source '{}'", mediaSource.getId());

                final CompletableFuture<List<MediaEntity>> sourceMedia = mediaSource.getAllMediaEntities();
                final CompletableFuture<List<MediaEntity>> destinationMedia = searchProvider.getAllMediaEntities(mediaSource.getId());

                sourceMedia.thenAcceptBothAsync(destinationMedia, (s, d) -> {
                    try {
                        final List<CompletableFuture<Media>> mediaFutures = new ArrayList<>();

                        MediaEntitySync.sync(s, d, new MediaEntitySyncHandler() {
                            @Override
                            public void add(String id) {
                                logger.debug("Adding '{}' from '{}'", id, mediaSource.getId());
                                final CompletableFuture<Media> f = mediaSource.getMedia(id);
                                mediaFutures.add(f);
                                f.thenAcceptAsync((m) -> {
                                    logger.debug("Got media to add {} from '{}'", m, mediaSource.getId());
                                    lazyLoadSearchPopulator().addMedia(m);
                                }, executor);
                            }

                            @Override
                            public void update(String id) {
                                logger.debug("Updating '{}' from '{}'", id, mediaSource.getId());
                                final CompletableFuture<Media> f = mediaSource.getMedia(id);
                                mediaFutures.add(f);
                                f.thenAcceptAsync((m) -> {
                                    logger.debug("Got media to update {} from '{}'", m, mediaSource.getId());
                                    lazyLoadSearchPopulator().updateMedia(m);
                                }, executor);
                            }

                            @Override
                            public void delete(String id) {
                                logger.debug("Deleting '{}' from '{}'", id, mediaSource.getId());
                                lazyLoadSearchPopulator().deleteMedia(id);
                            }
                        });

                        logger.debug("Fired off {} futures from '{}', waiting for them all to complete",
                                mediaFutures.size(), mediaSource.getId());
                        final CompletableFuture<Media>[] arr = new CompletableFuture[mediaFutures.size()];
                        CompletableFuture.allOf(mediaFutures.toArray(arr)).thenAcceptAsync((m) -> {
                            logger.debug("Completed all {} futures from '{}', committing and completing",
                                    mediaFutures.size(), mediaSource.getId());

                            if (searchPopulator != null) {
                                logger.debug("Looks like there were some changes from '{}', committing them", mediaSource.getId());
                                searchPopulator.commit();
                            }

                            logger.info("Sync for media source '{}' complete", mediaSource.getId());
                            syncer.complete(null);
                        }, executor);
                    } catch (Exception ex) {
                        syncer.completeExceptionally(ex);
                    }
                }, executor);
            } catch (Exception ex) {
                syncer.completeExceptionally(ex);
            }
        });

        return syncer;
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

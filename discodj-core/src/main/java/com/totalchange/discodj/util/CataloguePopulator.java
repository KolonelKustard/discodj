package com.totalchange.discodj.util;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.totalchange.discodj.catalogue.Catalogue;
import com.totalchange.discodj.media.Media;
import com.totalchange.discodj.search.SearchPopulator;
import com.totalchange.discodj.search.SearchProvider;

public final class CataloguePopulator implements Runnable {
    private static final Logger logger = LoggerFactory
            .getLogger(CataloguePopulator.class);

    private Catalogue catalogue;
    private SearchProvider searchProvider;

    @Inject
    public CataloguePopulator(Catalogue catalogue, SearchProvider searchProvider) {
        this.catalogue = catalogue;
        this.searchProvider = searchProvider;
    }

    @Override
    public void run() {
        logger.trace("Re-indexing search collection");
        final SearchPopulator searchPopulator = searchProvider.repopulate();

        try {
            catalogue.listAllSongs(new Catalogue.Listener() {
                @Override
                public void yetMoreMedia(Media media) {
                    logger.trace("Adding media {}", media);
                    searchPopulator.addMedia(media);
                }

                @Override
                public void warn(String msg, Throwable cause) {
                    logger.info(msg, cause);
                }
            });

            logger.trace("Committing");
            searchPopulator.commit();
            logger.trace("Finished re-indexing");
        } catch (Throwable th) {
            logger.error("Failed re-indexing search collections", th);
            throw new RuntimeException("Failed re-indexing search collections",
                    th);
        }
    }
}

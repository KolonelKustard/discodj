package com.totalchange.discodj;

import com.totalchange.discodj.catalogue.Catalogue;
import com.totalchange.discodj.catalogue.CatalogueSource;
import com.totalchange.discodj.media.file.FileMediaSource;
import com.totalchange.discodj.playlist.PlaylistQueue;
import com.totalchange.discodj.requests.web.DiscoDjRequestsWebServer;
import com.totalchange.discodj.search.lucene.LuceneSearchProvider;
import com.totalchange.discodj.server.media.MediaSource;
import com.totalchange.discodj.server.search.SearchProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;

public class DiscoDjApplication {
    private static final Logger logger = LoggerFactory.getLogger(DiscoDjApplication.class);
    
    public static void main(String[] args) throws Exception {
        logger.info("Starting");
        final SearchProvider searchProvider = new LuceneSearchProvider(
                Paths.get(DiscoDjConfiguration.getInstance().getSearchIndex()));

        final MediaSource[] mediaSources = new MediaSource[] { new FileMediaSource(
                Paths.get(DiscoDjConfiguration.getInstance().getMediaSource())) };
        final Catalogue catalogue = new Catalogue(mediaSources, searchProvider);

        for (CatalogueSource src : catalogue.getCatalogueSources()) {
            logger.info("Refreshing catalogue");
            src.refresh().get();
            logger.info("Refreshed catalogue");
        }

        DiscoDjRequestsWebServer server = new DiscoDjRequestsWebServer(searchProvider, new PlaylistQueue());
        server.start();

        System.in.read();

        logger.info("Shutting down");
        server.close();
        catalogue.close();
        searchProvider.close();

        logger.info("Shut down");
    }
}

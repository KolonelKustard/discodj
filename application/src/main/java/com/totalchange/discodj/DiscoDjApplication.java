package com.totalchange.discodj;

import com.totalchange.discodj.catalogue.Catalogue;
import com.totalchange.discodj.catalogue.CatalogueSource;
import com.totalchange.discodj.media.file.FileMediaSource;
import com.totalchange.discodj.search.lucene.LuceneSearchProvider;
import com.totalchange.discodj.server.media.MediaSource;
import com.totalchange.discodj.server.search.SearchProvider;

import java.nio.file.Paths;

public class DiscoDjApplication {
    public static void main(String[] args) throws Exception {
        System.out.println("Starting");
        final SearchProvider searchProvider = new LuceneSearchProvider(
                Paths.get("/Users/raljones/tdev/discodj-search-index"));

        final Catalogue catalogue = new Catalogue(
                new MediaSource[] { new FileMediaSource(Paths.get("/Users/raljones/tdev/discodj-sample-media")) }, searchProvider);

        for (CatalogueSource src : catalogue.getCatalogueSources()) {
            System.out.println("Refreshing");
            src.refresh().get();
            System.out.println("Refreshed");
        }

        System.out.println("Closing");
        catalogue.close();
        searchProvider.close();

        System.out.println("Done");
    }
}

package com.totalchange.discodj;

import com.totalchange.discodj.catalogue.Catalogue;
import com.totalchange.discodj.media.file.FileMediaSource;
import com.totalchange.discodj.search.lucene.LuceneSearchProvider;
import com.totalchange.discodj.server.media.MediaSource;
import com.totalchange.discodj.server.search.SearchProvider;

import java.io.IOException;
import java.nio.file.Paths;

public class DiscoDjApplication {
    public static void main(String[] args) throws IOException {
        final SearchProvider searchProvider = new LuceneSearchProvider(
                Paths.get("/Users/raljones/tdev/discodj-search-index"));
        final Catalogue catalogue = new Catalogue(
                new MediaSource[] { new FileMediaSource(Paths.get("/Users/raljones/tdev/discodj-sample-media")) },
                searchProvider);


    }
}

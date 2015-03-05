package com.totalchange.discodj.search.lucene;

import java.util.Iterator;

import javax.inject.Inject;

import org.apache.lucene.store.Directory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.totalchange.discodj.catalogue.Catalogue.CatalogueEntity;
import com.totalchange.discodj.search.SearchException;
import com.totalchange.discodj.search.SearchPopulator;
import com.totalchange.discodj.search.SearchProvider;
import com.totalchange.discodj.search.SearchQuery;
import com.totalchange.discodj.search.SearchResults;

public class LuceneSearchProvider implements SearchProvider {
    private static final Logger logger = LoggerFactory
            .getLogger(LuceneSearchProvider.class);

    static final String F_ID = "id";
    static final String F_LAST_MODIFIED = "lastModified";
    static final String F_ARTIST = "artist";
    static final String F_ALBUM = "album";
    static final String F_GENRE = "genre";
    static final String F_YEAR = "year";
    static final String F_REQUESTED_BY = "requestedBy";
    static final String F_TITLE = "title";

    static final String F_DECADE = "decade";
    static final String F_TEXT = "text";

    private Directory directory;

    @Inject
    public LuceneSearchProvider(Directory directory) {
        this.directory = directory;
    }

    @Override
    public Iterator<CatalogueEntity> listAllAlphabeticallyById()
            throws SearchException {
        return new LuceneCatalogueEntityIterator(directory);
    }

    @Override
    public SearchPopulator createPopulator() throws SearchException {
        logger.trace("Creating new Lucene populator for directory {}",
                directory);
        return new LuceneSearchPopulator(directory);
    }

    @Override
    public SearchResults search(SearchQuery query) throws SearchException {
        // TODO Auto-generated method stub
        return null;
    }
}

package com.totalchange.discodj.search.lucene;

import java.util.Iterator;

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

    private Directory directory;

    @Override
    public Iterator<CatalogueEntity> listAllAlphabeticallyById()
            throws SearchException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SearchPopulator createPopulator() throws SearchException {
        return new LuceneSearchPopulator();
    }

    @Override
    public SearchResults search(SearchQuery query) throws SearchException {
        // TODO Auto-generated method stub
        return null;
    }

}

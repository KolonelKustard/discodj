package com.totalchange.discodj.search;

import java.util.Iterator;

import com.totalchange.discodj.catalogue.Catalogue;

public interface SearchProvider {
    Iterator<Catalogue.CatalogueEntity> listAllAlphabeticallyById()
            throws SearchException;
    SearchPopulator repopulate() throws SearchException;
    SearchResults search(SearchQuery query) throws SearchException;
}

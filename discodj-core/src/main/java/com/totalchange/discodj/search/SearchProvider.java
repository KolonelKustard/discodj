package com.totalchange.discodj.search;

public interface SearchProvider {
    SearchPopulator repopulate() throws SearchException;
    SearchResults search(SearchQuery query) throws SearchException;
}

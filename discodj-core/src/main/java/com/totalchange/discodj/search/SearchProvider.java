package com.totalchange.discodj.search;

public interface SearchProvider {
    SearchPopulator repopulate();
    SearchResults search(SearchQuery query);
}

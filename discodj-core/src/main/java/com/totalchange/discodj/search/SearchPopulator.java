package com.totalchange.discodj.search;

import com.totalchange.discodj.media.Media;

public interface SearchPopulator {
    void addMedia(Media media);
    void commit();
    SearchResults search(SearchQuery query);
}

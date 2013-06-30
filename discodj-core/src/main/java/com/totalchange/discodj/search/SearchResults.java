package com.totalchange.discodj.search;

import java.util.List;

import com.totalchange.discodj.media.Media;

public interface SearchResults {
    long getNumFound();
    List<SearchFacet> getArtistFacets();
    List<SearchFacet> getAlbumFacets();
    List<SearchFacet> getGenreFacets();
    List<SearchFacet> getDecadeFacets();
    List<Media> getResults();
}

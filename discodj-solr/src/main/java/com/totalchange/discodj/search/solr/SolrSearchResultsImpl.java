package com.totalchange.discodj.search.solr;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.response.QueryResponse;

import com.totalchange.discodj.media.Media;
import com.totalchange.discodj.search.SearchFacet;
import com.totalchange.discodj.search.SearchResults;

class SolrSearchResultsImpl implements SearchResults {
    private long numFound;
    private List<SearchFacet> artistFacets;
    private List<SearchFacet> albumFacets;
    private List<SearchFacet> genreFacets;
    private List<SearchFacet> decadeFacets;
    private List<Media> results;
    
    SolrSearchResultsImpl(QueryResponse queryResponse) {
        init(queryResponse);
    }
    
    private List<SearchFacet> convertFacets() {
        List<SearchFacet> usFacets = new ArrayList<>();
        return usFacets;
    }
    
    private void init(QueryResponse res) {
        numFound = res.getResults().getNumFound();
        
        artistFacets = convertFacets();
    }
    
    @Override
    public long getNumFound() {
        return numFound;
    }

    @Override
    public List<SearchFacet> getArtistFacets() {
        return artistFacets;
    }

    @Override
    public List<SearchFacet> getAlbumFacets() {
        return albumFacets;
    }

    @Override
    public List<SearchFacet> getGenreFacets() {
        return genreFacets;
    }

    @Override
    public List<SearchFacet> getDecadeFacets() {
        return decadeFacets;
    }

    @Override
    public List<Media> getResults() {
        return results;
    }
}

package com.totalchange.discodj.search.solr;

import java.util.List;

import org.apache.solr.client.solrj.response.QueryResponse;

import com.totalchange.discodj.media.Media;
import com.totalchange.discodj.search.SearchFacet;
import com.totalchange.discodj.search.SearchResults;

class SolrSearchResultsImpl implements SearchResults {
    SolrSearchResultsImpl(QueryResponse queryResponse) {
        
    }
    
    @Override
    public int getNumPages() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<SearchFacet> getArtistFacets() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<SearchFacet> getAlbumFacets() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<SearchFacet> getGenreFacets() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<SearchFacet> getDecadeFacets() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Media> getResults() {
        // TODO Auto-generated method stub
        return null;
    }
}

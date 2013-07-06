package com.totalchange.discodj.search.solr;

import org.apache.solr.client.solrj.response.FacetField.Count;

import com.totalchange.discodj.search.SearchFacet;

final class SolrSearchFacetImpl implements SearchFacet {
    private Count count;
    
    SolrSearchFacetImpl(Count count) {
        this.count = count;
    }
    
    @Override
    public String getId() {
        return count.getAsFilterQuery();
    }

    @Override
    public String getName() {
        return count.getName();
    }

    @Override
    public long getNumMatches() {
        return count.getCount();
    }
}

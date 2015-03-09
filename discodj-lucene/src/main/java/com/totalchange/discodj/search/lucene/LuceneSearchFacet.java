package com.totalchange.discodj.search.lucene;

import org.apache.lucene.facet.LabelAndValue;

import com.totalchange.discodj.search.SearchFacet;

class LuceneSearchFacet implements SearchFacet {
    private final String id;
    private final String name;
    private final long numMatches;

    LuceneSearchFacet(String fieldName, LabelAndValue facet) {
        id = fieldName + ":" + facet.label;
        name = facet.label;
        numMatches = facet.value.longValue();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getNumMatches() {
        return numMatches;
    }

}

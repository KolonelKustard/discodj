package com.totalchange.discodj.search;

import java.util.ArrayList;
import java.util.List;

public class SearchQuery {
    private long start;
    private long rows;
    private String keywords;
    private List<String> facetIds = new ArrayList<>();

    public long getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public long getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public void addFacetId(String facetId) {
        this.facetIds.add(facetId);
    }

    public List<String> getFacetIds() {
        return facetIds;
    }
}

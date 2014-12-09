package com.totalchange.discodj.ws.search;

public class SearchFacet {
    private String id = null;
    private String name = null;
    private long numMatches = 0;
    private boolean selected = false;

    public SearchFacet(String id, String name, long numMatches, boolean selected) {
        this.id = id;
        this.name = name;
        this.numMatches = numMatches;
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getNumMatches() {
        return numMatches;
    }

    public boolean isSelected() {
        return selected;
    }
}

package com.totalchange.discodj.ws.search;

public class SearchFacet {
    private String id = null;
    private String name = null;
    private long numMatches = 0;
    private boolean selected = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getNumMatches() {
        return numMatches;
    }

    public void setNumMatches(long numMatches) {
        this.numMatches = numMatches;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

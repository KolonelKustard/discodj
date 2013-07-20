package com.totalchange.discodj.web.shared.dj;

import java.io.Serializable;

public class SearchFacet implements Serializable {
    private static final long serialVersionUID = 8103331032894574704L;

    private String id = null;
    private String name = null;
    private long numMatches = 0;
    private boolean selected = false;

    public SearchFacet() {
        // For serialiser
    }

    public SearchFacet(String id, String name, long numMatches, boolean selected) {
        this.id = id;
        this.name = name;
        this.numMatches = numMatches;
        this.selected = selected;
    }

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

    public void setOccurrences(long numMatches) {
        this.numMatches = numMatches;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

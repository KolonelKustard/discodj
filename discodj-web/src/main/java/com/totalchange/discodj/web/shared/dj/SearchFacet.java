package com.totalchange.discodj.web.shared.dj;

import java.io.Serializable;

public class SearchFacet implements Serializable {
    private static final long serialVersionUID = 8103331032894574704L;

    private String id;
    private String name;
    private long numMatches;

    public SearchFacet() {
        // For serialiser
    }

    public SearchFacet(String id, String name, long numMatches) {
        this.id = id;
        this.name = name;
        this.numMatches = numMatches;
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
}

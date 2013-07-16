package com.totalchange.discodj.web.shared.dj;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SearchFacet implements IsSerializable {
    private String id;
    private String name;
    private long numMatches;

    public SearchFacet(String id, String name, long numMatches) {
        this.id = id;
        this.name = name;
        this.numMatches = numMatches;
    }

    String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    long getNumMatches() {
        return numMatches;
    }

    void setOccurrences(long numMatches) {
        this.numMatches = numMatches;
    }
}

package com.totalchange.discodj.web.shared.dj;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SearchFacet implements IsSerializable {
    private String id;
    private String name;
    private int occurrences;

    public SearchFacet(String id, String name, int occurrences) {
        this.id = id;
        this.name = name;
        this.occurrences = occurrences;
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

    int getOccurrences() {
        return occurrences;
    }

    void setOccurrences(int occurrences) {
        this.occurrences = occurrences;
    }
}

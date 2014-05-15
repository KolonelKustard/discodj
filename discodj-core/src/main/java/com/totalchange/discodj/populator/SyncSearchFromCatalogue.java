package com.totalchange.discodj.populator;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.totalchange.discodj.catalogue.Catalogue;
import com.totalchange.discodj.search.SearchProvider;

@Singleton
public class SyncSearchFromCatalogue {
    private Catalogue catalogue;
    private SearchProvider searchProvider;

    @Inject
    public SyncSearchFromCatalogue(Catalogue catalogue,
            SearchProvider searchProvider) {
        this.catalogue = catalogue;
        this.searchProvider = searchProvider;
    }

    public boolean isInProgress() {
        return false;
    }

    public String getStatus() {
        return null;
    }

    public int getProgress() {
        return 0;
    }

    public void sync() {

    }

    public void fullRefresh() {

    }
}

package com.totalchange.discodj.search;

import org.junit.Test;

import com.totalchange.discodj.web.search.inject.IntegrationTestInjector;

public class SearchProviderTests {
    private void populateWithTestData() {

    }

    @Test
    public void keywordSearchOnArtist() {
        SearchProvider sp = IntegrationTestInjector
                .makeIntegrationTestInjector()
                .getInstance(SearchProvider.class);

        sp.repopulate();
    }
}

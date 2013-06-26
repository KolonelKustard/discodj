package com.totalchange.discodj.search;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.totalchange.discodj.media.Media;
import com.totalchange.discodj.web.search.inject.IntegrationTestInjector;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SearchProviderTests {
    private static final int NUM_TEST_MEDIA = 985364;

    private static SearchProvider searchProvider;

    private Media makeTestMedia(final int num) {
        return new Media() {
            @Override
            public int getYear() {
                return num;
            }

            @Override
            public String getTitle() {
                return "Test Title " + num;
            }

            @Override
            public String getRequestedBy() {
                return "Requested By " + num;
            }

            @Override
            public String getId() {
                return "Test ID " + num;
            }

            @Override
            public String getGenre() {
                return "Test Genre " + num;
            }

            @Override
            public String getArtist() {
                return "Test Artist " + num;
            }

            @Override
            public String getAlbum() {
                return "Test Album " + num;
            }
        };
    }

    @BeforeClass
    public static void setUp() {
        searchProvider = IntegrationTestInjector.makeIntegrationTestInjector()
                .getInstance(SearchProvider.class);
    }

    @AfterClass
    public static void shutdown() {
    }

    @Test
    public void order001_populateIndexes() {
        SearchPopulator pop = searchProvider.repopulate();
        for (int num = 0; num < NUM_TEST_MEDIA; num++) {
            pop.addMedia(makeTestMedia(num));
        }
        pop.commit();
    }

    @Test
    public void order002KeywordSearchOnArtist() {
        SearchQuery query = new SearchQuery();
        query.setKeywords("Test Artist 834");

        SearchResults res = searchProvider.search(query);
    }
}

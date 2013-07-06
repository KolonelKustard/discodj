package com.totalchange.discodj.search;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.totalchange.discodj.media.Media;
import com.totalchange.discodj.web.search.inject.IntegrationTestInjector;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SearchProviderTests {
    private static final int NUM_TEST_ARTISTS = 10;
    private static final int NUM_ALBUMS_PER_ARTIST = 10;
    private static final int NUM_TRACKS_PER_ALBUM = 10;
    private static final int START_DECADE = 1900;
    private static final int NUM_GENRES_TO_CYCLE_ARTISTS_THROUGH = 10;

    private static SearchProvider searchProvider;

    private Media makeTestMedia(final int artistNum, final int albumNum,
            final int titleNum, final int genreNum, final int decade) {
        return new Media() {
            @Override
            public int getYear() {
                return decade;
            }

            @Override
            public String getTitle() {
                return "Test Title " + titleNum;
            }

            @Override
            public String getRequestedBy() {
                return "Requested By Someone";
            }

            @Override
            public String getId() {
                return "Test Artist " + artistNum + ", Album " + albumNum
                        + ", Title " + titleNum;
            }

            @Override
            public String getGenre() {
                return "Test Genre " + genreNum;
            }

            @Override
            public String getArtist() {
                return "Test Artist " + artistNum;
            }

            @Override
            public String getAlbum() {
                return "Test Album " + albumNum;
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

        int genreNum = 0;
        for (int artistNum = 0; artistNum < NUM_TEST_ARTISTS; artistNum++) {
            int decade = START_DECADE;
            for (int albumNum = 0; albumNum < NUM_ALBUMS_PER_ARTIST; albumNum++) {
                for (int titleNum = 0; titleNum < NUM_TRACKS_PER_ALBUM; titleNum++) {
                    pop.addMedia(makeTestMedia(artistNum, albumNum, titleNum,
                            genreNum, decade));
                }
                decade += 10;
            }

            if (genreNum >= NUM_GENRES_TO_CYCLE_ARTISTS_THROUGH) {
                genreNum = 0;
            } else {
                genreNum++;
            }
        }
        pop.commit();
    }

    @Test
    public void order002KeywordSearchOnArtist() {
        SearchQuery query = new SearchQuery();
        query.setKeywords("\"Test Artist 5\"");

        SearchResults res = searchProvider.search(query);
        assertEquals(NUM_ALBUMS_PER_ARTIST * NUM_TRACKS_PER_ALBUM,
                res.getNumFound());
        assertEquals(1, res.getArtistFacets().size());
        assertEquals(NUM_ALBUMS_PER_ARTIST, res.getAlbumFacets().size());
        assertEquals(1, res.getGenreFacets().size());
        assertEquals(NUM_ALBUMS_PER_ARTIST, res.getDecadeFacets().size());
    }
}

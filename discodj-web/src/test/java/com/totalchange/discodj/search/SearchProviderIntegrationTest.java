package com.totalchange.discodj.search;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.totalchange.discodj.media.GenericMediaBuilder;
import com.totalchange.discodj.media.Media;
import com.totalchange.discodj.web.search.inject.IntegrationTestInjector;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SearchProviderIntegrationTest {
    private static final int NUM_TEST_ARTISTS = 1000;
    private static final int NUM_ALBUMS_PER_ARTIST = 10;
    private static final int NUM_TRACKS_PER_ALBUM = 10;
    private static final int START_DECADE = 1900;
    private static final int NUM_GENRES_TO_CYCLE_ARTISTS_THROUGH = 10;

    private SearchProvider searchProvider;

    private Media makeTestMedia(int artistNum, int albumNum, int titleNum,
            int genreNum, int decade) {
        return new GenericMediaBuilder()
                .withId("Test Artist " + artistNum + ", Album " + albumNum
                        + ", Title " + titleNum).withLastModified(new Date())
                .withArtist("Test Artist " + artistNum)
                .withAlbum("Test Album " + albumNum)
                .withTitle("Test Title " + titleNum)
                .withGenre("Test Genre " + genreNum).withYear(decade)
                .withRequestedBy("Request By Someone").build();
    }

    @Before
    public void setUp() {
        searchProvider = IntegrationTestInjector.getInjector()
                .getInstance(SearchProvider.class);
    }

    @AfterClass
    public static void shutdown() {
        SearchPopulator pop = IntegrationTestInjector
                .getInjector()
                .getInstance(SearchProvider.class).createPopulator();
        pop.deleteAll();
        pop.commit();
    }

    @Test
    public void order001_populateIndexes() {
        SearchPopulator pop = searchProvider.createPopulator();
        pop.deleteAll();

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

            if (genreNum >= NUM_GENRES_TO_CYCLE_ARTISTS_THROUGH - 1) {
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
        query.setKeywords("500");

        SearchResults res = searchProvider.search(query);
        assertEquals(NUM_ALBUMS_PER_ARTIST * NUM_TRACKS_PER_ALBUM,
                res.getNumFound());
        assertEquals(1, res.getArtistFacets().size());
        assertEquals(NUM_ALBUMS_PER_ARTIST, res.getAlbumFacets().size());
        assertEquals(1, res.getGenreFacets().size());
        assertEquals(NUM_ALBUMS_PER_ARTIST, res.getDecadeFacets().size());
    }

    @Test
    public void order003FacetedSearch() {
        SearchQuery query = new SearchQuery();
        SearchResults res = searchProvider.search(query);

        assertEquals(NUM_TEST_ARTISTS * NUM_ALBUMS_PER_ARTIST
                * NUM_TRACKS_PER_ALBUM, res.getNumFound());
        assertEquals(NUM_TEST_ARTISTS, res.getArtistFacets().size());
        assertEquals(NUM_ALBUMS_PER_ARTIST, res.getAlbumFacets().size());
        assertEquals(NUM_GENRES_TO_CYCLE_ARTISTS_THROUGH, res.getGenreFacets()
                .size());
        assertEquals(NUM_ALBUMS_PER_ARTIST, res.getDecadeFacets().size());

        List<SearchFacet> decadeFacets = new ArrayList<>();
        decadeFacets.add(res.getDecadeFacets().get(
                res.getDecadeFacets().size() / 2));
        for (SearchFacet facet : decadeFacets) {
            query.addFacetId(facet.getId());
        }
        res = searchProvider.search(query);

        assertEquals(NUM_TEST_ARTISTS * NUM_TRACKS_PER_ALBUM, res.getNumFound());
        assertEquals(NUM_TEST_ARTISTS, res.getArtistFacets().size());
        assertEquals(1, res.getAlbumFacets().size());
        assertEquals(NUM_GENRES_TO_CYCLE_ARTISTS_THROUGH, res.getGenreFacets()
                .size());
        assertEquals(1, res.getDecadeFacets().size());
    }
}

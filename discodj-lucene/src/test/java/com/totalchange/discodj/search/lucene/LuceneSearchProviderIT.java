package com.totalchange.discodj.search.lucene;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.totalchange.discodj.server.media.Media;
import com.totalchange.discodj.server.media.MediaEntity;
import com.totalchange.discodj.server.search.SearchPopulator;
import com.totalchange.discodj.server.search.SearchQuery;
import com.totalchange.discodj.server.search.SearchResults;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LuceneSearchProviderIT {
    private static final int NUM_ALBUMS_PER_ARTIST = 10;
    private static final int NUM_TRACKS_PER_ALBUM = 10;
    private static final int START_DECADE = 1900;
    private static final int NUM_GENRES_TO_CYCLE_ARTISTS_THROUGH = 10;

    private LuceneSearchProvider luceneSearchProvider;

    @Before
    public void setUp() throws IOException {
        File luceneDir = new File("target/lucene-search-provider-it");
        if (luceneDir.exists()) {
            FileUtils.deleteDirectory(luceneDir);
        }
        luceneDir.mkdirs();

        Directory directory = FSDirectory.open(luceneDir.toPath());
        luceneSearchProvider = new LuceneSearchProvider(directory);
    }

    @Test
    public void addALoadOfStuffSearchForItAndDeleteIt() throws ExecutionException, InterruptedException {
        checkIndexIsEmpty();
        checkSearchReturnsNothing();

        SearchPopulator searchPopulator = luceneSearchProvider
                .createPopulator();
        addLoadsOfStuff(searchPopulator);
        editSomeOfTheStuff(searchPopulator);
        deleteSomeOfTheStuff(searchPopulator);
        searchPopulator.commit();

        iterateOverAllThatStuff();
        searchForAllThatStuff();
        searchForTheStuffByArtist10();
        searchWithDifferentStartPoints();
        searchCantGoPastEnd();
        searchOnACoupleOfFacets();

        searchPopulator = luceneSearchProvider.createPopulator();
        deleteTheStuff(searchPopulator);
        searchPopulator.commit();

        checkIndexIsEmpty();
        checkSearchReturnsNothing();
    }

    private void addLoadsOfStuff(final SearchPopulator searchPopulator) {
        TakeMyMedia taker = new TakeMyMedia() {
            @Override
            public void takeIt(Media media) {
                searchPopulator.addMedia(media);
            }
        };

        // Make sure not addded in order to be fairer test of sorting
        makeLoadsOfMedia(700, 800, taker);
        makeLoadsOfMedia(900, 1000, taker);
        makeLoadsOfMedia(300, 400, taker);
        makeLoadsOfMedia(0, 100, taker);
        makeLoadsOfMedia(800, 900, taker);
        makeLoadsOfMedia(100, 200, taker);
        makeLoadsOfMedia(500, 600, taker);
        makeLoadsOfMedia(400, 500, taker);
        makeLoadsOfMedia(200, 300, taker);
        makeLoadsOfMedia(600, 700, taker);
    }

    private void editSomeOfTheStuff(final SearchPopulator searchPopulator) {
        makeLoadsOfMedia(200, 400, new TakeMyMedia() {
            @Override
            public void takeIt(Media media) {
                searchPopulator.updateMedia(media);
            }
        });
    }

    private void deleteSomeOfTheStuff(final SearchPopulator searchPopulator) {
        makeLoadsOfMedia(600, 700, new TakeMyMedia() {
            @Override
            public void takeIt(Media media) {
                searchPopulator.deleteMedia(media.getId());
            }
        });
    }

    private void checkIndexIsEmpty() throws ExecutionException, InterruptedException {
        List<MediaEntity> all = luceneSearchProvider.getAllMediaEntities("test").get();
        assertEquals("Search index is expected to be empty but has content", 0, all.size());
    }

    private void checkSearchReturnsNothing() {
        SearchResults res = luceneSearchProvider.search(new SearchQuery());
        assertEquals(0, res.getNumFound());
    }

    private void iterateOverAllThatStuff() throws ExecutionException, InterruptedException {
        final List<MediaEntity> all = luceneSearchProvider.getAllMediaEntities("test").get();
        all.sort(Comparator.comparing(MediaEntity::getId));
        final Iterator<MediaEntity> it = all.iterator();

        iterateOverAndAssertMedia(it, 0, 600);
        iterateOverAndAssertMedia(it, 700, 1000);
    }

    private void searchForAllThatStuff() {
        int totalResultsExpected = 900;

        SearchQuery query = new SearchQuery();
        query.setStart(15);
        query.setRows(15);

        SearchResults res = luceneSearchProvider.search(query);
        assertEquals(totalResultsExpected, res.getNumFound());
        assertEquals(totalResultsExpected / 100, res.getArtistFacets().size());
        assertEquals(NUM_ALBUMS_PER_ARTIST, res.getAlbumFacets().size());
        assertEquals(totalResultsExpected / 100, res.getGenreFacets().size());
        assertEquals(NUM_ALBUMS_PER_ARTIST, res.getDecadeFacets().size());
        assertEquals(15, res.getResults().size());
    }

    private void searchForTheStuffByArtist10() {
        SearchQuery query = new SearchQuery();
        query.setRows(10);
        query.setKeywords("\"Test Artist 10\"");

        SearchResults res = luceneSearchProvider.search(query);
        assertEquals(NUM_ALBUMS_PER_ARTIST * NUM_TRACKS_PER_ALBUM,
                res.getNumFound());
        assertEquals(1, res.getArtistFacets().size());
        assertEquals(NUM_ALBUMS_PER_ARTIST, res.getAlbumFacets().size());
        assertEquals(1, res.getGenreFacets().size());
        assertEquals(NUM_ALBUMS_PER_ARTIST, res.getDecadeFacets().size());
        assertEquals(10, res.getResults().size());
    }

    private void searchWithDifferentStartPoints() {
        String lastId = "";
        for (int start = 0; start < 10; start++) {
            SearchQuery query = new SearchQuery();
            query.setStart(start);
            query.setRows(1);

            SearchResults res = luceneSearchProvider.search(query);
            assertEquals(1, res.getResults().size());
            assertNotEquals("Should have moved onto the next page of results",
                    lastId, res.getResults().get(0).getId());
            lastId = res.getResults().get(0).getId();
        }
    }

    private void searchCantGoPastEnd() {
        SearchQuery query = new SearchQuery();
        query.setStart(875);
        query.setRows(50);

        SearchResults res = luceneSearchProvider.search(query);
        assertEquals(25, res.getResults().size());
    }

    private void searchOnACoupleOfFacets() {
        SearchQuery query = new SearchQuery();
        query.setRows(1000);
        query.addFacetId(LuceneSearchProvider.F_FACET_ARTIST + ":Test Artist 5");
        query.addFacetId(LuceneSearchProvider.F_FACET_ALBUM + ":Test Album 7");

        SearchResults res = luceneSearchProvider.search(query);
        assertEquals(NUM_TRACKS_PER_ALBUM, res.getResults().size());
    }

    private void deleteTheStuff(SearchPopulator searchPopulator) {
        searchPopulator.deleteAll();
    }

    private void makeLoadsOfMedia(int start, int end, TakeMyMedia taker) {
        for (int num = start; num < end; num++) {
            taker.takeIt(makeTestMedia(num));
        }
    }

    private Media makeTestMedia(int id) {
        int artistNum = (id / 100) + 1;
        int albumNum = ((id % 100) / 10) + 1;
        int decade = START_DECADE + ((albumNum - 1) * 10);
        int titleNum = (id % 10) + 1;
        int genreNum = artistNum % NUM_GENRES_TO_CYCLE_ARTISTS_THROUGH;

        return new GenericMedia.Builder()
                .withId(String.format("%10d", id))
                .withSourceId("test")
                .withLastModifiedMs(id + artistNum + albumNum + decade + titleNum + genreNum)
                .withArtist("Test Artist " + artistNum)
                .withAlbum("Test Album " + albumNum)
                .withTitle("Test Title " + titleNum)
                .withGenre("Test Genre " + genreNum).withYear(decade)
                .withRequestedBy("Request By Someone")
                .withUri(URI.create("http://dicsodj/" + id))
                .build();
    }

    private interface TakeMyMedia {
        void takeIt(Media media);
    }

    private void iterateOverAndAssertMedia(Iterator<MediaEntity> it,
            int start, int end) {
        for (int num = start; num < end; num++) {
            assertTrue("List all alphabetically has ended prematurely on iteration " + num, it.hasNext());
            MediaEntity entity = it.next();
            Media media = makeTestMedia(num);

            assertEquals(media.getId(), entity.getId());
            assertEquals(media.getLastModifiedMs(), entity.getLastModifiedMs());
        }
    }
}

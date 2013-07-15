package com.totalchange.discodj.catalogue;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.totalchange.discodj.media.Media;
import com.totalchange.discodj.web.search.inject.IntegrationTestInjector;

public class CatalogueTests {
    private static Catalogue catalogue;

    @BeforeClass
    public static void setUp() {
        catalogue = IntegrationTestInjector.makeIntegrationTestInjector()
                .getInstance(Catalogue.class);
    }

    private String makeMediaId(String filename) {
        File root = new File("./src/test/catalogue");
        return new File(root, filename).getAbsolutePath();
    }
    
    private void assertAndPopMedia(Media media) {
        
    }

    @Test
    public void listAllInCatalogue() {
        List<Media> expectedMedia = new ArrayList<>();
        expectedMedia.add(new TestMediaBuilder()
                .withId(makeMediaId("test.mp3")).withArtist("Test Artist")
                .withAlbum("Test Album").withGenre("Test Genre").withYear(1980)
                .withRequestedBy(null).withTitle("Test Song 1").build());
        expectedMedia.add(new TestMediaBuilder()
                .withId(makeMediaId("test.mp4")).withArtist("Test Artist")
                .withAlbum("Test Album").withGenre("Test Genre").withYear(1980)
                .withRequestedBy(null).withTitle("Test Video 1").build());

        catalogue.listAllSongs(new Catalogue.Listener() {
            @Override
            public void yetMoreMedia(Media media) {

            }

            @Override
            public void warn(String msg, Throwable cause) {
                throw new RuntimeException("Should not fail - " + msg, cause);
            }
        });
        assertEquals("All expected media should have been processed", 0,
                expectedMedia.size());
    }
}

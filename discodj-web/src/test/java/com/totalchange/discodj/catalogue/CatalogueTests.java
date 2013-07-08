package com.totalchange.discodj.catalogue;

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

    @Test
    public void listAllInCatalogue() {
        catalogue.listAllSongs(new Catalogue.Listener() {
            @Override
            public void yetMoreMedia(Media media) {
                // TODO Auto-generated method stub

            }

            @Override
            public void warn(String msg, Throwable cause) {
                // TODO Auto-generated method stub

            }
        });
    }
}

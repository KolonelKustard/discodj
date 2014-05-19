package com.totalchange.discodj.search.solr;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collection;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.totalchange.discodj.media.AbstractMedia;
import com.totalchange.discodj.media.Media;

public class SolrSearchPopulatorImplTests {
    private SolrServer solrServer;
    private SolrSearchPopulatorImpl populator;

    @Before
    public void setUp() {
        solrServer = mock(SolrServer.class);
        populator = new SolrSearchPopulatorImpl(solrServer);
    }

    @Test
    public void add9635MediaItems() throws IOException, SolrServerException {
        int numToAdd = 9635;

        for (int num = 0; num < numToAdd; num++) {
            populator.addMedia(makeFakeMedia("Test ID " + num, "Test Artist "
                    + num, "Test Album " + num, "Test Title " + num,
                    "Test Genre " + num, num, "Test Requested By " + num));
        }
        populator.commit();

        for (int num = 0; num < numToAdd; num++) {
            verify(solrServer).add(argThat(docMatcher(num)));
        }
        verify(solrServer).commit();
    }

    private Matcher<SolrInputDocument> docMatcher(final int num) {
        return new ArgumentMatcher<SolrInputDocument>() {
            @Override
            public boolean matches(Object item) {
                SolrInputDocument doc = (SolrInputDocument) item;
                System.out.println(num + ": " + doc);
                return doc.getFieldValue(SolrSearchProviderImpl.F_ID).equals(
                        "Test ID " + num)
                        && doc.getFieldValue(SolrSearchProviderImpl.F_ARTIST)
                                .equals("Test Artist " + num)
                        && doc.getFieldValue(SolrSearchProviderImpl.F_ALBUM)
                                .equals("Test Album " + num)
                        && doc.getFieldValue(SolrSearchProviderImpl.F_GENRE)
                                .equals("Test Genre " + num)
                        && doc.getFieldValue(
                                SolrSearchProviderImpl.F_REQUESTED_BY).equals(
                                "Test Requested By " + num)
                        && doc.getFieldValue(SolrSearchProviderImpl.F_YEAR)
                                .equals(String.valueOf(num))
                        && doc.getFieldValue(SolrSearchProviderImpl.F_TITLE)
                                .equals("Test Title " + num);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Test SOLR Document number " + num);
            }
        };
    }

    private Media makeFakeMedia(final String id, final String artist,
            final String album, final String title, final String genre,
            final int year, final String requestedBy) {
        return new AbstractMedia() {
            @Override
            public int getYear() {
                return year;
            }

            @Override
            public String getTitle() {
                return title;
            }

            @Override
            public String getRequestedBy() {
                return requestedBy;
            }

            @Override
            public String getId() {
                return id;
            }

            @Override
            public String getGenre() {
                return genre;
            }

            @Override
            public String getArtist() {
                return artist;
            }

            @Override
            public String getAlbum() {
                return album;
            }
        };
    }
}

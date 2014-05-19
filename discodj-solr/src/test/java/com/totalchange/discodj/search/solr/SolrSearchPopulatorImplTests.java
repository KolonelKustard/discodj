package com.totalchange.discodj.search.solr;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collection;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;
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
    }

    private int assertDocs(Collection<SolrInputDocument> docs, int numAdded) {
        for (SolrInputDocument doc : docs) {
            numAdded++;
            final int copyOfNum = numAdded;
            assertEquals("Test ID " + copyOfNum,
                    doc.getFieldValue(SolrSearchProviderImpl.F_ID));
            assertEquals("Test Artist " + copyOfNum,
                    doc.getFieldValue(SolrSearchProviderImpl.F_ARTIST));
            assertEquals("Test Album " + copyOfNum,
                    doc.getFieldValue(SolrSearchProviderImpl.F_ALBUM));
            assertEquals("Test Genre " + copyOfNum,
                    doc.getFieldValue(SolrSearchProviderImpl.F_GENRE));
            assertEquals("Test Requested By " + copyOfNum,
                    doc.getFieldValue(SolrSearchProviderImpl.F_REQUESTED_BY));
            assertEquals(copyOfNum,
                    doc.getFieldValue(SolrSearchProviderImpl.F_YEAR));
            assertEquals("Test Title " + copyOfNum,
                    doc.getFieldValue(SolrSearchProviderImpl.F_TITLE));
        }
        return numAdded;
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

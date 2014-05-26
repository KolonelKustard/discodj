package com.totalchange.discodj.search.solr;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.totalchange.discodj.media.GenericMediaBuilder;
import com.totalchange.discodj.media.Media;

public class SolrSearchPopulatorImplTest {
    private SolrServer solrServer;
    private SolrSearchPopulatorImpl populator;

    @Before
    public void setUp() {
        solrServer = mock(SolrServer.class);
        populator = new SolrSearchPopulatorImpl(solrServer);
    }

    @Test
    public void add4657AndUpdate3654MediaItems() throws IOException,
            SolrServerException {
        int numToAdd = 9635;
        int numToUpdate = 3654;

        when(solrServer.add(any(SolrInputDocument.class))).then(
                new IncrementalDocAnswerAsserter());

        for (int num = 0; num < numToAdd; num++) {
            Media media = new GenericMediaBuilder().withId("Test ID " + num)
                    .withArtist("Test Artist " + num)
                    .withAlbum("Test Album " + num)
                    .withTitle("Test Title " + num)
                    .withGenre("Test Genre " + num).withYear(num)
                    .withRequestedBy("Test Requested By " + num).build();
            populator.addMedia(media);
        }

        for (int num = numToAdd; num < numToUpdate; num++) {
            Media media = new GenericMediaBuilder().withId("Test ID " + num)
                    .withArtist("Test Artist " + num)
                    .withAlbum("Test Album " + num)
                    .withTitle("Test Title " + num)
                    .withGenre("Test Genre " + num).withYear(num)
                    .withRequestedBy("Test Requested By " + num).build();
            populator.updateMedia(media);
        }
    }

    @Test
    public void passesOnDeleteById() throws IOException, SolrServerException {
        populator.deleteMedia("123");
        verify(solrServer).deleteById("123");
    }

    @Test
    public void deletesAll() throws IOException, SolrServerException {
        populator.deleteAll();
        verify(solrServer).deleteByQuery("*:*");
    }

    @Test
    public void passesOnCommit() throws IOException, SolrServerException {
        populator.commit();
        verify(solrServer).commit();
    }

    private class IncrementalDocAnswerAsserter implements Answer<Object> {
        private int num = 0;

        /**
         * Intentionally using an alternate implementation so got a bit of
         * confidence the decade parsing is working.
         * 
         * @param year
         *            year to round down to nearest decade
         * @return the decade the year occurs in
         */
        private int toDecade(int year) {
            String y = String.valueOf(year);
            y = y.substring(0, y.length() - 1);
            y += "0";
            return Integer.parseInt(y);
        }

        @Override
        public Object answer(InvocationOnMock invocation) throws Throwable {
            SolrInputDocument doc = (SolrInputDocument) invocation
                    .getArguments()[0];

            assertEquals("ID for " + doc.toString(), "Test ID " + num,
                    doc.getFieldValue(SolrSearchProviderImpl.F_ID));
            assertEquals("Artist for " + doc.toString(), "Test Artist " + num,
                    doc.getFieldValue(SolrSearchProviderImpl.F_ARTIST));
            assertEquals("Album for " + doc.toString(), "Test Album " + num,
                    doc.getFieldValue(SolrSearchProviderImpl.F_ALBUM));
            assertEquals("Genre for " + doc.toString(), "Test Genre " + num,
                    doc.getFieldValue(SolrSearchProviderImpl.F_GENRE));
            assertEquals("Requested By for " + doc.toString(),
                    "Test Requested By " + num,
                    doc.getFieldValue(SolrSearchProviderImpl.F_REQUESTED_BY));
            assertEquals("Year for " + doc.toString(), num,
                    doc.getFieldValue(SolrSearchProviderImpl.F_YEAR));
            assertEquals("Decade for " + doc.toString(), toDecade(num),
                    doc.getFieldValue(SolrSearchProviderImpl.F_DECADE));
            assertEquals("Title for " + doc.toString(), "Test Title " + num,
                    doc.getFieldValue(SolrSearchProviderImpl.F_TITLE));

            num++;
            return null;
        }
    }
}

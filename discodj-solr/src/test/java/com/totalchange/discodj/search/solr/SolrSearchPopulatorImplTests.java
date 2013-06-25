package com.totalchange.discodj.search.solr;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collection;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.easymock.EasyMockSupport;
import org.easymock.IAnswer;
import org.junit.Before;
import org.junit.Test;

public class SolrSearchPopulatorImplTests extends EasyMockSupport {
    private SolrServer solrServer;
    private SolrSearchPopulatorImpl populator;

    @Before
    public void setUp() {
        solrServer = createMock(SolrServer.class);
        populator = new SolrSearchPopulatorImpl(solrServer);
    }

    @Test
    public void deletesAllOnCreate() throws IOException, SolrServerException {
        expect(solrServer.deleteByQuery("*:*")).andStubReturn(null);
        expect(solrServer.commit()).andStubReturn(null);

        replayAll();
        populator = new SolrSearchPopulatorImpl(solrServer);
        verifyAll();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void add9635MediaItems() throws IOException, SolrServerException {
        int numToAdd = 9635;

        int numOfCommits = numToAdd / 1000;
        for (int num = 0; num < numOfCommits; num++) {
            final int copyOfNum = num;

            solrServer.add(anyObject(Collection.class));
            expectLastCall().andAnswer(new IAnswer<Object>() {
                @Override
                public Object answer() throws Throwable {
                    Collection<SolrInputDocument> docs = (Collection<SolrInputDocument>) getCurrentArguments()[0];
                    for (SolrInputDocument doc : docs) {
                        assertEquals("Test ID " + copyOfNum,
                                doc.getFieldValue(SolrSearchProviderImpl.F_ID));
                        assertEquals("Test Artist " + copyOfNum, doc
                                .getFieldValue(SolrSearchProviderImpl.F_ARTIST));
                        assertEquals("Test Album " + copyOfNum, doc
                                .getFieldValue(SolrSearchProviderImpl.F_ALBUM));
                        assertEquals("Test Genre " + copyOfNum, doc
                                .getFieldValue(SolrSearchProviderImpl.F_GENRE));
                        assertEquals(
                                "Test Requested By " + copyOfNum,
                                doc.getFieldValue(SolrSearchProviderImpl.F_REQUESTED_BY));
                        assertEquals(copyOfNum, doc
                                .getFieldValue(SolrSearchProviderImpl.F_YEAR));
                        assertEquals("Test Title " + copyOfNum, doc
                                .getFieldValue(SolrSearchProviderImpl.F_TITLE));
                    }
                    return null;
                }
            });
        }

        replayAll();
        populator.addMedia(null);
        populator.commit();
        verifyAll();
    }
}

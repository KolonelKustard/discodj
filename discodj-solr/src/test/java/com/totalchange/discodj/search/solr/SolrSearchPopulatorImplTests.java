package com.totalchange.discodj.search.solr;

import static org.easymock.EasyMock.*;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.easymock.EasyMockSupport;
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
    public void add9635MediaItems() {
        int numToAdd = 9635;
        
        
        replayAll();
        populator.addMedia(null);
        populator.commit();
        verifyAll();
    }
}

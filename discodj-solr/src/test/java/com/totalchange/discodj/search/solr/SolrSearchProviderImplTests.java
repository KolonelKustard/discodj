package com.totalchange.discodj.search.solr;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.easymock.EasyMockSupport;
import org.easymock.IAnswer;
import org.junit.Before;
import org.junit.Test;

import com.totalchange.discodj.media.Media;
import com.totalchange.discodj.search.SearchPopulator;
import com.totalchange.discodj.search.SearchQuery;
import com.totalchange.discodj.search.SearchResults;

public class SolrSearchProviderImplTests extends EasyMockSupport {
    private SolrServer solrServer;
    private SolrSearchProviderImpl searchProvider;

    @Before
    public void setUp() {
        solrServer = createMock(SolrServer.class);
        searchProvider = new SolrSearchProviderImpl(solrServer);
    }

    @Test
    public void createRepopulater() throws IOException, SolrServerException {
        expect(solrServer.deleteByQuery("*:*")).andStubReturn(null);
        expect(solrServer.commit()).andStubReturn(null);

        replayAll();
        SearchPopulator populator = searchProvider.repopulate();
        verifyAll();

        assertTrue(populator instanceof SolrSearchPopulatorImpl);
    }

    @Test
    public void searchForKeywordsOnly() throws SolrServerException {
        SearchQuery query = new SearchQuery();
        query.setKeywords("Test");

        SolrDocumentList docs = createMock(SolrDocumentList.class);
        expect(docs.getNumFound()).andReturn(1050l);
        expect(docs.size()).andReturn(10);

        List<SolrDocument> docsList = new ArrayList<>(10);
        for (int num = 0; num < 10; num++) {
            SolrDocument doc = createMock(SolrDocument.class);
            expect(doc.get(SolrSearchProviderImpl.F_ID)).andReturn(
                    "Test ID " + num);
            expect(doc.get(SolrSearchProviderImpl.F_ARTIST)).andReturn(
                    "Test Artist " + num);
            expect(doc.get(SolrSearchProviderImpl.F_ALBUM)).andReturn(
                    "Test Album " + num);
            expect(doc.get(SolrSearchProviderImpl.F_GENRE)).andReturn(
                    "Test Genre " + num);
            expect(doc.get(SolrSearchProviderImpl.F_YEAR)).andReturn(num);
            expect(doc.get(SolrSearchProviderImpl.F_REQUESTED_BY)).andReturn(
                    "Test Requested By " + num);
            expect(doc.get(SolrSearchProviderImpl.F_TITLE)).andReturn(
                    "Test Title " + num);

            docsList.add(doc);
        }
        expect(docs.iterator()).andReturn(docsList.iterator());

        final QueryResponse response = createMock(QueryResponse.class);
        expect(response.getResults()).andReturn(docs);
        expect(response.getFacetField(SolrSearchProviderImpl.F_ARTIST))
                .andReturn(null);
        expect(response.getFacetField(SolrSearchProviderImpl.F_ALBUM))
                .andReturn(null);
        expect(response.getFacetField(SolrSearchProviderImpl.F_GENRE))
                .andReturn(null);
        expect(response.getFacetField(SolrSearchProviderImpl.F_YEAR))
                .andReturn(null);

        expect(solrServer.query(anyObject(SolrQuery.class))).andAnswer(
                new IAnswer<QueryResponse>() {
                    @Override
                    public QueryResponse answer() throws Throwable {
                        SolrQuery sq = (SolrQuery) getCurrentArguments()[0];
                        assertEquals("text:Test", sq.getQuery());

                        return response;
                    }
                });

        replayAll();
        SearchResults results = searchProvider.search(query);
        assertEquals(1050, results.getNumFound());
        assertEquals(10, results.getResults().size());
        for (int num = 0; num < results.getResults().size(); num++) {
            Media media = results.getResults().get(num);
            assertEquals("Test ID " + num, media.getId());
            assertEquals("Test Artist " + num, media.getArtist());
            assertEquals("Test Album " + num, media.getAlbum());
            assertEquals("Test Genre " + num, media.getGenre());
            assertEquals(num, media.getYear());
            assertEquals("Test Requested By " + num, media.getRequestedBy());
            assertEquals("Test Title " + num, media.getTitle());
        }
        verifyAll();
    }
}

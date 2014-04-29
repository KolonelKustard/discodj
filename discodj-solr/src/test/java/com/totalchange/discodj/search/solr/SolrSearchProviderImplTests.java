package com.totalchange.discodj.search.solr;

import static org.mockito.Mockito.*;
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
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.totalchange.discodj.media.Media;
import com.totalchange.discodj.search.SearchPopulator;
import com.totalchange.discodj.search.SearchQuery;
import com.totalchange.discodj.search.SearchResults;

public class SolrSearchProviderImplTests {
    private SolrServer solrServer;
    private SolrSearchProviderImpl searchProvider;

    @Before
    public void setUp() {
        solrServer = mock(SolrServer.class);
        searchProvider = new SolrSearchProviderImpl(solrServer);
    }

    @Test
    public void createRepopulater() throws IOException, SolrServerException {
        when(solrServer.deleteByQuery("*:*")).thenReturn(null);
        when(solrServer.commit()).thenReturn(null);

        SearchPopulator populator = searchProvider.repopulate();

        assertTrue(populator instanceof SolrSearchPopulatorImpl);
    }

    @Test
    public void searchForKeywordsOnly() throws SolrServerException {
        SearchQuery query = new SearchQuery();
        query.setKeywords("Test");

        SolrDocumentList docs = mock(SolrDocumentList.class);
        when(docs.getNumFound()).thenReturn(1050l);
        when(docs.size()).thenReturn(10);

        List<SolrDocument> docsList = new ArrayList<>(10);
        for (int num = 0; num < 10; num++) {
            SolrDocument doc = mock(SolrDocument.class);
            when(doc.get(SolrSearchProviderImpl.F_ID)).thenReturn(
                    "Test ID " + num);
            when(doc.get(SolrSearchProviderImpl.F_ARTIST)).thenReturn(
                    "Test Artist " + num);
            when(doc.get(SolrSearchProviderImpl.F_ALBUM)).thenReturn(
                    "Test Album " + num);
            when(doc.get(SolrSearchProviderImpl.F_GENRE)).thenReturn(
                    "Test Genre " + num);
            when(doc.get(SolrSearchProviderImpl.F_YEAR)).thenReturn(num);
            when(doc.get(SolrSearchProviderImpl.F_REQUESTED_BY)).thenReturn(
                    "Test Requested By " + num);
            when(doc.get(SolrSearchProviderImpl.F_TITLE)).thenReturn(
                    "Test Title " + num);

            docsList.add(doc);
        }
        when(docs.iterator()).thenReturn(docsList.iterator());

        final QueryResponse response = mock(QueryResponse.class);
        when(response.getResults()).thenReturn(docs);
        when(response.getFacetField(SolrSearchProviderImpl.F_ARTIST))
                .thenReturn(null);
        when(response.getFacetField(SolrSearchProviderImpl.F_ALBUM))
                .thenReturn(null);
        when(response.getFacetField(SolrSearchProviderImpl.F_GENRE))
                .thenReturn(null);
        when(response.getFacetField(SolrSearchProviderImpl.F_YEAR))
                .thenReturn(null);

        when(solrServer.query(any(SolrQuery.class))).thenAnswer(
                new Answer<QueryResponse>() {
                    @Override
                    public QueryResponse answer(InvocationOnMock invocation) throws Throwable {
                        SolrQuery sq = (SolrQuery) invocation.getArguments()[0];
                        assertEquals("text:Test", sq.getQuery());

                        return response;
                    }
                });

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
    }
}

/**
 * Copyright 2015 Ralph Jones
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.totalchange.discodj.search.solr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

public class SolrCatalogueEntityIteratorTest {
    private SolrServer solrServer;
    private SolrCatalogueEntityIterator solrCatalogueEntityIterator;

    @Before
    public void setUp() {
        solrServer = mock(SolrServer.class);
    }

    @Test
    public void shouldPageResultsOk() throws SolrServerException {
        QueryResponse res1 = makeDocList("1", "2");
        QueryResponse res2 = makeDocList("3", "4", "5");
        QueryResponse res3 = makeDocList("6");
        QueryResponse res4 = makeDocList();

        when(solrServer.query(argThat(new SolrQueryMatcher(0)))).thenReturn(
                res1);
        when(solrServer.query(argThat(new SolrQueryMatcher(2)))).thenReturn(
                res2);
        when(solrServer.query(argThat(new SolrQueryMatcher(5)))).thenReturn(
                res3);
        when(solrServer.query(argThat(new SolrQueryMatcher(6)))).thenReturn(
                res4);

        solrCatalogueEntityIterator = new SolrCatalogueEntityIterator(
                solrServer);

        assertTrue(solrCatalogueEntityIterator.hasNext());
        assertEquals("1", solrCatalogueEntityIterator.next().getId());
        assertTrue(solrCatalogueEntityIterator.hasNext());
        assertEquals("2", solrCatalogueEntityIterator.next().getId());
        assertTrue(solrCatalogueEntityIterator.hasNext());
        assertEquals("3", solrCatalogueEntityIterator.next().getId());
        assertTrue(solrCatalogueEntityIterator.hasNext());
        assertEquals("4", solrCatalogueEntityIterator.next().getId());
        assertTrue(solrCatalogueEntityIterator.hasNext());
        assertEquals("5", solrCatalogueEntityIterator.next().getId());
        assertTrue(solrCatalogueEntityIterator.hasNext());
        assertEquals("6", solrCatalogueEntityIterator.next().getId());
        assertFalse(solrCatalogueEntityIterator.hasNext());
    }

    private QueryResponse makeDocList(String... ids) {
        SolrDocumentList list = new SolrDocumentList();
        for (String id : ids) {
            SolrDocument doc = new SolrDocument();
            doc.setField(SolrSearchProviderImpl.F_ID, id);
            list.add(doc);
        }

        QueryResponse res = mock(QueryResponse.class);
        when(res.getResults()).thenReturn(list);

        return res;
    }

    private class SolrQueryMatcher extends ArgumentMatcher<SolrQuery> {
        private int start;

        private SolrQueryMatcher(int start) {
            this.start = start;
        }

        @Override
        public boolean matches(Object argument) {
            if (argument == null) {
                return false;
            } else {
                SolrQuery query = (SolrQuery) argument;
                return query.getStart() == start;
            }
        }
    }
}

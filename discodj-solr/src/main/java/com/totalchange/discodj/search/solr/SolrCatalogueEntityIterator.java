package com.totalchange.discodj.search.solr;

import java.util.Iterator;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import com.totalchange.discodj.catalogue.Catalogue.CatalogueEntity;
import com.totalchange.discodj.search.SearchException;

public class SolrCatalogueEntityIterator implements Iterator<CatalogueEntity> {
    private static final int NUM_PER_PAGE = 10000;
    private static final int OFFSET_FINISHED = -1;

    private SolrServer solrServer;

    private int currentPage = 0;
    private SolrDocumentList docs;
    private int nextOffset;

    public SolrCatalogueEntityIterator(SolrServer solrServer)
            throws SolrSearchException {
        this.solrServer = solrServer;
        getNextLoadFromSolr();
    }

    private void getNextLoadFromSolr() throws SolrSearchException {
        SolrQuery sq = new SolrQuery();
        sq.setQuery("*:*");
        sq.setSort(SolrSearchProviderImpl.F_ID, SolrQuery.ORDER.asc);
        sq.setStart(currentPage * NUM_PER_PAGE);
        sq.setRows(NUM_PER_PAGE);

        try {
            QueryResponse res = solrServer.query(sq);
            docs = res.getResults();
        } catch (SolrServerException sEx) {
            throw new SolrSearchException(sEx);
        }
    }

    @Override
    public boolean hasNext() {
        return nextOffset != OFFSET_FINISHED;
    }

    @Override
    public CatalogueEntity next() throws SolrSearchException {
        CatalogueEntity next = new SolrCatalogueEntity(docs.get(nextOffset));

        nextOffset++;
        if (nextOffset >= docs.size()) {
            getNextLoadFromSolr();
        }

        return next;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}

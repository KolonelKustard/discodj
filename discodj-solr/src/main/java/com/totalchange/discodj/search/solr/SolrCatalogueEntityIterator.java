package com.totalchange.discodj.search.solr;

import java.util.Iterator;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import com.totalchange.discodj.catalogue.Catalogue.CatalogueEntity;

class SolrCatalogueEntityIterator implements Iterator<CatalogueEntity> {
    private static final int PER_PAGE = 5000;
    private static final int NONE_LEFT = -1;

    private SolrServer solrServer;

    private int nextPageStart = 0;
    private SolrDocumentList thisPage;
    private int nextOffset;

    SolrCatalogueEntityIterator(SolrServer solrServer)
            throws SolrSearchException {
        this.solrServer = solrServer;
        grabTheNextPage();
    }

    private void grabTheNextPage() throws SolrSearchException {
        SolrQuery sq = new SolrQuery();
        sq.setFields(SolrSearchProviderImpl.F_ID);
        sq.setQuery("*:*");
        sq.setSort(SolrSearchProviderImpl.F_ID, SolrQuery.ORDER.asc);
        sq.setStart(nextPageStart);
        sq.setRows(PER_PAGE);

        try {
            thisPage = solrServer.query(sq).getResults();
        } catch (SolrServerException sEx) {
            throw new SolrSearchException(sEx);
        }

        if (thisPage.size() <= 0) {
            nextOffset = NONE_LEFT;
        } else {
            nextPageStart += thisPage.size();
            nextOffset = 0;
        }
    }

    @Override
    public boolean hasNext() {
        return nextOffset != NONE_LEFT;
    }

    @Override
    public CatalogueEntity next() throws SolrSearchException {
        SolrDocument next = thisPage.get(nextOffset);
        nextOffset++;
        if (nextOffset >= thisPage.size()) {
            grabTheNextPage();
        }
        return new SolrCatalogueEntity(next);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}

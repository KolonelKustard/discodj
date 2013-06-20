package com.totalchange.discodj.search.solr;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.solr.client.solrj.SolrServer;

import com.totalchange.discodj.search.SearchPopulator;
import com.totalchange.discodj.search.SearchProvider;
import com.totalchange.discodj.search.SearchQuery;
import com.totalchange.discodj.search.SearchResults;

@Singleton
public final class SolrSearchProviderImpl implements SearchProvider {
    static final String F_ID = "id";
    static final String F_ARTIST = "artist";
    static final String F_ALBUM = "album";
    static final String F_GENRE = "genre";
    static final String F_YEAR = "year";
    static final String F_REQUESTED_BY = "requestedBy";
    static final String F_TITLE = "title";

    private SolrServer solrServer;

    @Inject
    public SolrSearchProviderImpl(SolrServer solrServer) {
        this.solrServer = solrServer;
    }

    @Override
    public SearchPopulator repopulate() throws SolrSearchException {
        return new SolrSearchPopulatorImpl(solrServer);
    }

    @Override
    public SearchResults search(SearchQuery query) {
        // TODO Auto-generated method stub
        return null;
    }
}

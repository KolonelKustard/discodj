package com.totalchange.discodj.search.solr;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;

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

    static final String F_DECADE = "decade";
    static final String F_TEXT = "text";

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
    public SearchResults search(SearchQuery query) throws SolrSearchException {
        SolrQuery sq = new SolrQuery();

        sq.setStart((int) query.getStart());
        sq.setRows((int) query.getRows());

        if (query.getKeywords() != null && !query.getKeywords().isEmpty()) {
            sq.setQuery(F_TEXT + ":" + query.getKeywords() + "*");
        } else {
            sq.setQuery("*:*");
        }

        sq.setFacet(true);
        sq.setFacetMinCount(1);
        sq.setFacetLimit(-1);
        sq.addFacetField(F_ARTIST, F_ALBUM, F_GENRE, F_DECADE);

        for (String id : query.getFacetIds()) {
            sq.addFilterQuery(id);
        }

        // TODO Add exclusions based on playlist

        try {
            QueryResponse res = solrServer.query(sq);
            return new SolrSearchResultsImpl(res);
        } catch (SolrServerException sEx) {
            throw new SolrSearchException(sEx);
        }
    }
}

package com.totalchange.discodj.search.solr;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.FacetParams;

import com.totalchange.discodj.search.SearchFacet;
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

    static final String F_TEXT = "text";

    private static final int DECADE_RANGE_GAP = 10;

    private SolrServer solrServer;

    @Inject
    public SolrSearchProviderImpl(SolrServer solrServer) {
        this.solrServer = solrServer;
    }

    private void addFilterQueryIfHaveFacets(SolrQuery sq, String facetName,
            List<SearchFacet> facets) {
        if (facets != null && !facets.isEmpty()) {
            for (SearchFacet facet : facets) {
                sq.addFilterQuery(facetName + ":" + facet.getId());
            }
        }
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
            sq.setQuery(F_TEXT + ":" + query.getKeywords());
        }

        sq.setFacet(true);
        sq.setFacetMinCount(1);
        sq.addFacetField(F_ARTIST, F_ALBUM, F_GENRE);
        sq.add(FacetParams.FACET_RANGE, F_YEAR);
        sq.add("f." + F_YEAR + "." + FacetParams.FACET_RANGE_START,
                String.valueOf(1900));
        sq.add("f." + F_YEAR + "." + FacetParams.FACET_RANGE_END,
                String.valueOf(3000));
        sq.add("f." + F_YEAR + "." + FacetParams.FACET_RANGE_GAP,
                String.valueOf(DECADE_RANGE_GAP));

        addFilterQueryIfHaveFacets(sq, F_ARTIST, query.getArtistFacets());
        addFilterQueryIfHaveFacets(sq, F_ALBUM, query.getAlbumFacets());
        addFilterQueryIfHaveFacets(sq, F_GENRE, query.getGenreFacets());
        addFilterQueryIfHaveFacets(sq, F_YEAR, query.getDecadeFacets());

        try {
            QueryResponse res = solrServer.query(sq);
            return new SolrSearchResultsImpl(res);
        } catch (SolrServerException sEx) {
            throw new SolrSearchException(sEx);
        }
    }
}

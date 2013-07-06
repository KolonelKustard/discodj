package com.totalchange.discodj.search.solr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import com.totalchange.discodj.media.Media;
import com.totalchange.discodj.search.SearchFacet;
import com.totalchange.discodj.search.SearchResults;

class SolrSearchResultsImpl implements SearchResults {
    private long numFound;
    private List<SearchFacet> artistFacets;
    private List<SearchFacet> albumFacets;
    private List<SearchFacet> genreFacets;
    private List<SearchFacet> decadeFacets;
    private List<Media> results;

    SolrSearchResultsImpl(QueryResponse queryResponse) {
        init(queryResponse);
    }

    private List<SearchFacet> convertFacets(FacetField facetField) {
        if (facetField == null) {
            return Collections.emptyList();
        }

        List<SearchFacet> facets = new ArrayList<>(facetField.getValueCount());
        for (Count count : facetField.getValues()) {
            facets.add(new SolrSearchFacetImpl(count));
        }
        return facets;
    }

    private void init(QueryResponse res) {
        artistFacets = convertFacets(res
                .getFacetField(SolrSearchProviderImpl.F_ARTIST));
        albumFacets = convertFacets(res
                .getFacetField(SolrSearchProviderImpl.F_ALBUM));
        genreFacets = convertFacets(res
                .getFacetField(SolrSearchProviderImpl.F_GENRE));
        decadeFacets = convertFacets(res
                .getFacetField(SolrSearchProviderImpl.F_DECADE));
        
        SolrDocumentList docs = res.getResults();
        numFound = docs.getNumFound();

        results = new ArrayList<>(docs.size());
        for (SolrDocument doc : docs) {
            results.add(new SolrMediaImpl(doc));
        }
    }

    @Override
    public long getNumFound() {
        return numFound;
    }

    @Override
    public List<SearchFacet> getArtistFacets() {
        return artistFacets;
    }

    @Override
    public List<SearchFacet> getAlbumFacets() {
        return albumFacets;
    }

    @Override
    public List<SearchFacet> getGenreFacets() {
        return genreFacets;
    }

    @Override
    public List<SearchFacet> getDecadeFacets() {
        return decadeFacets;
    }

    @Override
    public List<Media> getResults() {
        return results;
    }
}

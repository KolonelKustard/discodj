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

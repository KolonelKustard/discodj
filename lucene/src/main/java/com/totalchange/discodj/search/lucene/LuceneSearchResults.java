package com.totalchange.discodj.search.lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.totalchange.discodj.server.media.Media;
import com.totalchange.discodj.server.search.SearchFacet;
import com.totalchange.discodj.server.search.SearchResults;
import org.apache.lucene.document.Document;
import org.apache.lucene.facet.FacetResult;
import org.apache.lucene.facet.Facets;
import org.apache.lucene.facet.LabelAndValue;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;

class LuceneSearchResults implements SearchResults {
    private static final int MAX_FACETS = 1000;

    private final long numFound;
    private final List<SearchFacet> artistFacets;
    private final List<SearchFacet> albumFacets;
    private final List<SearchFacet> genreFacets;
    private final List<SearchFacet> decadeFacets;
    private final List<Media> results;

    LuceneSearchResults() {
        numFound = 0;
        artistFacets = Collections.emptyList();
        albumFacets = Collections.emptyList();
        genreFacets = Collections.emptyList();
        decadeFacets = Collections.emptyList();
        results = Collections.emptyList();
    }

    LuceneSearchResults(IndexSearcher searcher, TopDocs docs, long start,
            long rows, Facets facets) throws IOException {
        numFound = docs.totalHits;
        artistFacets = makeFacets(facets.getTopChildren(MAX_FACETS,
                LuceneSearchProvider.F_FACET_ARTIST));
        albumFacets = makeFacets(facets.getTopChildren(MAX_FACETS,
                LuceneSearchProvider.F_FACET_ALBUM));
        genreFacets = makeFacets(facets.getTopChildren(MAX_FACETS,
                LuceneSearchProvider.F_FACET_GENRE));
        decadeFacets = makeFacets(facets.getTopChildren(MAX_FACETS,
                LuceneSearchProvider.F_FACET_DECADE));
        results = makeResults(searcher, docs, start, rows);
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

    private List<SearchFacet> makeFacets(FacetResult facetResult) {
        if (facetResult != null) {
            List<SearchFacet> facets = new ArrayList<>(
                    facetResult.labelValues.length);
            for (LabelAndValue facet : facetResult.labelValues) {
                facets.add(new LuceneSearchFacet(facetResult.dim, facet));
            }
            return facets;
        } else {
            return Collections.emptyList();
        }
    }

    private List<Media> makeResults(IndexSearcher searcher, TopDocs docs,
            long start, long rows) throws IOException {
        long actualEnd = Math.min(start + rows, docs.scoreDocs.length);
        List<Media> media = new ArrayList<>((int) rows);
        for (long num = start; num < actualEnd; num++) {
            Document doc = searcher.doc(docs.scoreDocs[(int) num].doc);
            media.add(new LuceneSearchMedia(doc));
        }
        return media;
    }
}

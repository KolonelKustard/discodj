package com.totalchange.discodj.search.lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.facet.FacetResult;
import org.apache.lucene.facet.Facets;
import org.apache.lucene.facet.LabelAndValue;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import com.totalchange.discodj.media.Media;
import com.totalchange.discodj.search.SearchFacet;
import com.totalchange.discodj.search.SearchResults;

class LuceneSearchResults implements SearchResults {
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

    LuceneSearchResults(IndexSearcher searcher, TopDocs docs, Facets facets)
            throws IOException {
        numFound = docs.totalHits;
        artistFacets = makeFacets(facets.getTopChildren(10,
                LuceneSearchProvider.F_ARTIST));
        albumFacets = makeFacets(facets.getTopChildren(10,
                LuceneSearchProvider.F_ALBUM));
        genreFacets = makeFacets(facets.getTopChildren(10,
                LuceneSearchProvider.F_GENRE));
        decadeFacets = makeFacets(facets.getTopChildren(10,
                LuceneSearchProvider.F_DECADE));
        results = makeResults(searcher, docs);
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

    private List<Media> makeResults(IndexSearcher searcher, TopDocs docs)
            throws IOException {
        List<Media> media = new ArrayList<>(docs.scoreDocs.length);
        for (ScoreDoc scoreDoc : docs.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            media.add(new LuceneSearchMedia(doc));
        }
        return media;
    }
}

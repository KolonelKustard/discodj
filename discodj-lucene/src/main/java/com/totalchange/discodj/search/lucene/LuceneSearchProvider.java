package com.totalchange.discodj.search.lucene;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.facet.DrillDownQuery;
import org.apache.lucene.facet.Facets;
import org.apache.lucene.facet.FacetsCollector;
import org.apache.lucene.facet.FacetsConfig;
import org.apache.lucene.facet.sortedset.DefaultSortedSetDocValuesReaderState;
import org.apache.lucene.facet.sortedset.SortedSetDocValuesFacetCounts;
import org.apache.lucene.facet.sortedset.SortedSetDocValuesReaderState;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexNotFoundException;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.totalchange.discodj.catalogue.Catalogue.CatalogueEntity;
import com.totalchange.discodj.search.SearchException;
import com.totalchange.discodj.search.SearchPopulator;
import com.totalchange.discodj.search.SearchProvider;
import com.totalchange.discodj.search.SearchQuery;
import com.totalchange.discodj.search.SearchResults;

public class LuceneSearchProvider implements SearchProvider {
    private static final Logger logger = LoggerFactory
            .getLogger(LuceneSearchProvider.class);

    static final String F_ID = "id";
    static final String F_ID_FOR_SORTING = "idForSorting";
    static final String F_LAST_MODIFIED = "lastModified";
    static final String F_ARTIST = "artist";
    static final String F_ALBUM = "album";
    static final String F_GENRE = "genre";
    static final String F_YEAR = "year";
    static final String F_REQUESTED_BY = "requestedBy";
    static final String F_TITLE = "title";

    static final String F_FACET_ARTIST = "artistFacet";
    static final String F_FACET_ALBUM = "albumFacet";
    static final String F_FACET_GENRE = "genreFacet";
    static final String F_FACET_DECADE = "decadeFacet";

    static final String F_TEXT = "text";

    private Directory directory;

    @Inject
    public LuceneSearchProvider(Directory directory) {
        this.directory = directory;
    }

    @Override
    public Iterator<CatalogueEntity> listAllAlphabeticallyById()
            throws SearchException {
        return new LuceneCatalogueEntityIterator(directory);
    }

    @Override
    public SearchPopulator createPopulator() throws SearchException {
        logger.trace("Creating new Lucene populator for directory {}",
                directory);
        return new LuceneSearchPopulator(directory);
    }

    @Override
    public SearchResults search(SearchQuery query) throws SearchException {
        try {
            DirectoryReader reader = DirectoryReader.open(directory);
            if (query.getRows() > 0 && reader.numDocs() > 0) {
                return doSearch(query, reader);
            } else {
                return new LuceneSearchResults();
            }
        } catch (IndexNotFoundException ex) {
            return new LuceneSearchResults();
        } catch (ParseException ex) {
            throw new SearchException(ex);
        } catch (IOException ex) {
            throw new SearchException(ex);
        }
    }

    private SearchResults doSearch(SearchQuery query, DirectoryReader reader)
            throws IOException, ParseException {
        try {
            IndexSearcher searcher = new IndexSearcher(reader);
            SortedSetDocValuesReaderState state = new DefaultSortedSetDocValuesReaderState(
                    reader);
            FacetsCollector fc = new FacetsCollector();
            TopDocs docs = FacetsCollector.search(searcher, makeQuery(query),
                    reader.numDocs(), fc);
            Facets facets = new SortedSetDocValuesFacetCounts(state, fc);

            return new LuceneSearchResults(searcher, docs, query.getStart(),
                    query.getRows(), facets);
        } finally {
            reader.close();
        }
    }

    private Query makeQuery(SearchQuery query) throws ParseException {
        if (query.getFacetIds() == null || query.getFacetIds().isEmpty()) {
            return makeBaseQuery(query);
        } else {
            return makeFacetQuery(query.getFacetIds(), makeBaseQuery(query));
        }
    }

    private Query makeBaseQuery(SearchQuery query) throws ParseException {
        if (query.getKeywords() != null
                && query.getKeywords().trim().length() > 0) {
            StandardAnalyzer analyser = new StandardAnalyzer();
            QueryParser parser = new QueryParser(LuceneSearchProvider.F_TEXT,
                    analyser);
            return parser.parse(query.getKeywords().trim());
        } else {
            return new MatchAllDocsQuery();
        }
    }

    private Query makeFacetQuery(List<String> facetIds, Query baseQuery) {
        DrillDownQuery query = new DrillDownQuery(new FacetsConfig(), baseQuery);
        for (String facetId : facetIds) {
            int firstColon = facetId.indexOf(':');
            String field = facetId.substring(0, firstColon);
            String value = facetId.substring(firstColon + 1);
            query.add(field, value);
        }

        return query;
    }
}

package com.totalchange.discodj.search.lucene;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.totalchange.discodj.server.media.Media;
import com.totalchange.discodj.server.media.MediaEntity;
import com.totalchange.discodj.server.search.SearchPopulator;
import com.totalchange.discodj.server.search.SearchProvider;
import com.totalchange.discodj.server.search.SearchQuery;
import com.totalchange.discodj.server.search.SearchResults;
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
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LuceneSearchProvider implements SearchProvider {
    private static final Logger logger = LoggerFactory.getLogger(LuceneSearchProvider.class);

    static final String F_ID = "id";
    static final String F_SOURCE_ID = "sourceId";
    static final String F_URI = "uri";
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

    private final ExecutorService executor = Executors.newSingleThreadExecutor(new NamedThreadFactory("lucene"));
    private final Directory directory;

    public LuceneSearchProvider(final Path searchIndexPath) throws IOException {
        this.directory = new NIOFSDirectory(searchIndexPath);
    }

    @Override
    public CompletableFuture<List<MediaEntity>> getAllMediaEntities(String mediaSourceId) {
        return LuceneAllMediaForSourceLister.getAllMediaEntities(executor, directory, mediaSourceId);
    }

    @Override
    public SearchPopulator createPopulator() {
        logger.trace("Creating new Lucene populator for directory {}",
                directory);
        return new LuceneSearchPopulator(directory);
    }

    @Override
    public SearchResults search(SearchQuery query) {
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
            throw new LuceneSearchException(ex);
        } catch (IOException ex) {
            throw new LuceneSearchException(ex);
        }
    }

    @Override
    public Optional<Media> getMediaById(final String id) {
        try (DirectoryReader reader = DirectoryReader.open(directory)) {
            final IndexSearcher searcher = new IndexSearcher(reader);
            final TermQuery query = new TermQuery(new Term(F_ID, id));
            final TopDocs docs = searcher.search(query, 1);
            if (docs.scoreDocs.length > 0) {
                return Optional.of(new LuceneSearchMedia(searcher.doc(docs.scoreDocs[0].doc)));
            } else {
                return Optional.empty();
            }
        } catch (IOException ex) {
            throw new LuceneSearchException(ex);
        }
    }

    @Override
    public void close() {
        logger.info("Shutting down Lucene search provider");
        try {
            logger.debug("Shutting down executor");
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
            logger.debug("Executor stopped cleanly");
        } catch (InterruptedException ex) {
            logger.warn("Some tasks didn't complete", ex);
        }

        try {
            logger.debug("Closing Lucene directory");
            directory.close();
            logger.debug("Directory closed cleanly");
        } catch (IOException ex) {
            logger.warn("Lucene directory didn't close cleanly", ex);
        }
        logger.info("Lucene search provider shut down");
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

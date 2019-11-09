package com.totalchange.discodj.search.lucene;

import com.totalchange.discodj.server.media.MediaEntity;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexNotFoundException;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

final class LuceneAllMediaForSourceLister {
    public static CompletableFuture<List<MediaEntity>> getAllMediaEntities(final Executor executor,
            final Directory directory, final String mediaSourceId) {
        CompletableFuture<List<MediaEntity>> cf = new CompletableFuture<>();
        executor.execute(() -> {
            try {
                cf.complete(getAllMediaEntities(directory, mediaSourceId));
            } catch (IndexNotFoundException ex) {
                cf.complete(Collections.emptyList());
            } catch (Exception ex) {
                cf.completeExceptionally(ex);
            }
        });
        return cf;
    }

    private static List<MediaEntity> getAllMediaEntities(final Directory directory, final String mediaSourceId)
            throws IOException {
        final DirectoryReader reader = DirectoryReader.open(directory);
        final IndexSearcher searcher = new IndexSearcher(reader);
        final TermQuery query = new TermQuery(new Term(LuceneSearchProvider.F_SOURCE_ID, mediaSourceId));
        final TopDocs topDocs = searcher.search(query, Integer.MAX_VALUE);

        final List<MediaEntity> allMedia = new ArrayList<>(topDocs.scoreDocs.length);
        for (final ScoreDoc searchDoc : topDocs.scoreDocs) {
            allMedia.add(new LuceneCatalogueEntity(searcher.doc(searchDoc.doc)));
        }

        return allMedia;
    }
}

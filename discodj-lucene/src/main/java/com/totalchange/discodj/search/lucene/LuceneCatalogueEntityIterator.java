package com.totalchange.discodj.search.lucene;

import java.io.IOException;
import java.util.Iterator;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexNotFoundException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;

import com.totalchange.discodj.catalogue.Catalogue.CatalogueEntity;
import com.totalchange.discodj.search.SearchException;

public class LuceneCatalogueEntityIterator implements Iterator<CatalogueEntity> {
    private static final int PER_PAGE = 100;
    private static final int NONE_LEFT = -1;

    private DirectoryReader reader;
    private IndexSearcher searcher;

    private ScoreDoc nextPageAfter = null;
    private ScoreDoc[] thisPage;
    private int nextOffset;

    public LuceneCatalogueEntityIterator(Directory directory)
            throws SearchException {
        try {
            reader = DirectoryReader.open(directory);
            searcher = new IndexSearcher(reader);
            grabNextPage();
        } catch (IndexNotFoundException ex) {
            nextOffset = NONE_LEFT;
        } catch (IOException ex) {
            throw new SearchException(ex);
        }
    }

    @Override
    public boolean hasNext() {
        return nextOffset != NONE_LEFT;
    }

    @Override
    public CatalogueEntity next() throws SearchException {
        ScoreDoc next = thisPage[nextOffset];
        nextOffset++;
        if (nextOffset >= thisPage.length) {
            grabNextPage();
        }
        try {
            return new LuceneCatalogueEntity(searcher.doc(next.doc));
        } catch (IOException ex) {
            throw new SearchException(ex);
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    private void grabNextPage() throws SearchException {
        Query query = new MatchAllDocsQuery();
        Sort sort = new Sort(new SortField(LuceneSearchProvider.F_ID,
                SortField.Type.STRING));
        try {
            TopDocs docs;
            if (nextPageAfter == null) {
                docs = searcher.search(query, PER_PAGE, sort);
            } else {
                docs = searcher.searchAfter(nextPageAfter, query, PER_PAGE, sort);
            }
            thisPage = docs.scoreDocs;
        } catch (IOException ex) {
            throw new SearchException(ex);
        }

        if (thisPage.length <= 0) {
            nextOffset = NONE_LEFT;
            try {
                reader.close();
            } catch (IOException ex) {
                throw new SearchException(ex);
            }
        } else {
            nextPageAfter = thisPage[thisPage.length - 1];
            nextOffset = 0;
        }
    }
}

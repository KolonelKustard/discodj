package com.totalchange.discodj.search.lucene;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.facet.FacetsConfig;
import org.apache.lucene.facet.sortedset.SortedSetDocValuesFacetField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.totalchange.discodj.media.Media;
import com.totalchange.discodj.search.SearchException;
import com.totalchange.discodj.search.SearchPopulator;

class LuceneSearchPopulator implements SearchPopulator {
    private static final Logger logger = LoggerFactory
            .getLogger(LuceneSearchPopulator.class);

    private final IndexWriter indexWriter;
    private final FacetsConfig config = new FacetsConfig();

    LuceneSearchPopulator(Directory directory) throws SearchException {
        try {
            indexWriter = new IndexWriter(directory,
                    new IndexWriterConfig(new StandardAnalyzer(
                            new CharArraySet(0, true)))
                            .setOpenMode(OpenMode.CREATE_OR_APPEND));
        } catch (IOException ex) {
            throw new SearchException(ex);
        }
    }

    @Override
    public void addMedia(Media media) throws SearchException {
        try {
            Document doc = config.build(makeDoc(media));
            logger.trace("Adding lucene doc {}", doc);
            indexWriter.addDocument(doc);
        } catch (IOException ex) {
            throw new SearchException(ex);
        }
    }

    @Override
    public void updateMedia(Media media) throws SearchException {
        try {
            Document doc = config.build(makeDoc(media));
            logger.trace("Updating lucene doc {}", doc);
            indexWriter.updateDocument(new Term(LuceneSearchProvider.F_ID,
                    media.getId()), doc);
        } catch (IOException ex) {
            throw new SearchException(ex);
        }
    }

    @Override
    public void deleteMedia(String id) throws SearchException {
        try {
            logger.trace("Deleting lucene doc {}", id);
            indexWriter
                    .deleteDocuments(new Term(LuceneSearchProvider.F_ID, id));
        } catch (IOException ex) {
            throw new SearchException(ex);
        }
    }

    @Override
    public void deleteAll() throws SearchException {
        try {
            indexWriter.deleteAll();
        } catch (IOException ex) {
            throw new SearchException(ex);
        }
    }

    @Override
    public void commit() throws SearchException {
        try {
            indexWriter.close();
        } catch (IOException ex) {
            throw new SearchException(ex);
        }
    }

    private Document makeDoc(Media media) {
        Document doc = new Document();
        doc.add(new SortedDocValuesField(LuceneSearchProvider.F_ID_FOR_SORTING,
                new BytesRef(media.getId())));
        doc.add(new StringField(LuceneSearchProvider.F_ID, media.getId(),
                Store.YES));
        doc.add(new LongField(LuceneSearchProvider.F_LAST_MODIFIED, media
                .getLastModified().getTime(), Store.YES));

        doc.add(new StringField(LuceneSearchProvider.F_ARTIST,
                blankNotNull(media.getArtist()), Store.YES));
        doc.add(new StringField(LuceneSearchProvider.F_ALBUM,
                blankNotNull(media.getAlbum()), Store.YES));
        doc.add(new StringField(LuceneSearchProvider.F_GENRE,
                blankNotNull(media.getGenre()), Store.YES));
        doc.add(new IntField(LuceneSearchProvider.F_YEAR, media.getYear(),
                Store.YES));
        doc.add(new StringField(LuceneSearchProvider.F_REQUESTED_BY,
                blankNotNull(media.getRequestedBy()), Store.YES));
        doc.add(new StringField(LuceneSearchProvider.F_TITLE,
                blankNotNull(media.getTitle()), Store.YES));

        addFacet(doc, LuceneSearchProvider.F_FACET_ARTIST, media.getArtist());
        addFacet(doc, LuceneSearchProvider.F_FACET_ALBUM, media.getAlbum());
        addFacet(doc, LuceneSearchProvider.F_FACET_GENRE, media.getGenre());
        addFacet(doc, LuceneSearchProvider.F_FACET_DECADE,
                floorYearToDecade(media.getYear()));

        doc.add(new TextField(LuceneSearchProvider.F_TEXT,
                makeSearchText(media)));

        return doc;
    }

    private String blankNotNull(String str) {
        if (str == null) {
            return "";
        } else {
            return str.trim();
        }
    }

    private String floorYearToDecade(int year) {
        if (year > 0) {
            return String.valueOf((year / 10) * 10);
        } else {
            return null;
        }
    }

    private Reader makeSearchText(Media media) {
        StringBuilder str = new StringBuilder();
        str.append(media.getArtist());
        str.append(' ');
        str.append(media.getAlbum());
        str.append(' ');
        str.append(media.getTitle());
        return new StringReader(str.toString());
    }

    private void addFacet(Document doc, String field, String value) {
        if (value != null && value.trim().length() > 0) {
            doc.add(new SortedSetDocValuesFacetField(field, value));
        }
    }
}

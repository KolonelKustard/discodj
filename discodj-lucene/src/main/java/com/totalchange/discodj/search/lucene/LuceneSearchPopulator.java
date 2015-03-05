package com.totalchange.discodj.search.lucene;

import java.io.IOException;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.facet.FacetsConfig;
import org.apache.lucene.facet.sortedset.SortedSetDocValuesFacetField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;

import com.totalchange.discodj.media.Media;
import com.totalchange.discodj.search.SearchException;
import com.totalchange.discodj.search.SearchPopulator;

class LuceneSearchPopulator implements SearchPopulator {
    private final IndexWriter indexWriter;
    private final FacetsConfig config = new FacetsConfig();

    LuceneSearchPopulator(Directory directory) throws SearchException {
        try {
            indexWriter = new IndexWriter(directory,
                    new IndexWriterConfig(new WhitespaceAnalyzer())
                            .setOpenMode(OpenMode.CREATE_OR_APPEND));
        } catch (IOException ex) {
            throw new SearchException(ex);
        }
    }

    @Override
    public void addMedia(Media media) throws SearchException {
        try {
            indexWriter.addDocument(config.build(makeDoc(media)));
        } catch (IOException ex) {
            throw new SearchException(ex);
        }
    }

    @Override
    public void updateMedia(Media media) throws SearchException {
        try {
            indexWriter.updateDocument(new Term(LuceneSearchProvider.F_ID,
                    media.getId()), config.build(makeDoc(media)));
        } catch (IOException ex) {
            throw new SearchException(ex);
        }
    }

    @Override
    public void deleteMedia(String id) throws SearchException {
        try {
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
        doc.add(new StringField(LuceneSearchProvider.F_ID, media.getId(),
                Store.YES));
        doc.add(new LongField(LuceneSearchProvider.F_LAST_MODIFIED, media
                .getLastModified().getTime(), Store.YES));
        doc.add(new SortedSetDocValuesFacetField(LuceneSearchProvider.F_ARTIST,
                "Bob"));
        doc.add(new SortedSetDocValuesFacetField(LuceneSearchProvider.F_ALBUM,
                media.getAlbum()));
        doc.add(new SortedSetDocValuesFacetField(LuceneSearchProvider.F_GENRE,
                media.getGenre()));
        doc.add(new IntField(LuceneSearchProvider.F_YEAR, media.getYear(),
                Store.YES));
        doc.add(new SortedSetDocValuesFacetField(LuceneSearchProvider.F_DECADE,
                floorYearToDecade(media.getYear())));
        doc.add(new StringField(LuceneSearchProvider.F_REQUESTED_BY, media
                .getRequestedBy(), Store.YES));
        doc.add(new StringField(LuceneSearchProvider.F_TITLE, media.getTitle(),
                Store.YES));

        doc.add(new TextField(LuceneSearchProvider.F_TEXT,
                makeSearchText(media), Store.NO));

        return doc;
    }

    private String floorYearToDecade(int year) {
        return String.valueOf((year / 10) * 10);
    }

    private String makeSearchText(Media media) {
        StringBuilder str = new StringBuilder();
        str.append(media.getArtist());
        str.append(' ');
        str.append(media.getAlbum());
        str.append(' ');
        str.append(media.getTitle());
        return str.toString();
    }
}

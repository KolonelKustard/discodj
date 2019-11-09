package com.totalchange.discodj.search.lucene;

import java.io.File;
import java.net.URI;
import java.util.Date;

import com.totalchange.discodj.server.media.Media;
import org.apache.lucene.document.Document;

class LuceneSearchMedia implements Media {
    private final String id;
    private final String sourceId;
    private final URI uri;
    private final long lastModified;
    private final String artist;
    private final String album;
    private final String genre;
    private final int year;
    private final String requestedBy;
    private final String title;

    LuceneSearchMedia(Document doc) {
        id = doc.get(LuceneSearchProvider.F_ID);
        sourceId = doc.get(LuceneSearchProvider.F_SOURCE_ID);
        uri = URI.create(doc.get(LuceneSearchProvider.F_URI));
        lastModified = doc.getField(LuceneSearchProvider.F_LAST_MODIFIED).numericValue().longValue();
        artist = doc.get(LuceneSearchProvider.F_ARTIST);
        album = doc.get(LuceneSearchProvider.F_ALBUM);
        genre = doc.get(LuceneSearchProvider.F_GENRE);
        year = doc.getField(LuceneSearchProvider.F_YEAR).numericValue().intValue();
        requestedBy = doc.get(LuceneSearchProvider.F_REQUESTED_BY);
        title = doc.get(LuceneSearchProvider.F_TITLE);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getSourceId() {
        return sourceId;
    }

    @Override
    public URI getUri() {
        return uri;
    }

    @Override
    public long getLastModifiedMs() {
        return lastModified;
    }

    @Override
    public String getArtist() {
        return artist;
    }

    @Override
    public String getAlbum() {
        return album;
    }

    @Override
    public String getGenre() {
        return genre;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public String getRequestedBy() {
        return requestedBy;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "LuceneSearchMedia [" + super.toString() + "]";
    }
}

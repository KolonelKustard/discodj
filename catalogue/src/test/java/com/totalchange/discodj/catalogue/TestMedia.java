package com.totalchange.discodj.catalogue;

import com.totalchange.discodj.server.media.Media;

import java.net.URI;
import java.util.Date;
import java.util.Objects;

public class TestMedia implements Media {
    private final int id;

    public TestMedia(final int id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return String.valueOf(id);
    }

    @Override
    public long getLastModifiedMs() {
        return id;
    }

    @Override
    public String getArtist() {
        return "Artist " + id;
    }

    @Override
    public String getAlbum() {
        return "Album " + id;
    }

    @Override
    public String getGenre() {
        return "Genre " + id;
    }

    @Override
    public int getYear() {
        return id;
    }

    @Override
    public String getRequestedBy() {
        return "Requested by " + id;
    }

    @Override
    public String getTitle() {
        return "Title " + id;
    }

    @Override
    public URI getUri() {
        return URI.create("http://test/media/" + id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestMedia testMedia = (TestMedia) o;
        return id == testMedia.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "TestMedia{id=" + id + '}';
    }
}

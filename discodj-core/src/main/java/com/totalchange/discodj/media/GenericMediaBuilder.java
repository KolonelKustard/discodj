package com.totalchange.discodj.media;

import java.util.Date;

public class GenericMediaBuilder {
    private String id;
    private Date lastModified;
    private String artist;
    private String album;
    private String genre;
    private int year;
    private String requestedBy;
    private String title;

    public GenericMediaBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public GenericMediaBuilder withLastModified(Date lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public GenericMediaBuilder withArtist(String artist) {
        this.artist = artist;
        return this;
    }

    public GenericMediaBuilder withAlbum(String album) {
        this.album = album;
        return this;
    }

    public GenericMediaBuilder withGenre(String genre) {
        this.genre = genre;
        return this;
    }

    public GenericMediaBuilder withYear(int year) {
        this.year = year;
        return this;
    }

    public GenericMediaBuilder withRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
        return this;
    }

    public GenericMediaBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public Media build() {
        return new GenericMedia(id, lastModified, artist, album, genre, year,
                requestedBy, title);
    }
}

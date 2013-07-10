package com.totalchange.discodj.catalogue;

import com.totalchange.discodj.media.Media;

final class TestMediaBuilder {
    private String id;
    private String artist;
    private String album;
    private String genre;
    private int year;
    private String requestedBy;
    private String title;

    TestMediaBuilder withId(String id) {
        this.id = id;
        return this;
    }

    TestMediaBuilder withArtist(String artist) {
        this.artist = artist;
        return this;
    }

    TestMediaBuilder withAlbum(String album) {
        this.album = album;
        return this;
    }

    TestMediaBuilder withGenre(String genre) {
        this.genre = genre;
        return this;
    }

    TestMediaBuilder withYear(int year) {
        this.year = year;
        return this;
    }

    TestMediaBuilder withRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
        return this;
    }

    TestMediaBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    Media build() {
        return new Media() {
            @Override
            public String getId() {
                return id;
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
        };
    }
}

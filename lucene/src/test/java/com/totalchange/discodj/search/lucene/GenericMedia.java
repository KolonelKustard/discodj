/**
 * Copyright 2015 Ralph Jones
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.totalchange.discodj.search.lucene;

import com.totalchange.discodj.server.media.Media;

import java.net.URI;

class GenericMedia implements Media {
    private final String id;
    private final String sourceId;
    private final long lastModifiedMs;
    private final String artist;
    private final String album;
    private final String genre;
    private final int year;
    private final String requestedBy;
    private final String title;
    private final URI uri;

    private GenericMedia(String id, String sourceId, long lastModifiedMs, String artist,
            String album, String genre, int year, String requestedBy,
            String title, URI uri) {
        this.id = id;
        this.sourceId = sourceId;
        this.lastModifiedMs = lastModifiedMs;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
        this.year = year;
        this.requestedBy = requestedBy;
        this.title = title;
        this.uri = uri;
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
    public long getLastModifiedMs() {
        return lastModifiedMs;
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
    public URI getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return "GenericMedia [" + super.toString() + "]";
    }

    public static final class Builder {
        private String id;
        private String sourceId;
        private long lastModifiedMs;
        private String artist;
        private String album;
        private String genre;
        private int year;
        private String requestedBy;
        private String title;
        private URI uri;

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withSourceId(String sourceId) {
            this.sourceId = sourceId;
            return this;
        }

        public Builder withLastModifiedMs(long lastModifiedMs) {
            this.lastModifiedMs = lastModifiedMs;
            return this;
        }

        public Builder withArtist(String artist) {
            this.artist = artist;
            return this;
        }

        public Builder withAlbum(String album) {
            this.album = album;
            return this;
        }

        public Builder withGenre(String genre) {
            this.genre = genre;
            return this;
        }

        public Builder withYear(int year) {
            this.year = year;
            return this;
        }

        public Builder withRequestedBy(String requestedBy) {
            this.requestedBy = requestedBy;
            return this;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withUri(URI uri) {
            this.uri = uri;
            return this;
        }

        public Media build() {
            return new GenericMedia(id, sourceId, lastModifiedMs, artist, album, genre,
                    year, requestedBy, title, uri);
        }
    }
}

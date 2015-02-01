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
package com.totalchange.discodj.media;

import java.io.File;
import java.util.Date;

public class GenericMediaBuilder {
    private String id;
    private File file;
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

    public GenericMediaBuilder withFile(File file) {
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
        return new GenericMedia(id, file, lastModified, artist, album, genre,
                year, requestedBy, title);
    }
}

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

class GenericMedia extends AbstractMedia {
    private String id;
    private File file;
    private Date lastModified;
    private String artist;
    private String album;
    private String genre;
    private int year;
    private String requestedBy;
    private String title;

    GenericMedia(String id, File file, Date lastModified, String artist,
            String album, String genre, int year, String requestedBy,
            String title) {
        super();
        this.id = id;
        this.file = file;
        this.lastModified = lastModified;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
        this.year = year;
        this.requestedBy = requestedBy;
        this.title = title;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Date getLastModified() {
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
    public File getFile() {
        return file;
    }

    @Override
    public String toString() {
        return "GenericMedia [" + super.toString() + "]";
    }
}

package com.totalchange.discodj.xuggler;

import java.util.Date;

import com.totalchange.discodj.media.AbstractMedia;
import com.xuggle.xuggler.IMetaData;

final class XugglerMediaImpl extends AbstractMedia {
    private String id;
    private Date lastModified;
    private String artist;
    private String album;
    private String genre;
    private int year;
    private String requestedBy;
    private String title;

    XugglerMediaImpl(String id, long lastModified, IMetaData md) {
        this.id = id;
        this.lastModified = new Date(lastModified);

        artist = md.getValue("artist");
        album = md.getValue("album");
        genre = md.getValue("genre");
        try {
            year = Integer.parseInt(md.getValue("year"));
        } catch (NumberFormatException nfEx) {
            year = -1;
        }
        requestedBy = md.getValue("comment");
        title = md.getValue("title");
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
    public String toString() {
        return "XugglerMediaImpl [" + super.toString() + "]";
    }
}

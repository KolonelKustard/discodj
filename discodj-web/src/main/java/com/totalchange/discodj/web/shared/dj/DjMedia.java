package com.totalchange.discodj.web.shared.dj;

import java.io.Serializable;

public class DjMedia implements Serializable {
    private static final long serialVersionUID = 8853328652712059275L;

    private String id;
    private String artist;
    private String title;
    private int whenCanBePlayedAgain;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getWhenCanBePlayedAgain() {
        return whenCanBePlayedAgain;
    }

    public void setWhenCanBePlayedAgain(int whenCanBePlayedAgain) {
        this.whenCanBePlayedAgain = whenCanBePlayedAgain;
    }
}

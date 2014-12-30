package com.totalchange.discodj.ws.playlist;

public class PlaylistNext {
    public enum MediaType {
        Video, Audio
    }

    private boolean queueEmpty;
    private MediaType type;
    private String artist;
    private String title;
    private String requestedBy;

    public boolean isQueueEmpty() {
        return queueEmpty;
    }

    public void setQueueEmpty(boolean queueEmpty) {
        this.queueEmpty = queueEmpty;
    }

    public MediaType getType() {
        return type;
    }

    public void setType(MediaType type) {
        this.type = type;
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

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }
}

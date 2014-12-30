package com.totalchange.discodj.ws.playlist;

import java.util.List;

import com.totalchange.discodj.ws.DjMedia;

public class PlaylistResult {
    private DjMedia nowPlaying;
    private List<DjMedia> playlist;

    public DjMedia getNowPlaying() {
        return nowPlaying;
    }

    public void setNowPlaying(DjMedia nowPlaying) {
        this.nowPlaying = nowPlaying;
    }

    public List<DjMedia> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(List<DjMedia> playlist) {
        this.playlist = playlist;
    }
}

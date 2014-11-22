package com.totalchange.discodj.web.shared.dj;

import java.util.List;

public class StatusResult {
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

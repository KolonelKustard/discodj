package com.totalchange.discodj.server.playlist;

import com.totalchange.discodj.server.media.Media;

public interface Playlist {
    void add(Media mediaToAdd);
    Media pop();
    Media getNowPlaying();
}

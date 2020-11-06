package com.totalchange.discodj.server.playlist;

import com.totalchange.discodj.server.media.Media;

import java.util.List;
import java.util.Optional;

public interface Playlist {
    void add(Media mediaToAdd);
    Optional<Media> pop();
    Optional<Media> getNowPlaying();
    List<Media> getPlaylist();
}

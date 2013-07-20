package com.totalchange.discodj.queue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Singleton;

@Singleton
public class PlaylistQueue {
    private static final int PLAYED_LIST_SIZE = 20;

    private List<String> playlist = Collections
            .synchronizedList(new ArrayList<String>());
    private List<String> playedList = Collections
            .synchronizedList(new ArrayList<String>(PLAYED_LIST_SIZE));

    public synchronized String popNextMediaId() {
        if (playlist.isEmpty()) {
            return null;
        }

        String id = playlist.remove(0);
        if (playedList.size() >= PLAYED_LIST_SIZE) {
            playedList.remove(0);
        }
        playedList.add(id);
        return id;
    }

    public List<String> getMediaIdsToExclude() {
        List<String> mediaIds = new ArrayList<>(playlist.size()
                + playedList.size());
        mediaIds.addAll(playlist);
        mediaIds.addAll(playedList);
        return mediaIds;
    }

    public List<String> getPlaylistMediaIds() {
        return playlist;
    }
}

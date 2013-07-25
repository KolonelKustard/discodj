package com.totalchange.discodj.queue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.totalchange.discodj.catalogue.Catalogue;
import com.totalchange.discodj.media.Media;

@Singleton
public class PlaylistQueue {
    private Queue<String> defaultQueue = new LinkedList<>();
    private PlaylistIdQueue queue = new PlaylistIdQueue();
    private Catalogue catalogue;
    private Media lastPopped = null;

    @Inject
    public PlaylistQueue(Catalogue catalogue) {
        this.catalogue = catalogue;

        List<Media> defaultMediaQueue = catalogue.getDefaultPlaylist();
        if (defaultMediaQueue != null) {
            for (Media media : defaultMediaQueue) {
                defaultQueue.offer(media.getId());
            }
        }
    }

    private Media fetchMedia(String id) {
        // TODO Add caching of media
        return catalogue.getMedia(id);
    }

    public Media pop() {
        // TODO Error handling in case resources aren't available
        String nextMediaId = queue.popNextMediaId();
        if (nextMediaId != null) {
            lastPopped = fetchMedia(nextMediaId);
            return lastPopped;
        }

        nextMediaId = defaultQueue.poll();
        if (nextMediaId != null) {
            lastPopped = fetchMedia(nextMediaId);
            return lastPopped;
        }

        return null;
    }

    public Media getLastPopped() {
        return lastPopped;
    }

    public List<Media> getPlaylist() {
        List<String> ids = queue.getCopiedPlaylist();
        List<Media> media = new ArrayList<>(ids.size());
        for (String id : ids) {
            media.add(fetchMedia(id));
        }
        return media;
    }

    public void setPlaylist(List<String> playlist) {
        queue.setPlaylist(playlist);
    }

    public List<String> getMediaIdsToExclude() {
        return queue.getMediaIdsToExclude();
    }
}

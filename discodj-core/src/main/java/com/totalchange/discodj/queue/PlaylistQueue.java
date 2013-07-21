package com.totalchange.discodj.queue;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.totalchange.discodj.catalogue.Catalogue;
import com.totalchange.discodj.media.Media;

@Singleton
public class PlaylistQueue {
    private PlaylistIdQueue queue = new PlaylistIdQueue();
    private Catalogue catalogue;

    @Inject
    public PlaylistQueue(Catalogue catalogue) {
        this.catalogue = catalogue;
    }

    private Media fetchMedia(String id) {
        // TODO Add caching of media
        return catalogue.getMedia(id);
    }

    public Media pop() {
        return fetchMedia(queue.popNextMediaId());
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

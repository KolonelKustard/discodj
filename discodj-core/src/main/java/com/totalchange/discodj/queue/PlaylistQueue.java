package com.totalchange.discodj.queue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.totalchange.discodj.catalogue.Catalogue;
import com.totalchange.discodj.media.Media;

@Singleton
public class PlaylistQueue {
    private static final Logger logger = LoggerFactory
            .getLogger(PlaylistQueue.class);

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
        logger.trace("Popping next item off the queue");

        // TODO Error handling in case resources aren't available
        String nextMediaId = queue.popNextMediaId();
        if (nextMediaId != null) {
            lastPopped = fetchMedia(nextMediaId);
            logger.trace("Returning media from manual queue {}", lastPopped);
            return lastPopped;
        }

        nextMediaId = defaultQueue.poll();
        if (nextMediaId != null) {
            lastPopped = fetchMedia(nextMediaId);
            logger.trace("Returning media from default queue {}", lastPopped);
            return lastPopped;
        }

        logger.trace("Queue empty");
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
        logger.trace("Setting playlist to {}", playlist);
        queue.setPlaylist(playlist);
    }

    public List<String> getMediaIdsToExclude() {
        return queue.getMediaIdsToExclude();
    }
}

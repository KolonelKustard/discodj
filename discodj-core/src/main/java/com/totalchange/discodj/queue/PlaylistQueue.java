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
    private static final int POPPED_LIST_SIZE = 20;

    private static final Logger logger = LoggerFactory
            .getLogger(PlaylistQueue.class);

    private final Queue<Media> defaultQueue;
    private final List<Media> requestedQueue = new ArrayList<>();
    private final List<String> poppedIdList = new ArrayList<>(POPPED_LIST_SIZE);

    private Catalogue catalogue;
    private Media lastPopped = null;

    @Inject
    public PlaylistQueue(Catalogue catalogue) {
        logger.trace("Creating new queue");
        this.catalogue = catalogue;

        logger.trace("Populating default playlist from catalogue");
        defaultQueue = new LinkedList<>(catalogue.getDefaultPlaylist());
        logger.trace("Done");
    }

    private Media fetchMedia(String id) {
        // TODO Add caching of media
        return catalogue.getMedia(id);
    }

    private void addPoppedId(String id) {
        if (poppedIdList.size() >= POPPED_LIST_SIZE) {
            poppedIdList.remove(0);
        }
        poppedIdList.add(id);
    }

    private boolean skipIfInPoppedList(Media media) {
        return poppedIdList.contains(media.getId());
    }

    public synchronized Media pop() {
        logger.trace("Popping next item off the queue - trying requested "
                + "queue first");

        if (requestedQueue.size() > 0) {
            Media nextMedia = requestedQueue.remove(0);
            if (nextMedia != null) {
                if (skipIfInPoppedList(nextMedia)) {
                    logger.trace("Skipping already played media {}", nextMedia);
                    return pop();
                } else {
                    lastPopped = nextMedia;
                    addPoppedId(lastPopped.getId());
                    logger.trace("Returning media from requested queue {}",
                            lastPopped);
                    return lastPopped;
                }
            }
        }

        logger.trace("Nothing in requested queue, trying default queue");
        Media nextMedia = defaultQueue.poll();
        if (nextMedia != null) {
            if (skipIfInPoppedList(nextMedia)) {
                logger.trace("Skipping already played media {}", nextMedia);
                return pop();
            } else {
                lastPopped = nextMedia;
                addPoppedId(lastPopped.getId());
                logger.trace("Returning media from default queue {}",
                        lastPopped);
                return lastPopped;
            }
        }

        lastPopped = null;
        logger.trace("Nothing left in the queue - returning null");
        return lastPopped;
    }

    public Media getLastPopped() {
        return lastPopped;
    }

    public synchronized List<Media> getPlaylist() {
        return new ArrayList<>(requestedQueue);
    }

    public synchronized void push(String id) {
        logger.trace("Adding {} to playlist", id);

        try {
            Media media = fetchMedia(id);
            if (!skipIfInPoppedList(media) && !requestedQueue.contains(media)) {
                requestedQueue.add(media);
            } else {
                logger.trace("Skipping adding {} as is in already in playlist "
                        + "or has already been played", media);
            }
        } catch (Exception ex) {
            logger.warn("Failed to add id " + id
                    + " with fetch from catalogue error " + ex.getMessage(),
                    ex);
        }
    }

    public synchronized int getWhenCanBePlayedAgain(Media media) {
        logger.trace("Working out when {} can be played", media);

        int pos = poppedIdList.indexOf(media.getId());
        if (pos > -1) {
            int songsTime = pos + 1;
            logger.trace("Based on what's popped it can be played in {} "
                    + "songs time", songsTime);
            return songsTime;
        }

        pos = requestedQueue.indexOf(media);
        if (pos > -1) {
            int songsTime = pos + 1;
            logger.trace("Based on requested queue it can be played in {} "
                    + "songs time", songsTime);
            return songsTime;
        }

        logger.trace("Not found in any list so can be played now");
        return -1;
    }
}

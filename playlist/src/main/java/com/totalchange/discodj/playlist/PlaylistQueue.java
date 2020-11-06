/**
 * Copyright 2015 Ralph Jones
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.totalchange.discodj.playlist;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.totalchange.discodj.server.media.Media;
import com.totalchange.discodj.server.playlist.Playlist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlaylistQueue implements Playlist {
    private static final int POPPED_LIST_SIZE = 20;

    private static final Logger logger = LoggerFactory
            .getLogger(PlaylistQueue.class);

    private final List<Media> requestedQueue = new ArrayList<>();
    private final List<String> poppedIdList = new ArrayList<>(POPPED_LIST_SIZE);

    private Optional<Media> nowPlaying = Optional.empty();

    private void addPoppedId(String id) {
        if (poppedIdList.size() >= POPPED_LIST_SIZE) {
            poppedIdList.remove(0);
        }
        poppedIdList.add(id);
    }

    private boolean skipIfInPoppedList(Media media) {
        return poppedIdList.contains(media.getId());
    }

    @Override
    public synchronized void add(Media media) {
        logger.trace("Adding {} to playlist", media);

        if (!skipIfInPoppedList(media) && !requestedQueue.contains(media)) {
            requestedQueue.add(media);
        } else {
            logger.trace("Skipping adding {} as is in already in playlist "
                    + "or has already been played", media);
        }
    }

    @Override
    public synchronized Optional<Media> pop() {
        logger.trace("Popping next item off the queue - trying requested "
                + "queue first");

        if (requestedQueue.size() > 0) {
            Media nextMedia = requestedQueue.remove(0);
            if (nextMedia != null) {
                if (skipIfInPoppedList(nextMedia)) {
                    logger.trace("Skipping already played media {}", nextMedia);
                    return pop();
                } else {
                    nowPlaying = Optional.of(nextMedia);
                    addPoppedId(nextMedia.getId());
                    logger.trace("Returning media from requested queue {}",
                            nowPlaying);
                    return nowPlaying;
                }
            }
        }

        nowPlaying = Optional.empty();
        logger.trace("Nothing left in the queue - returning empty");
        return nowPlaying;
    }

    @Override
    public Optional<Media> getNowPlaying() {
        return nowPlaying;
    }

    @Override
    public synchronized List<Media> getPlaylist() {
        return new ArrayList<>(requestedQueue);
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

    public synchronized void moveUp(String id) {
        for (int num = 1; num < requestedQueue.size(); num++) {
            Media media = requestedQueue.get(num);
            if (media.getId().equals(id)) {
                Media swapsy = requestedQueue.get(num - 1);
                requestedQueue.set(num - 1, media);
                requestedQueue.set(num, swapsy);
                break;
            }
        }
    }

    public synchronized void moveDown(String id) {
        for (int num = 0; num < requestedQueue.size() - 1; num++) {
            Media media = requestedQueue.get(num);
            if (media.getId().equals(id)) {
                Media swapsy = requestedQueue.get(num + 1);
                requestedQueue.set(num + 1, media);
                requestedQueue.set(num, swapsy);
                break;
            }
        }
    }
}

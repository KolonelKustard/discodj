package com.totalchange.discodj.catalogue.sync;

import com.totalchange.discodj.server.media.MediaEntity;

import java.util.Iterator;

class MediaEntityIteratorComparingThing {
    private static final MediaEntityComparator comparator = new MediaEntityComparator();

    private final Iterator<MediaEntity> sourceIterator;
    private final Iterator<MediaEntity> destinationIterator;
    private final MediaEntitySyncHandler handler;

    private MediaEntity currentSourceItem = null;
    private MediaEntity currentDestinationItem = null;

    MediaEntityIteratorComparingThing(Iterator<MediaEntity> sourceIterator,
            Iterator<MediaEntity> destinationIterator, MediaEntitySyncHandler handler) {
        this.sourceIterator = sourceIterator;
        this.destinationIterator = destinationIterator;
        this.handler = handler;
    }

    void syncThingsUp() {
        resetState();

        while (fetchTheNextMediaItemsAsLongAsTheresStillStuffToProcess()) {
            if (destinationHasEndedButStillSourceItemsLeftToAdd()) {
                handler.add(currentSourceItem.getId());
                currentSourceItem = null;
            } else if (sourceHasEndedButStillDestinationItemsLeftToDelete()) {
                handler.delete(currentDestinationItem.getId());
                currentDestinationItem = null;
            } else {
                compareTheCurrentTwoMediaItems();
            }
        }
    }

    private void resetState() {
        currentSourceItem = null;
        currentDestinationItem = null;
    }

    private boolean fetchTheNextMediaItemsAsLongAsTheresStillStuffToProcess() {
        final boolean srcHasNext = sourceIterator.hasNext();
        final boolean dstHasNext = destinationIterator.hasNext();

        if (srcHasNext || dstHasNext) {
            if (srcHasNext && currentSourceItem == null) {
                currentSourceItem = sourceIterator.next();
            }

            if (dstHasNext && currentDestinationItem == null) {
                currentDestinationItem = destinationIterator.next();
            }

            return true;
        } else {
            return false;
        }
    }

    private boolean destinationHasEndedButStillSourceItemsLeftToAdd() {
        return currentDestinationItem == null && currentSourceItem != null;
    }

    private boolean sourceHasEndedButStillDestinationItemsLeftToDelete() {
        return currentSourceItem == null && currentDestinationItem != null;
    }

    private void compareTheCurrentTwoMediaItems() {
        final int compared = comparator.compare(currentSourceItem, currentDestinationItem);
        if (compared == 0) {
            if (currentSourceItem.getLastModified() != currentDestinationItem.getLastModified()) {
                handler.update(currentSourceItem.getId());
            }
            currentSourceItem = null;
            currentDestinationItem = null;
        } else if (compared < 0) {
            handler.add(currentSourceItem.getId());
            currentSourceItem = null;
        } else if (compared > 0) {
            handler.delete(currentDestinationItem.getId());
            currentDestinationItem = null;
        }
    }
}

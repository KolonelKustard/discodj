package com.totalchange.discodj.catalogue.sync;

import com.totalchange.discodj.server.media.MediaEntity;

import java.util.Iterator;
import java.util.List;

public final class MediaEntitySync {
    private static final MediaEntityComparator comparator = new MediaEntityComparator();

    private MediaEntitySync() {
        // Private constructor for utility class
    }

    public static void sync(final List<MediaEntity> source, final List<MediaEntity> destination,
            final MediaEntitySyncHandler handler) {
        source.sort(comparator);
        destination.sort(comparator);

        final MediaEntityIteratorComparingThing comparingThing = new MediaEntityIteratorComparingThing(
                source.iterator(), destination.iterator(), handler);
        comparingThing.syncThingsUp();
    }
}

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
        sync(source.iterator(), destination.iterator(), handler);
    }

    private static void sync(final Iterator<MediaEntity> src, final Iterator<MediaEntity> dest,
            final MediaEntitySyncHandler handler) {
        do {
            if (src.hasNext() && dest.hasNext()) {
                MediaEntity srcEntity = src.next();
                MediaEntity destEntity = dest.next();

                int compared = comparator.compare(srcEntity, destEntity);
                if (compared == 0 && srcEntity.getLastModified() != destEntity.getLastModified()) {
                    handler.update(destEntity.getId());
                } else if (compared < 0) {
                    while (compared < 0) {
                        handler.add(srcEntity.getId());
                        srcEntity = src.next();
                        compared = comparator.compare(srcEntity, destEntity);
                    }
                } else if (compared > 0) {
                    while (compared > 0) {
                        handler.delete(destEntity.getId());
                        destEntity = dest.next();
                        compared = comparator.compare(srcEntity, destEntity);
                    }
                }
            } else if (src.hasNext() && !dest.hasNext()) {
                handler.add(src.next().getId());
            } else if (!src.hasNext() && dest.hasNext()) {
                handler.delete(dest.next().getId());
            }
        } while (src.hasNext() || dest.hasNext());
    }
}

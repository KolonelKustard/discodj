package com.totalchange.discodj.catalogue.sync;

import com.totalchange.discodj.server.media.MediaEntity;

import java.util.Comparator;

class MediaEntityComparator implements Comparator<MediaEntity> {
    @Override
    public int compare(MediaEntity o1, MediaEntity o2) {
        return o1.getId().compareTo(o2.getId());
    }
}

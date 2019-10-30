package com.totalchange.discodj.catalogue;

import com.totalchange.discodj.server.media.MediaEntity;

public class TestMediaEntity implements MediaEntity {
    private final int id;
    private final long lastModified;

    public TestMediaEntity(final int id, final long lastModified) {
        this.id = id;
        this.lastModified = lastModified;
    }

    @Override
    public String getId() {
        return String.valueOf(id);
    }

    @Override
    public long getLastModified() {
        return lastModified;
    }

    @Override
    public String toString() {
        return "TestMediaEntity{id=" + id + ", lastModified=" + lastModified + '}';
    }
}

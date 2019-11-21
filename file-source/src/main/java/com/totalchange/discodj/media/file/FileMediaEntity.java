package com.totalchange.discodj.media.file;

import com.totalchange.discodj.server.media.MediaEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class FileMediaEntity implements MediaEntity {
    private final String id;
    private final long lastModifiedMs;

    FileMediaEntity(final Path path) throws IOException {
        this.id = path.toString();
        this.lastModifiedMs = Files.getLastModifiedTime(path).toMillis();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public long getLastModifiedMs() {
        return lastModifiedMs;
    }

    @Override
    public String toString() {
        return "FileMediaEntity{id='" + id + "', lastModifiedMs=" + lastModifiedMs + '}';
    }
}

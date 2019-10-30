package com.totalchange.discodj.catalogue;

import com.totalchange.discodj.server.media.MediaEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class TestMediaEntityListBuilder {
    private List<MediaEntity> mediaEntities = new ArrayList<>();

    public TestMediaEntityListBuilder withMediaEntity(int id) {
        mediaEntities.add(new TestMediaEntity(id, id));
        return this;
    }

    public TestMediaEntityListBuilder withMediaEntity(int id, long lastModified) {
        mediaEntities.add(new TestMediaEntity(id, lastModified));
        return this;
    }

    public CompletableFuture<List<MediaEntity>> build() {
        return CompletableFutureWithRandomDelay.completeInABitWithThing(100, 300, mediaEntities);
    }
}

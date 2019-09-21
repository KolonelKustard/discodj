package com.totalchange.discodj.catalogue.sync;

import com.totalchange.discodj.server.media.Media;

public interface MediaEntitySyncHandler {
    void add(String id);
    void update(String id);
    void delete(String id);
}

package com.totalchange.discodj.catalogue.sync;

public interface MediaEntitySyncHandler {
    void add(String id);
    void update(String id);
    void delete(String id);
}

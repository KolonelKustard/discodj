package com.totalchange.discodj.populator;

import java.util.LinkedList;
import java.util.List;

class ActionsToTake {
    private List<String> toAdd = new LinkedList<>();
    private List<String> toUpdate = new LinkedList<>();
    private List<String> toDelete = new LinkedList<>();

    List<String> getToAdd() {
        return toAdd;
    }

    List<String> getToUpdate() {
        return toUpdate;
    }

    List<String> getToDelete() {
        return toDelete;
    }

    void add(String id) {
        toAdd.add(id);
    }

    void update(String id) {
        toUpdate.add(id);
    }

    void delete(String id) {
        toDelete.add(id);
    }
}

package com.totalchange.discodj.populator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

class ActionsToTake {
    private List<String> toAdd = new LinkedList<>();
    private List<String> toUpdate = new LinkedList<>();
    private List<String> toDelete = new LinkedList<>();

    Iterator<String> getToAdd() {
        return toAdd.iterator();
    }

    Iterator<String> getToUpdate() {
        return toUpdate.iterator();
    }

    Iterator<String> getToDelete() {
        return toDelete.iterator();
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

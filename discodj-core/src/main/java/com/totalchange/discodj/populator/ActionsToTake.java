/**
 * Copyright 2015 Ralph Jones
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

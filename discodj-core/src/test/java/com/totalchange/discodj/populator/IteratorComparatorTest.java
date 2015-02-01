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

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.totalchange.discodj.catalogue.Catalogue;

import static org.junit.Assert.assertEquals;

public class IteratorComparatorTest {
    @Test
    public void noChanges() {
        List<Catalogue.CatalogueEntity> src = new ListBuilder().add("1", 1)
                .add("2", 2).add("3", 3).add("4", 4).add("5", 5).build();
        List<Catalogue.CatalogueEntity> dest = new ListBuilder().add("1", 1)
                .add("2", 2).add("3", 3).add("4", 4).add("5", 5).build();

        IteratorComparator comparator = new IteratorComparator();
        ActionsToTake actions = comparator.compare(src.iterator(),
                dest.iterator());

        assertIteratorEmpty(actions.getToAdd());
        assertIteratorEmpty(actions.getToDelete());
        assertIteratorEmpty(actions.getToUpdate());
    }

    @Test
    public void allNewItems() {
        List<Catalogue.CatalogueEntity> src = new ListBuilder().add("1", 1)
                .add("2", 2).add("3", 3).add("4", 4).build();
        List<Catalogue.CatalogueEntity> dest = new ListBuilder().build();

        IteratorComparator comparator = new IteratorComparator();
        ActionsToTake actions = comparator.compare(src.iterator(),
                dest.iterator());

        assertIteratorEquals(src, actions.getToAdd());
        assertIteratorEmpty(actions.getToDelete());
        assertIteratorEmpty(actions.getToUpdate());
    }

    @Test
    public void deleteAllItems() {
        List<Catalogue.CatalogueEntity> src = new ListBuilder().build();
        List<Catalogue.CatalogueEntity> dest = new ListBuilder().add("1", 1)
                .add("2", 2).add("3", 3).add("4", 4).build();

        IteratorComparator comparator = new IteratorComparator();
        ActionsToTake actions = comparator.compare(src.iterator(),
                dest.iterator());

        assertIteratorEquals(dest, actions.getToDelete());
        assertIteratorEmpty(actions.getToAdd());
        assertIteratorEmpty(actions.getToUpdate());
    }

    @Test
    public void missingSomeItems() {
        List<Catalogue.CatalogueEntity> src = new ListBuilder().add("1", 1)
                .add("2", 2).add("3", 3).add("4", 4).add("5", 5).build();
        List<Catalogue.CatalogueEntity> dest = new ListBuilder().add("1", 1)
                .add("3", 3).add("4", 4).build();

        IteratorComparator comparator = new IteratorComparator();
        ActionsToTake actions = comparator.compare(src.iterator(),
                dest.iterator());

        assertIteratorEquals(new ListBuilder().add("2", "5").build(),
                actions.getToAdd());
        assertIteratorEmpty(actions.getToDelete());
        assertIteratorEmpty(actions.getToUpdate());
    }

    @Test
    public void needToDeleteSomeItems() {
        List<Catalogue.CatalogueEntity> src = new ListBuilder().add("1", 1)
                .add("3", 3).add("4", 4).build();
        List<Catalogue.CatalogueEntity> dest = new ListBuilder().add("1", 1)
                .add("2", 2).add("3", 3).add("4", 4).add("5", 5).build();

        IteratorComparator comparator = new IteratorComparator();
        ActionsToTake actions = comparator.compare(src.iterator(),
                dest.iterator());

        assertIteratorEquals(new ListBuilder().add("2", "5").build(),
                actions.getToDelete());
        assertIteratorEmpty(actions.getToAdd());
        assertIteratorEmpty(actions.getToUpdate());
    }

    @Test
    public void needToUpdateSomeItems() {
        List<Catalogue.CatalogueEntity> src = new ListBuilder().add("1", 1)
                .add("2", 3).add("3", 3).add("4", 4).add("5", 6).build();
        List<Catalogue.CatalogueEntity> dest = new ListBuilder().add("1", 1)
                .add("2", 2).add("3", 3).add("4", 4).add("5", 5).build();

        IteratorComparator comparator = new IteratorComparator();
        ActionsToTake actions = comparator.compare(src.iterator(),
                dest.iterator());

        assertIteratorEquals(new ListBuilder().add("2", "5").build(),
                actions.getToUpdate());
        assertIteratorEmpty(actions.getToAdd());
        assertIteratorEmpty(actions.getToDelete());
    }

    @Test
    public void needToAddDeleteAndUpdateSomeItems() {
        List<Catalogue.CatalogueEntity> src = new ListBuilder()
                .add("2", 3).add("3", 3).add("4", 4).add("5", 6).build();
        List<Catalogue.CatalogueEntity> dest = new ListBuilder().add("1", 1)
                .add("2", 2).add("3", 3).add("5", 5).build();

        IteratorComparator comparator = new IteratorComparator();
        ActionsToTake actions = comparator.compare(src.iterator(),
                dest.iterator());

        assertIteratorEquals(new ListBuilder().add("4").build(),
                actions.getToAdd());
        assertIteratorEquals(new ListBuilder().add("1").build(),
                actions.getToDelete());
        assertIteratorEquals(new ListBuilder().add("2", "5").build(),
                actions.getToUpdate());
    }

    @Test
    public void needToAddDeleteAndUpdateSomeMoreItems() {
        List<Catalogue.CatalogueEntity> src = new ListBuilder().add("1", 1)
                .add("2", 2).add("3", 3).add("5", 5).build();
        List<Catalogue.CatalogueEntity> dest = new ListBuilder()
        .add("2", 3).add("3", 3).add("4", 4).add("5", 6).build();

        IteratorComparator comparator = new IteratorComparator();
        ActionsToTake actions = comparator.compare(src.iterator(),
                dest.iterator());

        assertIteratorEquals(new ListBuilder().add("4").build(),
                actions.getToDelete());
        assertIteratorEquals(new ListBuilder().add("1").build(),
                actions.getToAdd());
        assertIteratorEquals(new ListBuilder().add("2", "5").build(),
                actions.getToUpdate());
    }

    private void assertIteratorEquals(List<Catalogue.CatalogueEntity> expected,
            List<String> actual) {
        StringBuilder expectedStr = new StringBuilder();
        StringBuilder actualStr = new StringBuilder();

        for (Catalogue.CatalogueEntity entity : expected) {
            if (expectedStr.length() > 0) {
                expectedStr.append(", ");
            }
            expectedStr.append(entity.getId());
        }

        for (String id : actual) {
            if (actualStr.length() > 0) {
                actualStr.append(", ");
            }
            actualStr.append(id);
        }

        assertEquals(expectedStr.toString(), actualStr.toString());
    }

    private void assertIteratorEmpty(List<String> actual) {
        assertIteratorEquals(
                Collections.<Catalogue.CatalogueEntity> emptyList(), actual);
    }

    private class ListBuilder {
        List<Catalogue.CatalogueEntity> things = new LinkedList<>();

        public ListBuilder add(final String id, final long lastModified) {
            things.add(new Catalogue.CatalogueEntity() {
                @Override
                public String getId() {
                    return id;
                }

                @Override
                public Date getLastModified() {
                    return new Date(lastModified);
                }

                @Override
                public boolean equals(Object obj) {
                    Catalogue.CatalogueEntity entity = (Catalogue.CatalogueEntity) obj;
                    return this.getId().equals(entity.getId())
                            && this.getLastModified().equals(
                                    entity.getLastModified());
                }

                @Override
                public String toString() {
                    return this.getId() + ", "
                            + this.getLastModified().getTime();
                }
            });

            return this;
        }

        public ListBuilder add(final String... ids) {
            for (String id : ids) {
                add(id, -1);
            }
            return this;
        }

        List<Catalogue.CatalogueEntity> build() {
            return things;
        }
    }
}

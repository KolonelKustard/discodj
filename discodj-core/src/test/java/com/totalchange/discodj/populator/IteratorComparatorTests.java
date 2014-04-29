package com.totalchange.discodj.populator;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.totalchange.discodj.catalogue.Catalogue;

import static org.junit.Assert.assertEquals;

public class IteratorComparatorTests {
    @Test
    public void allNewItems() {
        Iterator<Catalogue.CatalogueEntity> src = new ListBuilder().add("1", 1)
                .add("2", 2).add("3", 3).add("4", 4).build();
        Iterator<Catalogue.CatalogueEntity> dest = new ListBuilder().build();

        IteratorComparator comparator = new IteratorComparator();
        ActionsToTake actions = comparator.compare(src, dest);

        assertIteratorEquals(src, actions.getToAdd());
    }

    private void assertIteratorEquals(
            Iterator<Catalogue.CatalogueEntity> expected,
            Iterator<String> actual) {
        StringBuilder expectedStr = new StringBuilder();
        StringBuilder actualStr = new StringBuilder();

        while (expected.hasNext()) {
            if (expectedStr.length() > 0) {
                expectedStr.append(", ");
            }
            expectedStr.append(expected.next().getId());
        }

        while (actual.hasNext()) {
            if (actualStr.length() > 0) {
                actualStr.append(", ");
            }
            actualStr.append(actual.next());
        }

        assertEquals(expectedStr.toString(), actualStr.toString());
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

        Iterator<Catalogue.CatalogueEntity> build() {
            return things.iterator();
        }
    }
}

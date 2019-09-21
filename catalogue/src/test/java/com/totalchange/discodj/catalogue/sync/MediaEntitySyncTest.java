package com.totalchange.discodj.catalogue.sync;

import com.totalchange.discodj.server.media.MediaEntity;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

public class MediaEntitySyncTest {
    private MediaEntitySyncHandler handler;

    @Before
    public void setUp() {
        handler = mock(MediaEntitySyncHandler.class);
    }

    @Test
    public void noChanges() {
        MediaEntitySync.sync(mediaList(1, 2, 3, 4, 5), mediaList(5, 4, 3, 2, 1), handler);
        verify(handler, never()).add(anyString());
        verify(handler, never()).update(anyString());
        verify(handler, never()).delete(anyString());
    }

    @Test
    public void allNewItems() {
        MediaEntitySync.sync(mediaList(1, 2, 3, 4, 5), Collections.emptyList(), handler);
        verify(handler, times(1)).add("1");
        verify(handler, times(1)).add("2");
        verify(handler, times(1)).add("3");
        verify(handler, times(1)).add("4");
        verify(handler, times(1)).add("5");
        verify(handler, never()).update(anyString());
        verify(handler, never()).delete(anyString());
    }

    @Test
    public void deleteAllItems() {
        MediaEntitySync.sync(Collections.emptyList(), mediaList(1, 2, 3, 4, 5), handler);
        verify(handler, never()).add(anyString());
        verify(handler, never()).update(anyString());
        verify(handler, times(1)).delete("1");
        verify(handler, times(1)).delete("2");
        verify(handler, times(1)).delete("3");
        verify(handler, times(1)).delete("4");
        verify(handler, times(1)).delete("5");
    }

    @Test
    public void missingSomeItems() {
        MediaEntitySync.sync(mediaList(1, 2, 3, 4, 5), mediaList(1, 3, 4), handler);
        verify(handler, times(1)).add("2");
        verify(handler, times(1)).add("5");
        verify(handler, never()).update(anyString());
        verify(handler, never()).delete(anyString());
    }

    @Test
    public void needToDeleteSomeItems() {
        MediaEntitySync.sync(mediaList(1, 3, 4), mediaList(1, 2, 3, 4, 5), handler);
        verify(handler, never()).add(anyString());
        verify(handler, never()).update(anyString());
        verify(handler, times(1)).delete("2");
        verify(handler, times(1)).delete("5");
    }

    @Test
    public void needToUpdateSomeItems() {
        MediaEntitySync.sync(mediaList(m(1, 1), m(2, 1), m(3, 3), m(4, 4), m(5, 6)), mediaList(1, 2, 3, 4, 5), handler);
        verify(handler, never()).add(anyString());
        verify(handler, times(1)).update("2");
        verify(handler, times(1)).update("5");
        verify(handler, never()).delete(anyString());
    }

    @Test
    public void needToAddUpdateAndDeleteSomeItems() {
        MediaEntitySync.sync(mediaList(m(2, 3), m(3, 3), m(4, 4), m(5, 6)), mediaList(1, 2, 3, 5), handler);
        verify(handler, times(1)).add("4");
        verify(handler, times(1)).update("2");
        verify(handler, times(1)).update("5");
        verify(handler, times(1)).delete("1");
    }

    @Test
    public void needToAddUpdateAndDeleteSomeMoreItems() {
        MediaEntitySync.sync(mediaList(1, 2, 3, 5), mediaList(m(2, 3), m(3, 3), m(4, 4), m(5, 6)), handler);
        verify(handler, times(1)).add("1");
        verify(handler, times(1)).update("2");
        verify(handler, times(1)).update("5");
        verify(handler, times(1)).delete("4");
    }

    private List<MediaEntity> mediaList(final int... ids) {
        final List<MediaEntity> l = new ArrayList<>(ids.length);
        for (int id : ids) {
            l.add(new TestMediaEntity(String.valueOf(id), id));
        }
        return l;
    }

    private List<MediaEntity> mediaList(final MediaEntity... media) {
        final List<MediaEntity> l = new ArrayList<>(media.length);
        for (MediaEntity m : media) {
            l.add(m);
        }
        return l;
    }

    private MediaEntity m(int id, long lastModified) {
        return new TestMediaEntity(String.valueOf(id), lastModified);
    }

    private static class TestMediaEntity implements MediaEntity {
        private final String id;
        private final long lastModified;

        private TestMediaEntity(final String id, final long lastModified) {
            this.id = id;
            this.lastModified = lastModified;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public long getLastModified() {
            return lastModified;
        }
    }
}

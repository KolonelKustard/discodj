package com.totalchange.discodj.catalogue;

import com.totalchange.discodj.server.media.Media;
import com.totalchange.discodj.server.media.MediaSource;
import com.totalchange.discodj.server.search.SearchPopulator;
import com.totalchange.discodj.server.search.SearchProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class CatalogueSourceTest {
    private MediaSource mediaSource;
    private SearchProvider searchProvider;
    private SearchPopulator searchPopulator;
    private CatalogueSource catalogueSource;

    @Before
    public void setUp() {
        mediaSource = mock(MediaSource.class);
        searchProvider = mock(SearchProvider.class);
        searchPopulator = mock(SearchPopulator.class);
        when(searchProvider.createPopulator()).thenReturn(searchPopulator);

        catalogueSource = new CatalogueSource(
                Executors.newSingleThreadExecutor(new NamedThreadFactory("catalogue-source-test")), mediaSource,
                searchProvider);
    }

    @Test
    public void addsSomeThingsToTheSearchProvider() throws ExecutionException, InterruptedException {
        when(mediaSource.getId()).thenReturn("test");
        when(mediaSource.getAllMediaEntities()).thenReturn(new TestMediaEntityListBuilder()
                .withMediaEntity(0)
                .withMediaEntity(1)
                .withMediaEntity(2)
                .withMediaEntity(3)
                .withMediaEntity(4)
                .build());
        when(mediaSource.getMedia("0")).thenReturn(mockMedia(0));
        when(mediaSource.getMedia("1")).thenReturn(mockMedia(1));
        when(mediaSource.getMedia("2")).thenReturn(mockMedia(2));
        when(mediaSource.getMedia("3")).thenReturn(mockMedia(3));
        when(mediaSource.getMedia("4")).thenReturn(mockMedia(4));

        when(searchProvider.getAllMediaEntities("test")).thenReturn(CompletableFuture.completedFuture(new ArrayList<>()));

        catalogueSource.refresh().get();

        verify(searchPopulator).addMedia(new TestMedia(0));
        verify(searchPopulator).addMedia(new TestMedia(1));
        verify(searchPopulator).addMedia(new TestMedia(2));
        verify(searchPopulator).addMedia(new TestMedia(3));
        verify(searchPopulator).addMedia(new TestMedia(4));
        verify(searchPopulator, never()).updateMedia(any());
        verify(searchPopulator, never()).deleteMedia(anyString());
        verify(searchPopulator).commit();
    }

    @Test
    public void updatesSomeThingsInTheSearchProvider() throws ExecutionException, InterruptedException {
        when(mediaSource.getId()).thenReturn("test");

        when(mediaSource.getAllMediaEntities()).thenReturn(new TestMediaEntityListBuilder()
                .withMediaEntity(0)
                .withMediaEntity(1)
                .withMediaEntity(2)
                .withMediaEntity(3)
                .withMediaEntity(4)
                .build());

        when(mediaSource.getMedia("0")).thenReturn(mockMedia(0));
        when(mediaSource.getMedia("1")).thenReturn(mockMedia(1));
        when(mediaSource.getMedia("2")).thenReturn(mockMedia(2));
        when(mediaSource.getMedia("3")).thenReturn(mockMedia(3));
        when(mediaSource.getMedia("4")).thenReturn(mockMedia(4));

        when(searchProvider.getAllMediaEntities("test")).thenReturn(new TestMediaEntityListBuilder()
                .withMediaEntity(0, 1)
                .withMediaEntity(1, 2)
                .withMediaEntity(2, 3)
                .withMediaEntity(3, 4)
                .withMediaEntity(4, 5)
                .build());

        catalogueSource.refresh().get();

        verify(searchPopulator).updateMedia(new TestMedia(0));
        verify(searchPopulator).updateMedia(new TestMedia(1));
        verify(searchPopulator).updateMedia(new TestMedia(2));
        verify(searchPopulator).updateMedia(new TestMedia(3));
        verify(searchPopulator).updateMedia(new TestMedia(4));
        verify(searchPopulator, never()).addMedia(any());
        verify(searchPopulator, never()).deleteMedia(anyString());
        verify(searchPopulator).commit();
    }

    @Test
    public void deletesSomeThingsInTheSearchProvider() throws ExecutionException, InterruptedException {
        when(mediaSource.getId()).thenReturn("test");

        when(mediaSource.getAllMediaEntities()).thenReturn(new TestMediaEntityListBuilder().build());

        when(searchProvider.getAllMediaEntities("test")).thenReturn(new TestMediaEntityListBuilder()
                .withMediaEntity(0)
                .withMediaEntity(1)
                .withMediaEntity(2)
                .withMediaEntity(3)
                .withMediaEntity(4)
                .build());

        catalogueSource.refresh().get();

        verify(searchPopulator).deleteMedia("0");
        verify(searchPopulator).deleteMedia("1");
        verify(searchPopulator).deleteMedia("2");
        verify(searchPopulator).deleteMedia("3");
        verify(searchPopulator).deleteMedia("4");
        verify(searchPopulator, never()).addMedia(any());
        verify(searchPopulator, never()).updateMedia(any());
        verify(searchPopulator).commit();
    }

    @Test
    public void addsUpdatesAndDeletesSomeThingsInTheSearchProvider() throws ExecutionException, InterruptedException {
        when(mediaSource.getId()).thenReturn("test");

        when(mediaSource.getAllMediaEntities()).thenReturn(new TestMediaEntityListBuilder()
                .withMediaEntity(1)
                .withMediaEntity(2)
                .withMediaEntity(4)
                .withMediaEntity(5)
                .build());

        when(mediaSource.getMedia("1")).thenReturn(mockMedia(1));
        when(mediaSource.getMedia("2")).thenReturn(mockMedia(2));
        when(mediaSource.getMedia("4")).thenReturn(mockMedia(4));
        when(mediaSource.getMedia("5")).thenReturn(mockMedia(5));

        when(searchProvider.getAllMediaEntities("test")).thenReturn(new TestMediaEntityListBuilder()
                .withMediaEntity(0)
                .withMediaEntity(1, 2)
                .withMediaEntity(3)
                .withMediaEntity(5)
                .build());

        catalogueSource.refresh().get();

        verify(searchPopulator).deleteMedia("0");
        verify(searchPopulator).updateMedia(new TestMedia(1));
        verify(searchPopulator).addMedia(new TestMedia(2));
        verify(searchPopulator).deleteMedia("3");
        verify(searchPopulator).addMedia(new TestMedia(4));
        verify(searchPopulator).commit();
    }

    @Test
    public void doesNothingIfNothingHasChanged() throws ExecutionException, InterruptedException {
        when(mediaSource.getId()).thenReturn("test");
        when(mediaSource.getAllMediaEntities()).thenReturn(new TestMediaEntityListBuilder()
                .withMediaEntity(0)
                .withMediaEntity(1)
                .withMediaEntity(2)
                .withMediaEntity(3)
                .withMediaEntity(4)
                .build());

        when(searchProvider.getAllMediaEntities("test")).thenReturn(new TestMediaEntityListBuilder()
                .withMediaEntity(0)
                .withMediaEntity(1)
                .withMediaEntity(2)
                .withMediaEntity(3)
                .withMediaEntity(4)
                .build());

        catalogueSource.refresh().get();

        verify(searchPopulator, never()).addMedia(any());
        verify(searchPopulator, never()).updateMedia(any());
        verify(searchPopulator, never()).deleteMedia(anyString());
        verify(searchProvider, never()).createPopulator();
        verify(searchPopulator, never()).commit();
    }

    @Test
    public void handlesFailureFromMediaSourceToGetAllMedia() throws ExecutionException, InterruptedException {
        when(mediaSource.getAllMediaEntities()).thenThrow(new TestException("Media source get all"));
        assertExceptional(catalogueSource.refresh(), "Media source get all");
    }

    @Test
    public void handlesFailureFromMediaSourceToGetAllMediaAsync() throws ExecutionException, InterruptedException {
        when(mediaSource.getId()).thenReturn("test");
        when(mediaSource.getAllMediaEntities()).thenReturn(
                CompletableFutureWithRandomDelay.completeWithErrorInABit(100, 200, "Media source get all future"));
        when(searchProvider.getAllMediaEntities("test")).thenReturn(new TestMediaEntityListBuilder().build());
        assertExceptional(catalogueSource.refresh(), "Media source get all future");
    }

    private CompletableFuture<Media> mockMedia(final int id) {
        return CompletableFutureWithRandomDelay.completeInABitWithThing(200, 500, new TestMedia(id));
    }

    private void assertExceptional(final CompletableFuture<?> f, final String msg) throws InterruptedException {
        try {
            f.get();
        } catch (ExecutionException ex) {
            assertNotNull("Expected there to be a cause for the completable future failure", ex.getCause());
            assertEquals("Expected cause of completable future failure to be a TestException",
                    TestException.class, ex.getCause().getClass());
            assertEquals("Expected an error message in the cause of " + msg, ex.getCause().getMessage(), msg);
        }
    }
}

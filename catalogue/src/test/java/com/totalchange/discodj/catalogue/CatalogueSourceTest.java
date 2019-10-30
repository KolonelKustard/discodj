package com.totalchange.discodj.catalogue;

import com.totalchange.discodj.server.media.Media;
import com.totalchange.discodj.server.media.MediaEntity;
import com.totalchange.discodj.server.media.MediaSource;
import com.totalchange.discodj.server.search.SearchPopulator;
import com.totalchange.discodj.server.search.SearchProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.*;

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
        verify(searchPopulator).commit();
    }

    @Test
    public void deletesSomeThingsInTheSearchProvider() throws ExecutionException, InterruptedException {
        when(mediaSource.getId()).thenReturn("test");

        when(mediaSource.getAllMediaEntities()).thenReturn(new TestMediaEntityListBuilder().build());

        when(mediaSource.getMedia("0")).thenReturn(mockMedia(0));
        when(mediaSource.getMedia("1")).thenReturn(mockMedia(1));
        when(mediaSource.getMedia("2")).thenReturn(mockMedia(2));
        when(mediaSource.getMedia("3")).thenReturn(mockMedia(3));
        when(mediaSource.getMedia("4")).thenReturn(mockMedia(4));

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
        verify(searchPopulator).commit();
    }

    private CompletableFuture<Media> mockMedia(int id) {
        return CompletableFutureWithRandomDelay.completeInABitWithThing(200, 500, new TestMedia(id));
    }
}

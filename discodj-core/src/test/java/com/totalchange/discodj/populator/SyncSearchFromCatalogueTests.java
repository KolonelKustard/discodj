package com.totalchange.discodj.populator;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.totalchange.discodj.catalogue.Catalogue;
import com.totalchange.discodj.media.Media;
import com.totalchange.discodj.search.SearchPopulator;
import com.totalchange.discodj.search.SearchProvider;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class SyncSearchFromCatalogueTests {
    private Catalogue catalogue;
    private SearchProvider searchProvider;
    private SearchPopulator searchPopulator;
    private IteratorComparator iteratorComparator;
    private ActionsToTake actionsToTake;
    private List<String> toAdd;
    private List<String> toUpdate;
    private List<String> toDelete;
    private SyncSearchFromCatalogue syncSearchFromCatalogue;

    @Before
    public void setUp() {
        catalogue = mock(Catalogue.class);
        searchProvider = mock(SearchProvider.class);
        searchPopulator = mock(SearchPopulator.class);
        iteratorComparator = mock(IteratorComparator.class);
        actionsToTake = mock(ActionsToTake.class);
        toAdd = new ArrayList<>();
        toUpdate = new ArrayList<>();
        toDelete = new ArrayList<>();

        when(searchProvider.repopulate()).thenReturn(searchPopulator);
        when(iteratorComparator.compare(null, null)).thenReturn(actionsToTake);
        when(actionsToTake.getToAdd()).thenReturn(toAdd);
        when(actionsToTake.getToUpdate()).thenReturn(toUpdate);
        when(actionsToTake.getToDelete()).thenReturn(toDelete);

        syncSearchFromCatalogue = new SyncSearchFromCatalogue(catalogue,
                searchProvider, iteratorComparator);
    }

    @Test
    public void shouldFailSyncIfAlreadyInProgress() throws InterruptedException {
        final Object waitToBeInProgress = new Object();
        final Object waitUntilCompleted = new Object();

        when(catalogue.listAllAlphabeticallyById()).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                synchronized (waitToBeInProgress) {
                    waitToBeInProgress.notify();
                }
                synchronized (waitUntilCompleted) {
                    waitUntilCompleted.wait();
                }
                return null;
            }
        });

        Thread pauser = new Thread() {
            @Override
            public void run() {
                syncSearchFromCatalogue.sync();
            }
        };
        pauser.start();

        synchronized (waitToBeInProgress) {
            waitToBeInProgress.wait();
        }

        assertTrue(syncSearchFromCatalogue.isInProgress());
        assertEquals(Messages.getStatusFindingChanges(),
                syncSearchFromCatalogue.getStatus());
        try {
            syncSearchFromCatalogue.sync();
            fail("Should have complained about being in progress already");
        } catch (SyncInProgressException sEx) {
            // All good :-)
        }

        synchronized (waitUntilCompleted) {
            waitUntilCompleted.notify();
        }
    }

    @Test
    public void doesAWholeLoadOfSyncing() {
        toAdd.add("a1");
        toAdd.add("a2");
        toAdd.add("a3");
        toDelete.add("d1");
        toDelete.add("d2");
        toDelete.add("d3");
        toUpdate.add("u1");
        toUpdate.add("u2");
        toUpdate.add("u3");

        Media add1 = mock(Media.class);
        Media add2 = mock(Media.class);
        Media add3 = mock(Media.class);
        when(catalogue.getMedia("a1")).thenReturn(add1);
        when(catalogue.getMedia("a2")).thenReturn(add2);
        when(catalogue.getMedia("a3")).thenReturn(add3);

        Media update1 = mock(Media.class);
        Media update2 = mock(Media.class);
        Media update3 = mock(Media.class);
        when(catalogue.getMedia("u1")).thenReturn(update1);
        when(catalogue.getMedia("u2")).thenReturn(update2);
        when(catalogue.getMedia("u3")).thenReturn(update3);

        syncSearchFromCatalogue.sync();

        verify(searchPopulator).deleteMedia("d1");
        verify(searchPopulator).deleteMedia("d2");
        verify(searchPopulator).deleteMedia("d3");

        verify(searchPopulator).addMedia(add1);
        verify(searchPopulator).addMedia(add2);
        verify(searchPopulator).addMedia(add3);

        verify(searchPopulator).updateMedia(update1);
        verify(searchPopulator).updateMedia(update2);
        verify(searchPopulator).updateMedia(update3);

        verify(searchPopulator).commit();
    }

    @Test
    public void deletesAllWhenDoingFullRefresh() {
        toAdd.add("a1");
        Media add1 = mock(Media.class);
        when(catalogue.getMedia("a1")).thenReturn(add1);

        syncSearchFromCatalogue.fullRefresh();

        verify(searchPopulator).deleteAll();
        verify(searchPopulator).addMedia(add1);
        verify(searchPopulator, times(2)).commit();
    }
}

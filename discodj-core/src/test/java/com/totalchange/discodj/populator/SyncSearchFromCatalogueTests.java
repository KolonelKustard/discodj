package com.totalchange.discodj.populator;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.totalchange.discodj.catalogue.Catalogue;
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
        assertEquals("Looking for changes to be made",
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
}

package com.totalchange.discodj.populator;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.totalchange.discodj.catalogue.Catalogue;
import com.totalchange.discodj.catalogue.CatalogueException;
import com.totalchange.discodj.media.Media;
import com.totalchange.discodj.search.SearchPopulator;
import com.totalchange.discodj.search.SearchProvider;

@Singleton
public class SyncSearchFromCatalogue {
    private static final Logger logger = LoggerFactory
            .getLogger(SyncSearchFromCatalogue.class);

    private Catalogue catalogue;
    private SearchProvider searchProvider;
    private IteratorComparator iteratorComparator;

    private boolean inProgress = false;
    private String status = Messages.getStatusIdle();
    private long totalChanges = 0;
    private long currentItem = 0;

    SyncSearchFromCatalogue(Catalogue catalogue, SearchProvider searchProvider,
            IteratorComparator iteratorComparator) {
        this.catalogue = catalogue;
        this.searchProvider = searchProvider;
        this.iteratorComparator = iteratorComparator;
    }

    @Inject
    public SyncSearchFromCatalogue(Catalogue catalogue,
            SearchProvider searchProvider) {
        this(catalogue, searchProvider, new IteratorComparator());
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public String getStatus() {
        return status;
    }

    public int getPercentageProgress() {
        if (totalChanges <= 0) {
            return 0;
        } else {
            double percent = ((double) currentItem / (double) totalChanges) * 100;
            return (int) percent;
        }
    }

    public void sync() throws SyncInProgressException {
        logger.trace("Started sync operation");

        assertNotInProgress();
        doSync();

        logger.trace("Finished sync");
    }

    public void fullRefresh() throws SyncInProgressException {
        logger.trace("Started full refresh");
        assertNotInProgress();

        logger.trace("Deleting all");
        SearchPopulator populator = searchProvider.createPopulator();
        populator.deleteAll();
        populator.commit();

        logger.trace("Doing sync");
        doSync();

        logger.trace("Finished full refresh");
    }

    private void doSync() {
        long startTime = System.currentTimeMillis();

        setStatus(Messages.getStatusFindingChanges());
        ActionsToTake actionsToTake = iteratorComparator.compare(
                catalogue.listAllAlphabeticallyById(),
                searchProvider.listAllAlphabeticallyById());

        totalChanges = actionsToTake.getToAdd().size()
                + actionsToTake.getToUpdate().size()
                + actionsToTake.getToDelete().size();
        if (logger.isTraceEnabled()) {
            logger.trace(actionsToTake.getToAdd().size() + " additions, "
                    + actionsToTake.getToDelete().size() + " deletions and "
                    + actionsToTake.getToUpdate().size()
                    + " updates to be made (" + totalChanges + " in total)");
        }

        if (totalChanges > 0) {
            SearchPopulator searchPopulator = searchProvider.createPopulator();
            currentItem = 0;

            delete(searchPopulator, actionsToTake.getToDelete());
            add(searchPopulator, actionsToTake.getToAdd());
            update(searchPopulator, actionsToTake.getToUpdate());

            logger.trace("Committing");
            searchPopulator.commit();
        }

        logger.info("Completed synchronising {} items in {}ms", currentItem,
                System.currentTimeMillis() - startTime);

        setStatus(Messages.getStatusIdle());
        currentItem = 0;
        totalChanges = 0;
        inProgress = false;
    }

    private synchronized void assertNotInProgress()
            throws SyncInProgressException {
        if (isInProgress()) {
            logger.warn("Sync aborted as already in progress");
            throw new SyncInProgressException(Messages.getInProgressException());
        }

        inProgress = true;
    }

    private void setStatus(String status) {
        this.status = status;
    }

    private void delete(SearchPopulator searchPopulator,
            List<String> itemsToDelete) {
        for (String id : itemsToDelete) {
            currentItem++;

            setStatus(Messages.getStatusDeleting(currentItem, totalChanges, id));
            if (logger.isTraceEnabled()) {
                logger.trace("Deleting item " + currentItem + " of "
                        + totalChanges + " (id: " + id + ")");
            }

            searchPopulator.deleteMedia(id);
        }
    }

    private void add(SearchPopulator searchPopulator, List<String> itemsToAdd) {
        for (String id : itemsToAdd) {
            currentItem++;
            try {
                Media media = grabAndCheckMediaIsSafe(id);

                setStatus(Messages.getStatusAdding(currentItem, totalChanges,
                        media.getTitle(), media.getArtist()));
                if (logger.isTraceEnabled()) {
                    logger.trace("Adding item " + currentItem + " of "
                            + totalChanges + " (id: " + id + ")");
                }

                searchPopulator.addMedia(media);
            } catch (CatalogueException catEx) {
                logger.info("Skipped adding media item with id " + id
                        + " because of catalogue failure", catEx);
            }
        }
    }

    private void update(SearchPopulator searchPopulator,
            List<String> itemsToUpdate) {
        for (String id : itemsToUpdate) {
            currentItem++;

            try {
                Media media = grabAndCheckMediaIsSafe(id);

                setStatus(Messages.getStatusUpdating(currentItem, totalChanges,
                        media.getTitle(), media.getArtist()));
                if (logger.isTraceEnabled()) {
                    logger.trace("Updating item " + currentItem + " of "
                            + totalChanges + " (id: " + id + ")");
                }

                searchPopulator.updateMedia(media);
            } catch (CatalogueException catEx) {
                logger.info("Skipped updating media item with id " + id, catEx);
            }
        }
    }

    private Media grabAndCheckMediaIsSafe(String id) throws CatalogueException {
        Media media = catalogue.getMedia(id);
        if (media.getTitle() == null || media.getTitle().trim().length() <= 0) {
            throw new CatalogueException("No title for media " + id);
        } else {
            return media;
        }
    }
}

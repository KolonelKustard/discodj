package com.totalchange.discodj.web.client.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

import net.customware.gwt.dispatch.client.DispatchAsync;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.totalchange.discodj.web.client.error.ErrorHandler;
import com.totalchange.discodj.web.client.places.DjPlace;
import com.totalchange.discodj.web.client.views.DjView;
import com.totalchange.discodj.web.shared.dj.DjMedia;
import com.totalchange.discodj.web.shared.dj.SearchAction;
import com.totalchange.discodj.web.shared.dj.SearchResult;
import com.totalchange.discodj.web.shared.dj.StatusAction;
import com.totalchange.discodj.web.shared.dj.StatusResult;
import com.totalchange.discodj.web.shared.dj.UpdatePlaylistAction;

public class DjActivity extends AbstractActivity implements DjView.Presenter {
    private static final Logger logger = Logger.getLogger(DjActivity.class
            .getName());

    private DjView djView;
    private PlaceController placeController;
    private DispatchAsync dispatchAsync;
    private ErrorHandler errorHandler;

    private String keywords = null;
    private List<String> facetIds = null;
    private int page = 1;

    private List<DjMedia> playlist = null;

    @Inject
    public DjActivity(DjView djView, PlaceController placeController,
            DispatchAsync dispatchAsync, ErrorHandler errorHandler) {
        this.djView = djView;
        this.placeController = placeController;
        this.dispatchAsync = dispatchAsync;
        this.errorHandler = errorHandler;

        this.djView.setPresenter(this);
    }

    private void showSearchResults(SearchResult result) {
        djView.setResults(page, result.getNumPages(), result.getResults());
        djView.setArtistFacets(result.getArtistFacets());
        djView.setAlbumFacets(result.getAlbumFacets());
        djView.setGenreFacets(result.getGenreFacets());
        djView.setDecadeFacets(result.getDecadeFacets());
    }

    private boolean isDifferentToPlaylist(List<DjMedia> other) {
        if (playlist == null) {
            return true;
        }

        if (playlist.size() != other.size()) {
            return true;
        }

        for (int num = 0; num < playlist.size(); num++) {
            String id1 = playlist.get(num).getId();
            String id2 = other.get(num).getId();

            if (!id1.equals(id2)) {
                return true;
            }
        }

        return false;
    }

    private void showStatusUpdate(StatusResult result) {
        if (result.getNowPlaying() != null) {
            djView.setNowPlaying(result.getNowPlaying().getArtist(), result
                    .getNowPlaying().getTitle());
        } else {
            djView.setNowPlaying(null, null);
        }

        if (isDifferentToPlaylist(result.getPlaylist())) {
            playlist = result.getPlaylist();
            djView.setPlaylist(result.getPlaylist());
        }

        // TODO Trigger another time delayed update
    }

    private void requestStatusUpdate() {
        StatusAction action = new StatusAction();
        dispatchAsync.execute(action, new AsyncCallback<StatusResult>() {
            @Override
            public void onFailure(Throwable caught) {
                errorHandler.loadingError(caught);
            }

            @Override
            public void onSuccess(StatusResult result) {
                showStatusUpdate(result);
            }
        });
    }

    public void search() {
        logger.finer("Performing search for '" + keywords + "', facets "
                + facetIds + " and page " + page);

        SearchAction action = new SearchAction();
        action.setKeywords(keywords);
        action.setFacetIds(facetIds);
        action.setPage(page);
        dispatchAsync.execute(action, new AsyncCallback<SearchResult>() {
            @Override
            public void onFailure(Throwable caught) {
                errorHandler.loadingError(caught);
            }

            @Override
            public void onSuccess(SearchResult result) {
                logger.finer("Got result " + result);
                showSearchResults(result);
            }
        });
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public void setFacetIds(List<String> facetIds) {
        this.facetIds = facetIds;
    }

    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public void start(AcceptsOneWidget container, EventBus eventBus) {
        logger.finer("Starting up InitJizzActivity");
        container.setWidget(djView.asWidget());
        requestStatusUpdate();
        logger.finer("Finished starting up InitJizzActivity");
    }

    @Override
    public void search(String keywords) {
        DjPlace place = new DjPlace();
        place.setKeywords(keywords);

        placeController.goTo(place);
    }

    @Override
    public void addFacet(String facetId) {
        if (facetIds == null) {
            facetIds = new ArrayList<String>();
        }
        facetIds.add(facetId);

        DjPlace place = new DjPlace();
        place.setKeywords(keywords);
        place.setFacetIds(facetIds);

        placeController.goTo(place);
    }

    @Override
    public void removeFacet(String facetId) {
        if (facetIds != null) {
            facetIds.remove(facetId);
            if (facetIds.isEmpty()) {
                facetId = null;
            }
        }

        DjPlace place = new DjPlace();
        place.setKeywords(keywords);
        place.setFacetIds(facetIds);

        placeController.goTo(place);
    }

    private void goToPage(int page) {
        DjPlace place = new DjPlace();
        place.setKeywords(keywords);
        place.setFacetIds(facetIds);
        place.setPage(page);

        placeController.goTo(place);
    }

    @Override
    public void previousPage() {
        if (page > 1) {
            goToPage(page - 1);
        }
    }

    @Override
    public void nextPage() {
        goToPage(page + 1);
    }

    @Override
    public void playlistPossiblyChanged() {
        List<DjMedia> revised = djView.getPlaylist();
        if (isDifferentToPlaylist(revised)) {
            logger.fine("Updating playlist");

            UpdatePlaylistAction action = new UpdatePlaylistAction();
            List<DjMedia> playlist = djView.getPlaylist();
            List<String> revisedPlaylistMediaIds = new ArrayList<String>(
                    playlist.size());
            for (DjMedia media : playlist) {
                revisedPlaylistMediaIds.add(media.getId());
            }
            action.setRevisedPlaylist(revisedPlaylistMediaIds);
            dispatchAsync.execute(action, new AsyncCallback<StatusResult>() {
                @Override
                public void onFailure(Throwable caught) {
                    errorHandler.loadingError(caught);
                }

                @Override
                public void onSuccess(StatusResult result) {
                    logger.fine("Playlist updated");
                    showStatusUpdate(result);
                }
            });
        }
    }
}

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
import com.totalchange.discodj.web.shared.dj.SearchAction;
import com.totalchange.discodj.web.shared.dj.SearchResult;
import com.totalchange.discodj.web.shared.player.Media;

public class DjActivity extends AbstractActivity implements DjView.Presenter {
    private static final Logger logger = Logger.getLogger(DjActivity.class
            .getName());

    private DjView djView;
    private PlaceController placeController;
    private DispatchAsync dispatchAsync;
    private ErrorHandler errorHandler;

    private String keywords = null;
    private List<String> facetIds = null;

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

    }

    private void search() {
        SearchAction action = new SearchAction();
        action.setKeywords(keywords);
        action.setFacetIds(facetIds);
        dispatchAsync.execute(action, new AsyncCallback<SearchResult>() {
            @Override
            public void onFailure(Throwable caught) {
                errorHandler.loadingError(caught);
            }

            @Override
            public void onSuccess(SearchResult result) {
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

    @Override
    public void start(AcceptsOneWidget container, EventBus eventBus) {
        logger.finer("Starting up InitJizzActivity");
        container.setWidget(djView.asWidget());

        if ((keywords != null && keywords.length() > 0)
                || (facetIds != null && !facetIds.isEmpty())) {
            search();
        }

        logger.finer("Finished starting up InitJizzActivity");
    }

    @Override
    public void setPlaylist(List<Media> playlist) {
        // TODO Auto-generated method stub
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
}

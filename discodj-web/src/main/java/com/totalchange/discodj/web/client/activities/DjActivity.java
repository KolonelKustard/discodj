package com.totalchange.discodj.web.client.activities;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

import net.customware.gwt.dispatch.client.DispatchAsync;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.totalchange.discodj.web.client.views.DjView;
import com.totalchange.discodj.web.shared.player.Media;

public class DjActivity extends AbstractActivity implements DjView.Presenter {
    private static final Logger logger = Logger.getLogger(DjActivity.class
            .getName());

    private DjView djView;
    private DispatchAsync dispatchAsync;

    @Inject
    public DjActivity(DjView djView, DispatchAsync dispatchAsync) {
        this.djView = djView;
        this.dispatchAsync = dispatchAsync;

        this.djView.setPresenter(this);
    }

    @Override
    public void start(AcceptsOneWidget container, EventBus eventBus) {
        logger.finer("Starting up InitJizzActivity");
        container.setWidget(djView.asWidget());
        logger.finer("Finished starting up InitJizzActivity");
    }

    @Override
    public void setPlaylist(List<Media> playlist) {
        // TODO Auto-generated method stub
    }
}

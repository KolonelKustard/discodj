package com.totalchange.discodj.web.client.activities;

import java.util.logging.Logger;

import javax.inject.Inject;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.totalchange.discodj.web.client.views.DjView;

public class DjActivity extends AbstractActivity implements DjView.Presenter {
    private static final Logger logger = Logger.getLogger(DjActivity.class
            .getName());

    private DjView djView;

    @Inject
    public DjActivity(DjView djView) {
        this.djView = djView;

        this.djView.setPresenter(this);
    }

    @Override
    public void start(AcceptsOneWidget container, EventBus eventBus) {
        logger.finer("Starting up InitJizzActivity");
        container.setWidget(djView.asWidget());
        logger.finer("Finished starting up InitJizzActivity");
    }
}

/**
 * Copyright 2012 Ralph Jones.
 * 
 * This file is part of Jizz.  Jizz is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * Jizz is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Jizz.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.totalchange.discodj.web.client.activities;

import java.util.logging.Logger;

import javax.inject.Inject;

import net.customware.gwt.dispatch.client.DispatchAsync;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.totalchange.discodj.web.client.error.ErrorHandler;
import com.totalchange.discodj.web.client.views.PlayerView;

public class PlayerActivity extends AbstractActivity implements
        PlayerView.Presenter {
    private static final Logger logger = Logger
            .getLogger(PlayerActivity.class.getName());

    private PlayerView initJizzView;
    private PlaceController placeController;
    private DispatchAsync dispatch;
    private ErrorHandler errorHandler;

    @Inject
    public PlayerActivity(PlayerView initJizzView,
            PlaceController placeController, DispatchAsync dispatch,
            ErrorHandler errorHandler) {
        this.initJizzView = initJizzView;
        this.placeController = placeController;
        this.dispatch = dispatch;
        this.errorHandler = errorHandler;

        this.initJizzView.setPresenter(this);
    }

    @Override
    public void start(AcceptsOneWidget container, EventBus eventBus) {
        logger.finer("Starting up InitJizzActivity");
        container.setWidget(initJizzView.asWidget());
        logger.finer("Finished starting up InitJizzActivity");
    }

    @Override
    public void finishedPlayingCurrent() {
        // TODO Auto-generated method stub
        
    }
}

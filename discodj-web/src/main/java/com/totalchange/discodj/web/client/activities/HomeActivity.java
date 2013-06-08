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
import com.totalchange.discodj.web.client.places.DjPlace;
import com.totalchange.discodj.web.client.places.PlayerPlace;
import com.totalchange.discodj.web.client.views.HomeView;

public class HomeActivity extends AbstractActivity implements
        HomeView.Presenter {
    private static final Logger logger = Logger.getLogger(HomeActivity.class
            .getName());

    private HomeView homeView;
    private PlaceController placeController;
    private DispatchAsync dispatch;

    @Inject
    public HomeActivity(HomeView homeView, PlaceController placeController,
            DispatchAsync dispatch) {
        this.homeView = homeView;
        this.placeController = placeController;
        this.dispatch = dispatch;

        this.homeView.setPresenter(this);
    }

    @Override
    public void start(final AcceptsOneWidget container, EventBus eventBus) {
        logger.finer("Starting up Home activity");
        container.setWidget(homeView.asWidget());
        logger.finer("Finished starting up Home activity");
    }

    @Override
    public void goToPlayer() {
        placeController.goTo(new PlayerPlace());
    }

    @Override
    public void goToDj() {
        placeController.goTo(new DjPlace());
    }

    @Override
    public void reIndex() {
        // TODO Auto-generated method stub
        
    }
}

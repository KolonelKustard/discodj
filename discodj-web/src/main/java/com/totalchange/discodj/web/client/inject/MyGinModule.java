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
package com.totalchange.discodj.web.client.inject;

import net.customware.gwt.dispatch.client.DefaultExceptionHandler;
import net.customware.gwt.dispatch.client.gin.StandardDispatchModule;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.totalchange.discodj.web.client.AppActivityMapper;
import com.totalchange.discodj.web.client.AppPlaceHistoryMapper;
import com.totalchange.discodj.web.client.activities.DjActivity;
import com.totalchange.discodj.web.client.activities.HomeActivity;
import com.totalchange.discodj.web.client.activities.PlayerActivity;
import com.totalchange.discodj.web.client.places.HomePlace;
import com.totalchange.discodj.web.client.views.DjView;
import com.totalchange.discodj.web.client.views.ErrorView;
import com.totalchange.discodj.web.client.views.HomeView;
import com.totalchange.discodj.web.client.views.PlayerView;
import com.totalchange.discodj.web.client.views.LoadingView;
import com.totalchange.discodj.web.client.views.impl.DjViewImpl;
import com.totalchange.discodj.web.client.views.impl.ErrorViewImpl;
import com.totalchange.discodj.web.client.views.impl.HomeViewImpl;
import com.totalchange.discodj.web.client.views.impl.PlayerViewImpl;
import com.totalchange.discodj.web.client.views.impl.LoadingViewImpl;

public class MyGinModule extends AbstractGinModule {
    @Override
    protected void configure() {
        // Makes sure the event bus is a singleton
        bind(EventBus.class).to(SimpleEventBus.class).asEagerSingleton();

        // Our activity mapper
        bind(ActivityMapper.class).to(AppActivityMapper.class);

        // The views
        bind(ErrorView.class).to(ErrorViewImpl.class);
        bind(LoadingView.class).to(LoadingViewImpl.class);
        bind(HomeView.class).to(HomeViewImpl.class);
        bind(PlayerView.class).to(PlayerViewImpl.class);
        bind(DjView.class).to(DjViewImpl.class);

        // Presenters/Activites
        bind(HomeView.Presenter.class).to(HomeActivity.class);
        bind(PlayerView.Presenter.class).to(PlayerActivity.class);
        bind(DjView.Presenter.class).to(DjActivity.class);

        // GWT Dispatch which provides the command pattern
        install(new StandardDispatchModule(DefaultExceptionHandler.class));
    }

    @Provides
    public PlaceHistoryHandler getPlaceHistoryHandler(
            AppPlaceHistoryMapper historyMapper,
            PlaceController placeController, EventBus bus) {
        PlaceHistoryHandler handler = new PlaceHistoryHandler(historyMapper);
        handler.register(placeController, bus, new HomePlace());
        return handler;
    }

    @Provides
    public ActivityManager getActivityManager(ActivityMapper mapper,
            EventBus bus) {
        return new ActivityManager(mapper, bus);
    }

    @Provides
    @Singleton
    public PlaceController getPlaceController(EventBus bus) {
        return new PlaceController(bus);
    }
}

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
package com.totalchange.discodj.web.client;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.totalchange.discodj.web.client.inject.MyGinInjector;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Jizz implements EntryPoint {
    private static MyGinInjector injector = GWT.create(MyGinInjector.class);

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        // ActivityManager needs a one widget panel so we create one.
        SimplePanel startPanel = new SimplePanel();

        // Start the activity manager.
        ActivityManager activityManager = injector.getActivityManager();
        activityManager.setDisplay(startPanel);

        // Start the PlaceHistoryManager
        PlaceHistoryHandler historyHandler = injector
                .getRegisteredPlaceHistoryHandler();

        RootLayoutPanel.get().add(startPanel);

        // This call kicks off for new screens by kicking off the event we need
        // to get everything started.
        historyHandler.handleCurrentHistory();
    }
}

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

import javax.inject.Inject;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.totalchange.discodj.web.client.activities.HomeActivity;
import com.totalchange.discodj.web.client.activities.PlayerActivity;
import com.totalchange.discodj.web.client.places.HomePlace;
import com.totalchange.discodj.web.client.places.PlayerPlace;

public class AppActivityMapper implements ActivityMapper {
    @Inject
    PlayerActivity initJizzActivity;

    @Inject
    HomeActivity homeActivity;

    @Override
    public Activity getActivity(Place place) {
        if (place instanceof PlayerPlace) {
            return initJizzActivity;
        }
        if (place instanceof HomePlace) {
            return homeActivity;
        }
        return null;
    }
}

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

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.totalchange.discodj.web.client.error.ErrorHandler;
import com.totalchange.discodj.web.client.loading.LoadingHandler;

/**
 * The GIN interface you call to get instances of classes.
 * @see <a href="http://code.google.com/p/google-gin/wiki/GinTutorial">Gin tutorial</a>
 * @see <a href="http://www.canoo.com/blog/2011/04/05/gwt-dependency-injection-recipes-using-gin/">Excellent GIN tutorial</a>
 * @see <a href="http://www.youtube.com/watch?v=hBVJbzAagfs">Gin and Guice Google I/O talk</a>
 * @author dparish
 *
 */
@GinModules(MyGinModule.class)
public interface MyGinInjector extends Ginjector {
	PlaceController getPlaceController();
	ActivityMapper getActivityMapper();
	ActivityManager getActivityManager();
	PlaceHistoryHandler getRegisteredPlaceHistoryHandler();
	ErrorHandler getErrorHandler();
	LoadingHandler getLoadingHandler();
}

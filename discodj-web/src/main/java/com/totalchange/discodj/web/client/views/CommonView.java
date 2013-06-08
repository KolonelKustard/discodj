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
package com.totalchange.discodj.web.client.views;

import com.google.gwt.user.client.ui.Widget;

public interface CommonView {
    /**
     * Called so that the presenter can add the view to a container
     * 
     * @return the view as a widget
     */
    Widget asWidget();

    /**
     * The call that renders the view for instance, after you get the data from
     * a server, you call render on the view
     */
    public void render();
}

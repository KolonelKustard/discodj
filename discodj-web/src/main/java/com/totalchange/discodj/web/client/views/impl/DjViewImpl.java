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
package com.totalchange.discodj.web.client.views.impl;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.totalchange.discodj.web.client.views.DjView;

public class DjViewImpl extends Composite implements DjView {
    interface DjViewUiBinder extends UiBinder<Widget, DjViewImpl> {
    }

    private static DjViewUiBinder uiBinder = GWT.create(DjViewUiBinder.class);

    private Presenter presenter;

    @UiField
    AbsolutePanel boundaryPanel;

    @UiField
    VerticalPanel searchPanel;

    @UiField
    VerticalPanel resultsPanel;

    @UiField
    VerticalPanel playlistPanel;

    public DjViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        initDnd();
    }

    private void initDnd() {
        PickupDragController songDragController = new PickupDragController(
                boundaryPanel, false);
        songDragController.setBehaviorMultipleSelection(false);

        VerticalPanelDropController playlistDropController = new VerticalPanelDropController(
                playlistPanel);
        songDragController.registerDropController(playlistDropController);

        int count = 0;
        for (int row = 1; row <= 4; row++) {
            // initialize a widget
            HTML widget = new HTML("Draggable&nbsp;#" + ++count);
            widget.addStyleName("songWidget");
            widget.setHeight(Random.nextInt(4) + 2 + "em");
            resultsPanel.add(widget);

            // make the widget draggable
            songDragController.makeDraggable(widget);
        }
    }

    @Override
    public void render() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }
}

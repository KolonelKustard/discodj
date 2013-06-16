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

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.totalchange.discodj.web.shared.player.Media;

class MediaWidget extends Composite {
    interface MediaWidgetUiBinder extends UiBinder<Widget, MediaWidget> {
    }

    private static MediaWidgetUiBinder uiBinder = GWT
            .create(MediaWidgetUiBinder.class);

    @UiField
    Label artistLabel;

    @UiField
    Label titleLabel;

    private Media media;

    public MediaWidget() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public MediaWidget(Media media) {
        this();
        setMedia(media);
    }

    private void populateFromMedia() {
        artistLabel.setText(media.getArtist());
        titleLabel.setText(media.getTitle());
    }
    
    @UiHandler({"artistLabel", "titleLabel", "byLabel"})
    void preventMouseDownEvent(MouseDownEvent ev) {
        ev.preventDefault();
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
        populateFromMedia();
    }
}

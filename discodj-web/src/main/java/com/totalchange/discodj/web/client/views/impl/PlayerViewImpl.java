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
import com.google.gwt.event.dom.client.EndedEvent;
import com.google.gwt.media.client.Audio;
import com.google.gwt.media.client.Video;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.totalchange.discodj.web.client.views.PlayerView;

public class PlayerViewImpl extends Composite implements PlayerView {
    interface PlayerViewUiBinder extends UiBinder<Widget, PlayerViewImpl> {
    }

    private static PlayerViewUiBinder uiBinder = GWT
            .create(PlayerViewUiBinder.class);

    private Presenter presenter;

    @UiField
    Audio audio;

    @UiField
    Video video;

    public PlayerViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiFactory
    Audio createAudio() {
        return Audio.createIfSupported();
    }

    @UiFactory
    Video createVideo() {
        return Video.createIfSupported();
    }

    @UiHandler("audio")
    void audioEnded(EndedEvent ev) {
        presenter.finishedPlayingCurrent();
    }

    @UiHandler("video")
    void videoEnded(EndedEvent ev) {
        presenter.finishedPlayingCurrent();
    }

    @Override
    public void render() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void playVideo(String videoUrl) {
        video.setSrc(videoUrl);
        video.play();
    }

    @Override
    public void playAudio(String audioUrl) {
        audio.setSrc(audioUrl);
        audio.play();
    }
}

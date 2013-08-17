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
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
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

    @UiField
    HTMLPanel infoPanel;

    @UiField
    HTMLPanel requestedByPanel;

    @UiField
    InlineLabel requestedByLabel;

    @UiField
    HTMLPanel titlePanel;

    @UiField
    InlineLabel titleLabel;

    @UiField
    HTMLPanel artistPanel;

    @UiField
    InlineLabel artistLabel;

    public PlayerViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    private void showOrHideInfoPanelBasedOnContent() {
        boolean shouldBeVisible = requestedByPanel.isVisible()
                || titlePanel.isVisible() || artistPanel.isVisible();

        if (infoPanel.isVisible() != shouldBeVisible) {
            infoPanel.setVisible(shouldBeVisible);
        }
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
        audio.setVisible(false);
        video.setVisible(false);
        presenter.finishedPlayingCurrent();
    }

    @UiHandler("video")
    void videoEnded(EndedEvent ev) {
        audio.setVisible(false);
        video.setVisible(false);
        presenter.finishedPlayingCurrent();
    }

    @Override
    public void render() {
        // Nothing to be done
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void playVideo(String videoUrl) {
        audio.setVisible(false);
        video.setSrc(videoUrl);
        video.setVisible(true);
        video.play();
    }

    @Override
    public void playAudio(String audioUrl) {
        video.setVisible(false);
        audio.setSrc(audioUrl);
        audio.setVisible(true);
        audio.play();
    }

    @Override
    public void setNowPlayingTitle(String title) {
        if (title != null && title.length() > 0) {
            titleLabel.setText(title);
            titlePanel.setVisible(true);
        } else {
            titlePanel.setVisible(false);
            titleLabel.setText("");
        }

        showOrHideInfoPanelBasedOnContent();
    }

    @Override
    public void setNowPlayingArtist(String artist) {
        if (artist != null && artist.length() > 0) {
            artistLabel.setText(artist);
            artistPanel.setVisible(true);
        } else {
            artistPanel.setVisible(false);
            artistLabel.setText("");
        }

        showOrHideInfoPanelBasedOnContent();
    }

    @Override
    public void setNowPlayingRequestedBy(String requestedBy) {
        if (requestedBy != null && requestedBy.length() > 0) {
            requestedByLabel.setText(requestedBy);
            requestedByPanel.setVisible(true);
        } else {
            requestedByPanel.setVisible(false);
            requestedByLabel.setText("");
        }

        showOrHideInfoPanelBasedOnContent();
    }
}

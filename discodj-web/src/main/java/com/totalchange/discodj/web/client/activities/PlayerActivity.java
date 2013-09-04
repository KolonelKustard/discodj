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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.totalchange.discodj.web.client.error.ErrorHandler;
import com.totalchange.discodj.web.client.views.PlayerView;
import com.totalchange.discodj.web.shared.player.GetNextFromPlaylistAction;
import com.totalchange.discodj.web.shared.player.GetNextFromPlaylistResult;
import com.totalchange.discodj.web.shared.player.GetNextFromPlaylistResult.MediaType;

public class PlayerActivity extends AbstractActivity implements
        PlayerView.Presenter {
    private static final int STATUS_CHECK_INTERVAL_MS = 1000;

    private static final Logger logger = Logger.getLogger(PlayerActivity.class
            .getName());

    private PlayerView playerView;
    private DispatchAsync dispatch;
    private ErrorHandler errorHandler;

    private boolean requestForNextInProgress = false;

    @Inject
    public PlayerActivity(PlayerView playerView, DispatchAsync dispatch,
            ErrorHandler errorHandler) {
        this.playerView = playerView;
        this.dispatch = dispatch;
        this.errorHandler = errorHandler;

        this.playerView.setPresenter(this);
    }

    private void playNext(GetNextFromPlaylistResult next) {
        if (next.isQueueEmpty()) {
            logger.fine("Queue is empty - nothing to play");
            return;
        }

        if (next.getType() == MediaType.Video) {
            logger.fine("Playing next audio " + next.getUrl());
            playerView.playVideo(next.getUrl());
        } else {
            logger.fine("Playing next video " + next.getUrl());
            playerView.playAudio(next.getUrl());
        }

        playerView.setNowPlayingTitle(next.getTitle());
        playerView.setNowPlayingArtist(next.getArtist());
        playerView.setNowPlayingRequestedBy(next.getRequestedBy());
    }

    private void loadNext() {
        logger.finer("Loading next media");
        requestForNextInProgress = true;
        dispatch.execute(new GetNextFromPlaylistAction(),
                new AsyncCallback<GetNextFromPlaylistResult>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        requestForNextInProgress = false;
                        errorHandler.loadingError(caught);
                    }

                    @Override
                    public void onSuccess(GetNextFromPlaylistResult result) {
                        requestForNextInProgress = false;
                        logger.finer("Got next media");
                        playNext(result);
                    }
                });
    }

    private void checkStatus() {
        logger.finest("Checking status");

        if (!playerView.isSomethingPlaying() && !requestForNextInProgress) {
            logger.fine("Nothing playing and no request in progress");
            loadNext();
        }

        logger.finest("Status checked");
    }

    @Override
    public void start(AcceptsOneWidget container, EventBus eventBus) {
        logger.finer("Starting up InitJizzActivity");
        container.setWidget(playerView.asWidget());
        loadNext();
        logger.finer("Finished starting up InitJizzActivity");
    }

    @Override
    public void onStop() {
        logger.finer("Stopping");
        logger.finer("Stopped");
    }

    @Override
    public void finishedPlayingCurrent() {
        loadNext();
    }
}

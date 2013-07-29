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
package com.totalchange.discodj.web.server.player;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.inject.Inject;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.totalchange.discodj.media.Media;
import com.totalchange.discodj.queue.PlaylistQueue;
import com.totalchange.discodj.web.server.MediaServlet;
import com.totalchange.discodj.web.shared.player.GetNextFromPlaylistAction;
import com.totalchange.discodj.web.shared.player.GetNextFromPlaylistResult;
import com.totalchange.discodj.web.shared.player.GetNextFromPlaylistResult.MediaType;

public class GetNextFromPlaylistHandler implements
        ActionHandler<GetNextFromPlaylistAction, GetNextFromPlaylistResult> {
    private static final String MEDIA_SERVLET_URL_PREFIX = MediaServlet.PATH
            + "?" + MediaServlet.PARAM_ID + "=";
    private static final String URL_ENCODING = StandardCharsets.UTF_8.name();

    private static final Logger logger = LoggerFactory
            .getLogger(GetNextFromPlaylistHandler.class);

    private PlaylistQueue playlistQueue;

    @Inject
    public GetNextFromPlaylistHandler(PlaylistQueue playlistQueue) {
        this.playlistQueue = playlistQueue;
    }

    @Override
    public Class<GetNextFromPlaylistAction> getActionType() {
        return GetNextFromPlaylistAction.class;
    }

    @Override
    public GetNextFromPlaylistResult execute(GetNextFromPlaylistAction action,
            ExecutionContext context) throws DispatchException {
        logger.trace("Fetching next song to play from playlist");
        GetNextFromPlaylistResult result = new GetNextFromPlaylistResult();

        Media media = playlistQueue.pop();

        if (media == null) {
            result.setQueueEmpty(true);
            return result;
        }
        result.setQueueEmpty(false);

        result.setType(MediaType.Audio);
        if (media.getId().toLowerCase().endsWith("mp4")) {
            // TODO Improve crude type detection
            result.setType(MediaType.Video);
        }

        try {
            result.setUrl(MEDIA_SERVLET_URL_PREFIX
                    + URLEncoder.encode(media.getId(), URL_ENCODING));
        } catch (UnsupportedEncodingException ueEx) {
            throw new RuntimeException(ueEx);
        }
        result.setArtist(media.getArtist());
        result.setTitle(media.getTitle());
        result.setRequestedBy(media.getRequestedBy());

        logger.trace("Returning next song {}", result);
        return result;
    }

    @Override
    public void rollback(GetNextFromPlaylistAction action,
            GetNextFromPlaylistResult result, ExecutionContext context)
            throws DispatchException {
        logger.warn("Calls to GetNextFromPlaylistHandler#rollback do nothing");
    }
}

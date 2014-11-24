package com.totalchange.discodj.web.server.dj;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.totalchange.discodj.media.Media;
import com.totalchange.discodj.queue.PlaylistQueue;
import com.totalchange.discodj.web.shared.dj.DjMedia;
import com.totalchange.discodj.web.shared.dj.StatusAction;
import com.totalchange.discodj.web.shared.dj.StatusResult;
import com.totalchange.discodj.ws.search.SearchResource;

@Path("status")
public class StatusHandler {
    private static final Logger logger = LoggerFactory
            .getLogger(StatusHandler.class);

    private PlaylistQueue queue;

    @Inject
    public StatusHandler(PlaylistQueue queue) {
        this.queue = queue;
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public StatusResult execute(StatusAction action) {
        logger.trace("Executing status update");

        StatusResult result = new StatusResult();

        Media nowPlaying = queue.getLastPopped();
        result.setNowPlaying(SearchResource.copyMedia(nowPlaying, null));

        List<Media> playlist = queue.getPlaylist();
        List<DjMedia> copied = new ArrayList<>(playlist.size());
        for (Media media : playlist) {
            copied.add(SearchResource.copyMedia(media, null));
        }
        result.setPlaylist(copied);

        logger.trace("Executed status update and returning {}", result);
        return result;
    }
}

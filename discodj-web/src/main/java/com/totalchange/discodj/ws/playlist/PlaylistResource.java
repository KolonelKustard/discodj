package com.totalchange.discodj.ws.playlist;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.totalchange.discodj.media.Media;
import com.totalchange.discodj.queue.PlaylistQueue;
import com.totalchange.discodj.web.shared.dj.DjMedia;
import com.totalchange.discodj.ws.search.SearchResource;

@Singleton
@Path("playlist")
public class PlaylistResource {
    private static final Logger logger = LoggerFactory
            .getLogger(PlaylistResource.class);

    private PlaylistQueue queue;

    @Inject
    public PlaylistResource(PlaylistQueue queue) {
        this.queue = queue;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public PlaylistResult getPlaylist() {
        logger.trace("Fetching current playlist");

        PlaylistResult result = new PlaylistResult();

        Media nowPlaying = queue.getLastPopped();
        result.setNowPlaying(SearchResource.copyMedia(nowPlaying, null));

        List<Media> playlist = queue.getPlaylist();
        List<DjMedia> copied = new ArrayList<>(playlist.size());
        for (Media media : playlist) {
            copied.add(SearchResource.copyMedia(media, null));
        }
        result.setPlaylist(copied);

        logger.trace("Built playlist {}", result);
        return result;
    }

    @PUT @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public PlaylistResult addToPlaylist(PlaylistAdd toAdd) {
        queue.push(toAdd.getId());
        return getPlaylist();
    }
}

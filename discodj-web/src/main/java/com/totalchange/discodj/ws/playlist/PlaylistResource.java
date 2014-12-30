package com.totalchange.discodj.ws.playlist;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.totalchange.discodj.media.Media;
import com.totalchange.discodj.queue.PlaylistQueue;
import com.totalchange.discodj.ws.DjMedia;
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

    @GET
    @Path("add")
    public boolean addToPlaylist(@QueryParam("id") String id) {
        queue.push(id);
        return true;
    }

    @GET
    @Path("moveUp")
    public boolean moveUp(@QueryParam("id") String id) {
        queue.moveUp(id);
        return true;
    }

    @GET
    @Path("moveDown")
    public boolean moveDown(@QueryParam("id") String id) {
        queue.moveDown(id);
        return true;
    }

    @GET
    @Path("next")
    @Produces(MediaType.APPLICATION_JSON)
    public PlaylistNext next() {
        logger.trace("Fetching next song to play from playlist");
        PlaylistNext result = new PlaylistNext();

        Media media = queue.pop();

        if (media == null) {
            result.setQueueEmpty(true);
            return result;
        }
        result.setQueueEmpty(false);

        result.setType(PlaylistNext.MediaType.Audio);
        if (media.getId().toLowerCase().endsWith("mp4")) {
            // TODO Improve crude type detection
            result.setType(PlaylistNext.MediaType.Video);
        }

        result.setId(media.getId());
        result.setArtist(media.getArtist());
        result.setTitle(media.getTitle());
        result.setRequestedBy(media.getRequestedBy());

        logger.trace("Returning next song {}", result);
        return result;
    }
}

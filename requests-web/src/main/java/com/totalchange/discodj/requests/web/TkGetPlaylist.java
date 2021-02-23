package com.totalchange.discodj.requests.web;

import com.totalchange.discodj.server.media.Media;
import com.totalchange.discodj.server.playlist.Playlist;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rs.RsJson;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonStructure;
import java.util.List;

public class TkGetPlaylist implements Take {
    private final Playlist playlist;

    public TkGetPlaylist(final Playlist playlist) {
        this.playlist = playlist;
    }

    @Override
    public Response act(final Request req) throws Exception {
        return new RsJson(playlistToJson(playlist));
    }

    private JsonStructure playlistToJson(final Playlist playlist) {
        final JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder()
                .add("playlist", playlistToJson(playlist.getPlaylist()));

        playlist.getNowPlaying().ifPresent(nowPlaying ->
                jsonObjectBuilder.add("nowPlaying", mediaToObject(nowPlaying)));

        return jsonObjectBuilder.build();
    }

    private JsonStructure playlistToJson(final List<Media> media) {
        final JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        media.forEach(result -> jsonArrayBuilder.add(mediaToObject(result)));
        return jsonArrayBuilder.build();
    }

    private JsonStructure mediaToObject(final Media media) {
        return Json.createObjectBuilder()
                .add("id", media.getId())
                .add("artist", media.getArtist())
                .add("title", media.getTitle())
                .build();
    }
}

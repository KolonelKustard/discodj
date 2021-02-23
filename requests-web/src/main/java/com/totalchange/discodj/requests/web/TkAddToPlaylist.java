package com.totalchange.discodj.requests.web;

import com.totalchange.discodj.server.media.Media;
import com.totalchange.discodj.server.playlist.Playlist;
import com.totalchange.discodj.server.search.SearchProvider;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rs.RsText;
import org.takes.rs.RsWithStatus;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.IOException;
import java.util.Optional;

public class TkAddToPlaylist implements Take {
    private final SearchProvider searchProvider;
    private final Playlist playlist;

    public TkAddToPlaylist(final SearchProvider searchProvider, final Playlist playlist) {
        this.searchProvider = searchProvider;
        this.playlist = playlist;
    }

    @Override
    public Response act(Request req) throws IOException {
        final JsonObject reqJson = Json.createReader(req.body()).readObject();
        final Optional<Media> toAdd = searchProvider.getMediaById(reqJson.getString("id"));

        if (toAdd.isPresent()) {
            return null;
        } else {
            return new RsWithStatus(new RsText("Media " + reqJson.getString("id") + " not found"), 404);
        }
    }
}

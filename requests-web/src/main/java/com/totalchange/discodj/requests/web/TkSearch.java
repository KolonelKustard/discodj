package com.totalchange.discodj.requests.web;

import com.totalchange.discodj.server.search.SearchProvider;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rs.RsJson;

import javax.json.Json;
import java.io.IOException;

public class TkSearch implements Take {
    private final SearchProvider searchProvider;

    public TkSearch(final SearchProvider searchProvider) {
        this.searchProvider = searchProvider;
    }

    @Override
    public Response act(Request req) throws IOException {
        return new RsJson(Json.createObjectBuilder().add("foo", "bar").build());
    }
}

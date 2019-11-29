package com.totalchange.discodj.requests.web;

import org.takes.facets.fork.FkRegex;
import org.takes.facets.fork.TkFork;
import org.takes.http.Exit;
import org.takes.http.FtBasic;

public class DiscoDjRequestsWebServer {
    public static void main(String[] args) throws Exception {
        new FtBasic(makeTkFork(), 8080).start(Exit.NEVER);
    }

    private static TkFork makeTkFork() {
        return new TkFork(new FkRegex("/.*",
                new TkResources("/META-INF/com.totalchange.discodj.requests.ui")));
    }
}

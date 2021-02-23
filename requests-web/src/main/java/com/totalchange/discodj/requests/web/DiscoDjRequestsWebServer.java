package com.totalchange.discodj.requests.web;

import com.totalchange.discodj.server.playlist.Playlist;
import com.totalchange.discodj.server.search.SearchProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.takes.facets.fork.FkMethods;
import org.takes.facets.fork.FkRegex;
import org.takes.facets.fork.TkFork;
import org.takes.http.FtBasic;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DiscoDjRequestsWebServer {
    private static final Logger logger = LoggerFactory.getLogger(DiscoDjRequestsWebServer.class);

    private final ExecutorService executor = Executors.newSingleThreadExecutor(new NamedThreadFactory("web"));
    private final FtBasic ftBasic;
    private boolean exit;

    public DiscoDjRequestsWebServer(final SearchProvider searchProvider, final Playlist playlist) throws IOException {
        this.ftBasic = new FtBasic(makeTkFork(searchProvider, playlist), 8080);
    }

    public void start() throws IOException {
        executor.execute(() -> {
            try {
                logger.info("Started requests web server");
                ftBasic.start(() -> exit);
                logger.info("Stopped requests web server");
            } catch (IOException e) {
                logger.error("Web server failed", e);
            }
        });
    }

    public void close() {
        logger.info("Shutting down");
        exit = true;
        executor.shutdown();
        try {
            executor.awaitTermination(20, TimeUnit.SECONDS);
            logger.info("Shutdown complete cleanly");
        } catch (InterruptedException ex) {
            logger.error("Shutdown didn't complete cleanly", ex);
        }
    }

    private static TkFork makeTkFork(final SearchProvider searchProvider, final Playlist playlist) {
        return new TkFork(
                new FkRegex("/search", new TkSearch(searchProvider)),
                new FkRegex("/playlist", new TkFork(
                        new FkMethods("GET", new TkGetPlaylist(playlist)),
                        new FkMethods("POST", new TkAddToPlaylist(searchProvider, playlist))
                )),
                new FkRegex("/.*", new TkResources("/META-INF/com.totalchange.discodj.requests.ui"))
        );
    }
}

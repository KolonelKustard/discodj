package com.totalchange.discodj.web.server;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.totalchange.discodj.populator.BackgroundSync;

@Singleton
@WebServlet
public final class IndexingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory
            .getLogger(IndexingServlet.class);

    private BackgroundSync backgroundSync;

    @Inject
    public IndexingServlet(BackgroundSync backgroundSync) {
        this.backgroundSync = backgroundSync;
    }

    @Override
    public void init() throws ServletException {
        super.init();
        logger.trace("Init being used to start background sync job");
        backgroundSync.start();
        logger.trace("Init finished");
    }

    @Override
    public void destroy() {
        logger.trace("Shutting down background sync job");
        backgroundSync.stop();
        logger.trace("Shut down complete");
        super.destroy();
    }
}

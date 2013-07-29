package com.totalchange.discodj.web.server.inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.customware.gwt.dispatch.server.guice.GuiceStandardDispatchServlet;

import com.google.inject.servlet.ServletModule;
import com.totalchange.discodj.web.server.IndexingServlet;
import com.totalchange.discodj.web.server.MediaServlet;

public class DispatchServletModule extends ServletModule {
    public static final String MEDIA_SERVLET_PATH = "/media";

    private static Logger logger = LoggerFactory
            .getLogger(DispatchServletModule.class);

    @Override
    protected void configureServlets() {
        logger.trace("Configuring Guice servlets");
        serve("/DiscoDj/dispatch").with(GuiceStandardDispatchServlet.class);
        serve("/indexer").with(IndexingServlet.class);
        serve(MediaServlet.PATH).with(MediaServlet.class);
        logger.trace("Configured Guice servlets");
    }
}

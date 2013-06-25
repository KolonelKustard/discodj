package com.totalchange.discodj.web.server.inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.customware.gwt.dispatch.server.guice.GuiceStandardDispatchServlet;

import com.google.inject.servlet.ServletModule;

public class DispatchServletModule extends ServletModule {
    private static Logger logger = LoggerFactory
            .getLogger(DispatchServletModule.class);

    @Override
    protected void configureServlets() {
        logger.trace("Configuring Guice servlets");
        serve("/com.totalchange.discodj.web.client.DiscoDj/dispatch").with(
                GuiceStandardDispatchServlet.class);
        logger.trace("Configured Guice servlets");
    }
}

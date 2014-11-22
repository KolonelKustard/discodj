package com.totalchange.discodj.web.server.inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class BootstrapListener extends GuiceServletContextListener {
    private static final Logger logger = LoggerFactory
            .getLogger(BootstrapListener.class);

    @Override
    protected Injector getInjector() {
        logger.trace("Creating Guice injector");
        Injector injector = Guice.createInjector(new DiscoDjModule(),
                new DiscoDjConfigurationModule(), new DispatchServletModule());
        logger.trace("Created Guice injector {}", injector);

        return injector;
    }
}

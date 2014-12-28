package com.totalchange.discodj.web.server.inject;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.totalchange.discodj.web.server.IndexingServlet;
import com.totalchange.discodj.web.server.MediaServlet;

@WebListener
public class BootstrapListener extends GuiceServletContextListener {
    private static final Logger logger = LoggerFactory
            .getLogger(BootstrapListener.class);

    private static DiscoDjModule discoDjModule;
    private static Injector injector;
    static {
        logger.trace("Creating Guice injector");
        discoDjModule = new DiscoDjModule();
        injector = Guice.createInjector(discoDjModule,
            new DiscoDjConfigurationModule(), new ServletModule() {
                @Override
                protected void configureServlets() {
                    serve("/indexer/*").with(IndexingServlet.class);
                    serve("/media/*").with(MediaServlet.class);
                }
            });
        logger.trace("Created Guice injector {}", injector);
    }

    @Override
    protected Injector getInjector() {
        return getGuiceInjector();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        try {
            discoDjModule.close();
        } catch (Exception ex) {
            logger.warn("Failed to close DiscoDjModule", ex);
        }
        super.contextDestroyed(servletContextEvent);
    }

    public static Injector getGuiceInjector() {
        return injector;
    }
}

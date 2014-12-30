package com.totalchange.discodj.web.server;

import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.totalchange.discodj.web.server.inject.BootstrapListener;

@ApplicationPath("resources")
public class DiscoDjApplication extends ResourceConfig {
    private static final Logger logger = LoggerFactory.getLogger(DiscoDjApplication.class);

    @Inject
    public DiscoDjApplication(ServiceLocator locator) {
        logger.trace("Setting up WS endpoints {}", locator);
        GuiceBridge.getGuiceBridge().initializeGuiceBridge(locator);
        GuiceIntoHK2Bridge guiceBridge = locator.getService(GuiceIntoHK2Bridge.class);
        guiceBridge.bridgeGuiceInjector(BootstrapListener.getGuiceInjector());
        packages("com.totalchange.discodj.ws");
        logger.trace("Finished setting up WS endpoints");
    }
}

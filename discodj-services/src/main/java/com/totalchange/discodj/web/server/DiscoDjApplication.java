/**
 * Copyright 2015 Ralph Jones
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.totalchange.discodj.web.server;

import java.io.IOException;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.totalchange.discodj.web.server.inject.DiscoDjConfigurationModule;
import com.totalchange.discodj.web.server.inject.DiscoDjModule;

public class DiscoDjApplication extends ResourceConfig {
    private static final Logger logger = LoggerFactory
            .getLogger(DiscoDjApplication.class);

    public static void main(String[] args) throws IOException {
        logger.trace("Starting up DiscoDJ");
        ResourceConfig rc = new ResourceConfig()
                .packages("com.totalchange.discodj.ws");
        rc.register(new ContainerLifecycleListener() {
            private DiscoDjModule discoDjModule;
            private Injector injector;

            @Override
            public void onStartup(Container container) {
                logger.trace("Creating Guice injector");
                discoDjModule = new DiscoDjModule();
                injector = Guice.createInjector(discoDjModule,
                        new DiscoDjConfigurationModule());

                logger.trace("Setting up Guice bridge {}", container);
                ServiceLocator locator = container.getApplicationHandler()
                        .getServiceLocator();
                GuiceBridge.getGuiceBridge().initializeGuiceBridge(locator);
                GuiceIntoHK2Bridge guiceBridge = locator
                        .getService(GuiceIntoHK2Bridge.class);
                guiceBridge.bridgeGuiceInjector(injector);
                logger.trace("Finished setting up Guice bridge {}", container);
            }

            @Override
            public void onShutdown(Container container) {
                try {
                    discoDjModule.close();
                } catch (Exception ex) {
                    logger.warn("Failed to close DiscoDjModule", ex);
                }
            }

            @Override
            public void onReload(Container container) {
            }
        });
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(
                URI.create("http://0.0.0.0:58008/discodj"), rc);
        logger.info("Starting DiscoDJ on server {}", server);
        server.start();
        logger.info("Started");
        System.out.println("Press any key to stop DiscoDJ");
        System.in.read();
        logger.info("Shutting down");
        server.shutdown();
        logger.info("Ended");
    }
}

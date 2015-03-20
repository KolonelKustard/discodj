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

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.totalchange.discodj.populator.BackgroundSync;
import com.totalchange.discodj.web.server.inject.DiscoDjConfigurationModule;
import com.totalchange.discodj.web.server.inject.DiscoDjModule;

class DiscoDjContainerLifecycleListener implements ContainerLifecycleListener {
    private static final Logger logger = LoggerFactory
            .getLogger(DiscoDjContainerLifecycleListener.class);

    private DiscoDjModule discoDjModule;
    private Injector injector;
    private BackgroundSync backgroundSync;

    @Override
    public void onStartup(Container container) {
        logger.trace("Creating Guice injector");
        discoDjModule = new DiscoDjModule();
        injector = Guice.createInjector(discoDjModule,
                new DiscoDjConfigurationModule());
        logger.trace("Created Guice injector");

        logger.trace("Setting up Guice bridge {}", container);
        ServiceLocator locator = container.getApplicationHandler()
                .getServiceLocator();
        GuiceBridge.getGuiceBridge().initializeGuiceBridge(locator);
        GuiceIntoHK2Bridge guiceBridge = locator
                .getService(GuiceIntoHK2Bridge.class);
        guiceBridge.bridgeGuiceInjector(injector);
        logger.trace("Finished setting up Guice bridge {}", container);

        logger.trace("Starting up catalogue synchroniser");
        backgroundSync = injector.getInstance(BackgroundSync.class);
        backgroundSync.start();
        logger.trace("Started up catalogue synchroniser");
    }

    @Override
    public void onShutdown(Container container) {
        if (backgroundSync != null) {
            try {
                logger.trace("Stopping catalogue synchroniser");
                backgroundSync.stop();
                backgroundSync = null;
                logger.trace("Stopped catalogue synchroniser");
            } catch (Exception ex) {
                logger.warn("Failed to stop catalogue synchroniser", ex);
            }
        }

        if (discoDjModule != null) {
            try {
                logger.trace("Closing disco dj module");
                discoDjModule.close();
                discoDjModule = null;
                logger.trace("Closed disco dj module");
            } catch (Exception ex) {
                logger.warn("Failed to close DiscoDjModule", ex);
            }
        }
    }

    @Override
    public void onReload(Container container) {
    }
}

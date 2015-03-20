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

import java.net.URI;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscoDjApplication implements Daemon {
    private static final Logger logger = LoggerFactory
            .getLogger(DiscoDjApplication.class);

    private HttpServer server;

    @Override
    public void init(DaemonContext context) throws DaemonInitException,
            Exception {
    }

    @Override
    public void start() throws Exception {
        logger.trace("Starting up DiscoDJ");

        ResourceConfig rc = new ResourceConfig()
                .packages("com.totalchange.discodj.ws");
        rc.register(new DiscoDjContainerLifecycleListener());

        server = GrizzlyHttpServerFactory.createHttpServer(
                URI.create("http://0.0.0.0:58008/discodj/resources"), rc);

        logger.info("Starting DiscoDJ on server {}", server);
        server.start();
        logger.info("Started");
    }

    @Override
    public void stop() throws Exception {
        logger.info("Stopping DiscoDJ");
        if (server != null) {
            logger.trace("Server is running so shutting it down");
            server.shutdown();
            server = null;
        }
        logger.info("DiscoDJ Stopped");
    }

    @Override
    public void destroy() {
    }

    public static void main(String[] args) throws Exception {
        final DiscoDjApplication app = new DiscoDjApplication();
        app.start();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                logger.trace("Shutdown hook being executed");
                try {
                    app.stop();
                } catch (Exception ex) {
                    logger.error("Failed to shutdown cleanly in shutdown hook",
                            ex);
                }
            }
        });

        System.out.println("Press return key to stop DiscoDJ");
        System.in.read();

        app.stop();
    }
}

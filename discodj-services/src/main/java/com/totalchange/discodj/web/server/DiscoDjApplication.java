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

import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscoDjApplication {
    private static final Logger logger = LoggerFactory
            .getLogger(DiscoDjApplication.class);

    private HttpServer server;

    public void init(String[] context) {
        logger.trace("Init-ing DiscoDJ");
        DiscoDjContainerLifecycleListener lifecycleListener = new DiscoDjContainerLifecycleListener();
        ResourceConfig rc = new ResourceConfig();
        rc.packages("com.totalchange.discodj.ws");
        rc.register(lifecycleListener);
        server = GrizzlyHttpServerFactory.createHttpServer(
                URI.create("http://0.0.0.0:58008/discodj/resources/api"), rc);

        HttpHandler mediaHandler = lifecycleListener.getInjector().getInstance(
                DiscoDjMediaHttpHandler.class);
        server.getServerConfiguration().addHttpHandler(mediaHandler,
                "/discodj/resources/media");
    }

    public void start() throws IOException {
        logger.info("Starting DiscoDJ on server {}", server);
        server.start();
        logger.info("Started");
    }

    public void stop() {
        logger.info("Stopping DiscoDJ");
        if (server != null) {
            logger.trace("Server exists so shutting it down");
            server.shutdown();
        }
        logger.info("DiscoDJ Stopped");
    }

    public void destroy() {
        server = null;
    }

    public static void main(String[] args) throws Exception {
        final DiscoDjApplication app = new DiscoDjApplication();
        app.init(args);
        app.start();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                logger.trace("Shutdown hook being executed");
                app.stop();
                app.destroy();
            }
        });

        System.out.println("Press return key to stop DiscoDJ");
        System.in.read();

        app.stop();
        app.destroy();
    }
}

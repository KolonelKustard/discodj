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
package com.totalchange.discodj.web.server.inject;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

@WebListener
public class BootstrapListener extends GuiceServletContextListener {
    private static final Logger logger = LoggerFactory
            .getLogger(BootstrapListener.class);

    private static DiscoDjModule discoDjModule;
    private static Injector injector;
    static {
        logger.trace("Creating Guice injector");
        discoDjModule = new DiscoDjModule();
        injector = Guice.createInjector(discoDjModule, new DiscoDjConfigurationModule());
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

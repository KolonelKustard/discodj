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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.totalchange.discodj.populator.BackgroundSync;

@WebServlet(loadOnStartup = 1, value="/indexer")
public final class IndexingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory
            .getLogger(IndexingServlet.class);

    private BackgroundSync backgroundSync;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        logger.trace("Init being used to start background sync job");
        Injector injector = (Injector) config.getServletContext().getAttribute(Injector.class.getName());
        backgroundSync = injector.getInstance(BackgroundSync.class);
        backgroundSync.start();
        logger.trace("Init finished");
    }

    @Override
    public void destroy() {
        logger.trace("Shutting down background sync job");
        backgroundSync.stop();
        logger.trace("Shut down complete");
        super.destroy();
    }
}

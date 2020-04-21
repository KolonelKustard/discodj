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
package com.totalchange.discodj.catalogue;

import com.totalchange.discodj.server.media.MediaSource;
import com.totalchange.discodj.server.search.SearchProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Catalogue {
    private static final Logger logger = LoggerFactory.getLogger(Catalogue.class);

    private final ExecutorService executor = Executors.newSingleThreadExecutor(new NamedThreadFactory("catalogue"));
    private final CatalogueSource[] catalogueSources;

    public Catalogue(MediaSource[] mediaSources, SearchProvider searchProvider) {
        this.catalogueSources = new CatalogueSource[mediaSources.length];
        for (int i = 0; i < mediaSources.length; i++) {
            this.catalogueSources[i] = new CatalogueSource(executor, mediaSources[i], searchProvider);
        }
    }

    public CatalogueSource[] getCatalogueSources() {
        return this.catalogueSources;
    }

    public void close() {
        try {
            logger.info("Shutting down");
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
            logger.info("Shutdown complete cleanly");
        } catch (InterruptedException ex) {
            logger.info("Shutdown didn't complete cleanly", ex);
        }
    }
}

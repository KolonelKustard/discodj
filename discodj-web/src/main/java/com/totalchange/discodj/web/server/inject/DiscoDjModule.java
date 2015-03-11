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

import java.io.File;
import java.io.IOException;

import javax.inject.Named;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.totalchange.discodj.catalogue.Catalogue;
import com.totalchange.discodj.queue.PlaylistQueue;
import com.totalchange.discodj.search.SearchProvider;
import com.totalchange.discodj.search.lucene.LuceneSearchProvider;
import com.totalchange.discodj.xuggler.FileTikaCatalogueImpl;

public class DiscoDjModule extends AbstractModule implements AutoCloseable {
    private static Logger logger = LoggerFactory.getLogger(DiscoDjModule.class);

    private Directory directory = null;

    @Override
    protected void configure() {
        logger.trace("Configuring disco dj Guice bindings");
        bind(SearchProvider.class).to(LuceneSearchProvider.class);
        bind(Catalogue.class).to(FileTikaCatalogueImpl.class);
        bind(PlaylistQueue.class);
        logger.trace("Configured disco dj Guice bindings");
    }

    @Provides
    Directory provideLuceneDirectory(
            @Named(DiscoDjConfigurationModule.SEARCH_INDEX_ROOT) String searchRoot)
            throws IOException {
        if (directory == null) {
            logger.trace("Creating Lucene Directory instance");
            synchronized (this) {
                if (directory == null) {
                    File home = new File(searchRoot);
                    directory = FSDirectory.open(home.toPath());
                }
            }
            logger.trace("Created Lucene Directory instance {}", directory);
        }
        return directory;
    }

    @Override
    public void close() throws IOException {
        if (directory != null) {
            logger.trace("Shutting down Lucene Directory instance {}",
                    directory);
            directory.close();
        }
    }
}

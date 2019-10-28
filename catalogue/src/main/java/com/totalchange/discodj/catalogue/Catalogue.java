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

import javax.inject.Inject;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Catalogue {
    private final Executor executor = Executors.newSingleThreadExecutor(new NamedThreadFactory("catalogue"));
    private final CatalogueSource[] catalogueSources;

    @Inject
    public Catalogue(MediaSource[] mediaSources, SearchProvider searchProvider) {
        this.catalogueSources = new CatalogueSource[mediaSources.length];
        for (int i = 0; i < mediaSources.length; i++) {
            this.catalogueSources[i] = new CatalogueSource(executor, mediaSources[i], searchProvider);
        }
    }
}

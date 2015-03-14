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
package com.totalchange.discodj.web.search.inject;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.totalchange.discodj.web.server.inject.DiscoDjConfigurationModule;

public class IntegrationTestModule extends AbstractModule {
    private static final File SEARCH_INDEX_ROOT = new File(
            "./target/integration-tests-search-index");

    @Override
    protected void configure() {
        FileUtils.deleteQuietly(SEARCH_INDEX_ROOT);
        SEARCH_INDEX_ROOT.mkdirs();
        try {
            bindConstant().annotatedWith(
                    Names.named(DiscoDjConfigurationModule.SEARCH_INDEX_ROOT))
                    .to(SEARCH_INDEX_ROOT.getCanonicalPath());
        } catch (IOException ioEx) {
            throw new RuntimeException(ioEx);
        }

        bindConstant().annotatedWith(
                Names.named(DiscoDjConfigurationModule.CATALOGUE_ROOT)).to(
                "./src/test/catalogue");
    }
}

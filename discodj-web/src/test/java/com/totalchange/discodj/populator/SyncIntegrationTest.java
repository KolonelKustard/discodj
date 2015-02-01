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
package com.totalchange.discodj.populator;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.totalchange.discodj.search.SearchPopulator;
import com.totalchange.discodj.search.SearchProvider;
import com.totalchange.discodj.web.search.inject.IntegrationTestInjector;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SyncIntegrationTest {
    private SyncSearchFromCatalogue syncSearchFromCatalogue;

    @Before
    public void setUp() {
        syncSearchFromCatalogue = IntegrationTestInjector.getInjector()
                .getInstance(SyncSearchFromCatalogue.class);
    }

    @AfterClass
    public static void shutdown() {
        SearchPopulator pop = IntegrationTestInjector.getInjector()
                .getInstance(SearchProvider.class).createPopulator();
        pop.deleteAll();
        pop.commit();
    }

    @Test
    public void order001_doesFullRefresh() {
        syncSearchFromCatalogue.fullRefresh();
    }

    @Test
    public void order002_doesSyncWithNoChanges() {
        syncSearchFromCatalogue.sync();
    }

    @Test
    public void order003_doesSyncAfterSomeChanges() {
        syncSearchFromCatalogue.sync();
    }
}

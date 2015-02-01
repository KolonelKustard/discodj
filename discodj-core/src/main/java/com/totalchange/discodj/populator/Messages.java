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

import java.util.ResourceBundle;

class Messages {
    private static final String BUNDLE_NAME = "com.totalchange.discodj.populator.messages"; //$NON-NLS-1$

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
            .getBundle(BUNDLE_NAME);

    private Messages() {
    }

    public static String getStatusIdle() {
        return RESOURCE_BUNDLE.getString("SyncSearchFromCatalogue.statusIdle");
    }

    public static String getInProgressException() {
        return RESOURCE_BUNDLE
                .getString("SyncSearchFromCatalogue.inProgressException");
    }

    public static String getStatusFindingChanges() {
        return RESOURCE_BUNDLE
                .getString("SyncSearchFromCatalogue.statusFindingChanges");
    }

    public static String getStatusAdding(long currentItem, long totalChanges,
            String title, String artist) {
        return RESOURCE_BUNDLE
                .getString("SyncSearchFromCatalogue.statusAdding");
    }

    public static String getStatusUpdating(long currentItem, long totalChanges,
            String title, String artist) {
        return RESOURCE_BUNDLE
                .getString("SyncSearchFromCatalogue.statusUpdating");
    }

    public static String getStatusDeleting(long currentItem, long totalChanges,
            String id) {
        return RESOURCE_BUNDLE
                .getString("SyncSearchFromCatalogue.statusDeleting");
    }

    public static String getStatusEmptyingSearch() {
        return RESOURCE_BUNDLE
                .getString("SyncSearchFromCatalogue.emptyingSearch");
    }
}

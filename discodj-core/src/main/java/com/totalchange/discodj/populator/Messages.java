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

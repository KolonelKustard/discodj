package com.totalchange.discodj.web.search.inject;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.totalchange.discodj.web.server.inject.DiscoDjModule;

public class IntegrationTestInjector {
    private static Injector instance = Guice.createInjector(
            new IntegrationTestModule(), new DiscoDjModule());;

    public static Injector getInjector() {
        return instance;
    }
}

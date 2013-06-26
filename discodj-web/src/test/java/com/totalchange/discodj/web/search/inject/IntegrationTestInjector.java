package com.totalchange.discodj.web.search.inject;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.totalchange.discodj.web.server.inject.DiscoDjModule;

public class IntegrationTestInjector {
    public static Injector makeIntegrationTestInjector() {
        return Guice.createInjector(new IntegrationTestModule(),
                new DiscoDjModule());
    }
}

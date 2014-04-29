package com.totalchange.discodj.web.search.inject;

import static org.mockito.Mockito.*;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class IntegrationTestModule extends AbstractModule {
    private static final Logger logger = LoggerFactory
            .getLogger(IntegrationTestModule.class);

    @Override
    protected void configure() {
    }

    @Provides
    ServletContext provideMockServletContext() {
        logger.trace("Making mock ServletContext");
        ServletContext sc = mock(ServletContext.class);
        when(sc.getRealPath("/WEB-INF/solr")).thenReturn(
                "./src/main/webapp/WEB-INF/solr");
        when(sc.getRealPath("/WEB-INF/catalogue")).thenReturn(
                "./src/test/catalogue");

        logger.trace("Returning mock ServletContext");
        return sc;
    }
}

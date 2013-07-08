package com.totalchange.discodj.web.search.inject;

import static org.easymock.EasyMock.*;

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
        ServletContext sc = createMock(ServletContext.class);
        expect(sc.getRealPath("/WEB-INF/solr")).andReturn(
                "./src/main/webapp/WEB-INF/solr");
        expect(sc.getRealPath("/WEB-INF/catalogue")).andReturn(
                "./src/main/test/catalogue");

        replay(sc);
        logger.trace("Returning mock ServletContext");
        return sc;
    }
}

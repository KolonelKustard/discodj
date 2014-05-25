package com.totalchange.discodj.web.search.inject;

import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.totalchange.discodj.web.server.inject.DiscoDjConfigurationModule;

public class IntegrationTestModule extends AbstractModule {
    private static final File SOLR_HOME_SRC = new File(
            "../discodj-solr/src/main/solr");
    private static final File SOLR_HOME_DEST = new File(
            "./target/integration-tests-solr-home");

    private static final Logger logger = LoggerFactory
            .getLogger(IntegrationTestModule.class);

    @Override
    protected void configure() {
        FileUtils.deleteQuietly(SOLR_HOME_DEST);
        SOLR_HOME_DEST.mkdirs();
        try {
            FileUtils.copyDirectory(SOLR_HOME_SRC, SOLR_HOME_DEST);
            bindConstant().annotatedWith(
                    Names.named(DiscoDjConfigurationModule.SOLR_HOME)).to(
                    SOLR_HOME_DEST.getCanonicalPath());
        } catch (IOException ioEx) {
            throw new RuntimeException(ioEx);
        }

        bindConstant().annotatedWith(
                Names.named(DiscoDjConfigurationModule.CATALOGUE_ROOT)).to(
                "./src/test/catalogue");
    }

    @Provides
    ServletContext provideMockServletContext() {
        logger.trace("Making mock ServletContext");
        ServletContext sc = mock(ServletContext.class);
        when(sc.getRealPath("/WEB-INF/solr")).thenReturn(
                "./src/main/webapp/WEB-INF/solr");

        logger.trace("Returning mock ServletContext");
        return sc;
    }
}

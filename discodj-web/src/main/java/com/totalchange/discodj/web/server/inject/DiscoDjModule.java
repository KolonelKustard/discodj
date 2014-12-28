package com.totalchange.discodj.web.server.inject;

import java.io.File;
import java.io.FileNotFoundException;

import javax.inject.Named;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.core.CoreContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.totalchange.discodj.catalogue.Catalogue;
import com.totalchange.discodj.queue.PlaylistQueue;
import com.totalchange.discodj.search.SearchProvider;
import com.totalchange.discodj.search.solr.SolrSearchProviderImpl;
import com.totalchange.discodj.xuggler.XugglerCatalogueImpl;

public class DiscoDjModule extends AbstractModule implements AutoCloseable {
    private static Logger logger = LoggerFactory.getLogger(DiscoDjModule.class);

    private CoreContainer coreContainer = null;
    private SolrServer solrServer = null;

    @Override
    protected void configure() {
        logger.trace("Configuring disco dj Guice bindings");
        bind(SearchProvider.class).to(SolrSearchProviderImpl.class);
        bind(Catalogue.class).to(XugglerCatalogueImpl.class);
        bind(PlaylistQueue.class);
        logger.trace("Configured disco dj Guice bindings");
    }

    @Provides
    SolrServer provideSolrServer(
            @Named(DiscoDjConfigurationModule.SOLR_HOME) String solrHome)
            throws FileNotFoundException {
        if (solrServer == null) {
            logger.trace("Creating SolrServer instance");
            synchronized (this) {
                if (solrServer == null) {
                    File home = new File(solrHome);
                    coreContainer = new CoreContainer(home.getAbsolutePath());
                    coreContainer.load();
                    solrServer = new EmbeddedSolrServer(coreContainer, "discodj");
                }
            }
            logger.trace("Created SolrServer instance {}", solrServer);
        }
        return solrServer;
    }

    @Override
    public void close() throws Exception {
        if (solrServer != null) {
            logger.trace("Shutting down SolrServer instance {}", solrServer);
            solrServer.shutdown();
        }

        if (coreContainer != null) {
            logger.trace("Shutting down CoreContainer instance {}", coreContainer);
            coreContainer.shutdown();
        }
    }
}

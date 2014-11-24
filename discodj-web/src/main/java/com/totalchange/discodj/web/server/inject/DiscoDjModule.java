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

public class DiscoDjModule extends AbstractModule {
    private static Logger logger = LoggerFactory.getLogger(DiscoDjModule.class);

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
        logger.trace("Creating SolrServer instance");
        File home = new File(solrHome);
        CoreContainer container = new CoreContainer(home.getAbsolutePath());
        container.load();
        SolrServer server = new EmbeddedSolrServer(container, "discodj");
        logger.trace("Returning SolrServer instance {}", server);
        return server;
    }
}

package com.totalchange.discodj.web.server;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.totalchange.discodj.util.CataloguePopulator;

@Singleton
public final class IndexingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory
            .getLogger(IndexingServlet.class);

    private CataloguePopulator indexer;

    @Inject
    public IndexingServlet(CataloguePopulator indexer) {
        this.indexer = indexer;
    }

    @Override
    public void init() throws ServletException {
        logger.trace("Init being used to re-index catalogue");
        indexer.run();
        logger.trace("Finito");
    }
}

package com.totalchange.discodj.search.solr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.totalchange.discodj.media.Media;
import com.totalchange.discodj.search.SearchPopulator;

public class SolrSearchPopulatorImpl implements SearchPopulator {
    private static final int COMMIT_INTERVAL = 1000;

    private static final Logger logger = LoggerFactory
            .getLogger(SolrSearchPopulatorImpl.class);

    private SolrServer solrServer;

    private List<SolrInputDocument> toAdd = new ArrayList<>(COMMIT_INTERVAL);
    private int commitCounter = 0;

    public SolrSearchPopulatorImpl(SolrServer solrServer)
            throws SolrSearchException {
        logger.trace("Creating new SOLR search populator for server {}",
                solrServer);
        this.solrServer = solrServer;

        try {
            logger.debug("Deleting all existing data in repository");
            this.solrServer.deleteByQuery("*:*");
            this.solrServer.commit();
        } catch (SolrServerException ssEx) {
            throw new SolrSearchException(ssEx);
        } catch (IOException ioEx) {
            throw new SolrSearchException(ioEx);
        }
    }

    private void pushMediaInAndCommit() throws SolrSearchException {
        try {
            solrServer.add(toAdd);
            solrServer.commit();

            toAdd.clear();
            commitCounter = 0;
        } catch (SolrServerException ssEx) {
            throw new SolrSearchException(ssEx);
        } catch (IOException ioEx) {
            throw new SolrSearchException(ioEx);
        }
    }

    @Override
    public void addMedia(Media media) throws SolrSearchException {
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField(SolrSearchProviderImpl.F_ID, media.getId());
        doc.addField(SolrSearchProviderImpl.F_ARTIST, media.getArtist());
        doc.addField(SolrSearchProviderImpl.F_ALBUM, media.getAlbum());
        doc.addField(SolrSearchProviderImpl.F_GENRE, media.getGenre());
        doc.addField(SolrSearchProviderImpl.F_YEAR, media.getYear());
        doc.addField(SolrSearchProviderImpl.F_REQUESTED_BY,
                media.getRequestedBy());
        doc.addField(SolrSearchProviderImpl.F_TITLE, media.getTitle());
        toAdd.add(doc);

        if (commitCounter >= COMMIT_INTERVAL) {
            pushMediaInAndCommit();
        }
    }

    @Override
    public void commit() throws SolrSearchException {
        pushMediaInAndCommit();
        toAdd = null;
    }
}

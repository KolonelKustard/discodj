package com.totalchange.discodj.search.solr;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.totalchange.discodj.media.Media;
import com.totalchange.discodj.search.SearchException;
import com.totalchange.discodj.search.SearchPopulator;

public class SolrSearchPopulatorImpl implements SearchPopulator {
    private static final Logger logger = LoggerFactory
            .getLogger(SolrSearchPopulatorImpl.class);

    private SolrServer solrServer;

    public SolrSearchPopulatorImpl(SolrServer solrServer) {
        logger.trace("Creating new SOLR search populator for server {}",
                solrServer);
        this.solrServer = solrServer;
    }

    private int floorYearToDecade(int year) {
        return (year / 10) * 10;
    }

    @Override
    public void addMedia(Media media) throws SolrSearchException {
        SolrInputDocument doc = new SolrInputDocument();
        doc.setField(SolrSearchProviderImpl.F_ID, media.getId());
        doc.setField(SolrSearchProviderImpl.F_ARTIST, media.getArtist());
        doc.setField(SolrSearchProviderImpl.F_ALBUM, media.getAlbum());
        doc.setField(SolrSearchProviderImpl.F_GENRE, media.getGenre());
        doc.setField(SolrSearchProviderImpl.F_YEAR, media.getYear());
        doc.setField(SolrSearchProviderImpl.F_DECADE,
                floorYearToDecade(media.getYear()));
        doc.setField(SolrSearchProviderImpl.F_REQUESTED_BY,
                media.getRequestedBy());
        doc.setField(SolrSearchProviderImpl.F_TITLE, media.getTitle());

        try {
            this.solrServer.add(doc);
        } catch (SolrServerException ssEx) {
            throw new SolrSearchException(ssEx);
        } catch (IOException ioEx) {
            throw new SolrSearchException(ioEx);
        }
    }

    @Override
    public void updateMedia(Media media) throws SearchException {
        addMedia(media);
    }

    @Override
    public void deleteMedia(String id) throws SearchException {
        try {
            this.solrServer.deleteById(id);
        } catch (SolrServerException ssEx) {
            throw new SolrSearchException(ssEx);
        } catch (IOException ioEx) {
            throw new SolrSearchException(ioEx);
        }
    }

    @Override
    public void deleteAll() throws SearchException {
        try {
            this.solrServer.deleteByQuery("*:*");
        } catch (SolrServerException ssEx) {
            throw new SolrSearchException(ssEx);
        } catch (IOException ioEx) {
            throw new SolrSearchException(ioEx);
        }
    }

    @Override
    public void commit() throws SolrSearchException {
        try {
            this.solrServer.commit();
        } catch (SolrServerException ssEx) {
            throw new SolrSearchException(ssEx);
        } catch (IOException ioEx) {
            throw new SolrSearchException(ioEx);
        }
    }
}

package com.totalchange.discodj.search.solr;

import org.apache.solr.common.SolrDocument;

import com.totalchange.discodj.media.Media;

final class SolrMediaImpl implements Media {
    private SolrDocument doc;

    SolrMediaImpl(SolrDocument doc) {
        this.doc = doc;
    }

    @Override
    public String getId() {
        return (String) doc.get(SolrSearchProviderImpl.F_ID);
    }

    @Override
    public String getArtist() {
        return (String) doc.get(SolrSearchProviderImpl.F_ARTIST);
    }

    @Override
    public String getAlbum() {
        return (String) doc.get(SolrSearchProviderImpl.F_ALBUM);
    }

    @Override
    public String getGenre() {
        return (String) doc.get(SolrSearchProviderImpl.F_GENRE);
    }

    @Override
    public int getYear() {
        return (int) doc.get(SolrSearchProviderImpl.F_YEAR);
    }

    @Override
    public String getRequestedBy() {
        return (String) doc
                .get(SolrSearchProviderImpl.F_REQUESTED_BY);
    }

    @Override
    public String getTitle() {
        return (String) doc.get(SolrSearchProviderImpl.F_TITLE);
    }

}

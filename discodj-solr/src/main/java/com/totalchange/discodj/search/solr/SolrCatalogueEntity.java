package com.totalchange.discodj.search.solr;

import java.util.Date;

import org.apache.solr.common.SolrDocument;

import com.totalchange.discodj.catalogue.Catalogue.CatalogueEntity;

class SolrCatalogueEntity implements CatalogueEntity {
    private SolrDocument solrDocument;
    
    public SolrCatalogueEntity(SolrDocument solrDocument) {
        this.solrDocument = solrDocument;
    }

    @Override
    public String getId() {
        return (String) solrDocument.get(SolrSearchProviderImpl.F_ID);
    }

    @Override
    public Date getLastModified() {
        return (Date) solrDocument.get(SolrSearchProviderImpl.F_LAST_MODIFIED);
    }
}

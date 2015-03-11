package com.totalchange.discodj.search.lucene;

import java.util.Date;

import org.apache.lucene.document.Document;

import com.totalchange.discodj.catalogue.Catalogue.CatalogueEntity;

class LuceneCatalogueEntity implements CatalogueEntity {
    private final String id;
    private final Date lastModified;

    LuceneCatalogueEntity(Document doc) {
        this.id = doc.get(LuceneSearchProvider.F_ID);
        this.lastModified = new Date(Long.parseLong(doc
                .get(LuceneSearchProvider.F_LAST_MODIFIED)));
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Date getLastModified() {
        return lastModified;
    }

}

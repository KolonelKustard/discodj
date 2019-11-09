package com.totalchange.discodj.search.lucene;

import java.util.Date;

import com.totalchange.discodj.server.media.MediaEntity;
import org.apache.lucene.document.Document;

class LuceneCatalogueEntity implements MediaEntity {
    private final String id;
    private final long lastModified;

    LuceneCatalogueEntity(Document doc) {
        this.id = doc.get(LuceneSearchProvider.F_ID);
        this.lastModified = Long.parseLong(doc.get(LuceneSearchProvider.F_LAST_MODIFIED));
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public long getLastModifiedMs() {
        return lastModified;
    }
}

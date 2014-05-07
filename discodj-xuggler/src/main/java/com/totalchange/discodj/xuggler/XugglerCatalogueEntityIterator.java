package com.totalchange.discodj.xuggler;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import com.totalchange.discodj.catalogue.Catalogue.CatalogueEntity;

class XugglerCatalogueEntityIterator implements Iterator<CatalogueEntity> {
    private class FileCatalogueEntity implements CatalogueEntity {
        private String id;
        private Date lastModified;
        
        private FileCatalogueEntity(File file) {
            try {
                this.id = file.getCanonicalPath();
            } catch (IOException ioEx) {
                throw new RuntimeException(ioEx);
            }
            this.lastModified = new Date(file.lastModified());
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

    private CatalogueEntity next;

    public XugglerCatalogueEntityIterator(File root) {
        this.next = new FileCatalogueEntity(root);
    }

    @Override
    public boolean hasNext() {
        return next != null;
    }

    @Override
    public CatalogueEntity next() {
        return next;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}

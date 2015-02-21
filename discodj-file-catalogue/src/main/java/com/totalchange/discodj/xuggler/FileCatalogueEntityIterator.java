/**
 * Copyright 2015 Ralph Jones
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.totalchange.discodj.xuggler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Queue;

import com.totalchange.discodj.catalogue.Catalogue.CatalogueEntity;

class FileCatalogueEntityIterator implements Iterator<CatalogueEntity> {
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

    private String[] allowedExtensions;
    private Queue<File> queue = new ArrayDeque<>();
    private File next;

    public FileCatalogueEntityIterator(File root,
            String... allowedExtensions) {
        this.allowedExtensions = allowedExtensions;
        queue.add(root);
        moveToNext();
    }

    @Override
    public boolean hasNext() {
        return next != null;
    }

    @Override
    public CatalogueEntity next() {
        File r = this.next;
        moveToNext();
        return new FileCatalogueEntity(r);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    private boolean hasAllowedExtension(String name) {
        if (allowedExtensions == null || allowedExtensions.length <= 0) {
            return true;
        } else {
            String lower = name.toLowerCase();
            for (String ext : allowedExtensions) {
                if (lower.endsWith(ext)) {
                    return true;
                }
            }
            return false;
        }
    }

    private boolean shouldAdd(File file) {
        return file.isFile() && hasAllowedExtension(file.getName());
    }

    private boolean shouldRecurse(File file) {
        return file.isDirectory();
    }

    private void moveToNext() {
        File file = queue.poll();

        if (file == null) {
            next = null;
        } else if (shouldAdd(file)) {
            next = file;
        } else if (shouldRecurse(file)) {
            File[] children = file.listFiles();
            Arrays.sort(children, new FileComparator());
            queue.addAll(Arrays.asList(children));
            moveToNext();
        } else {
            moveToNext();
        }
    }
}

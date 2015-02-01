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
package com.totalchange.discodj.search.solr;

import java.io.File;
import java.util.Date;

import org.apache.commons.lang.NotImplementedException;
import org.apache.solr.common.SolrDocument;

import com.totalchange.discodj.media.AbstractMedia;

final class SolrMediaImpl extends AbstractMedia {
    private SolrDocument doc;

    SolrMediaImpl(SolrDocument doc) {
        this.doc = doc;
    }

    @Override
    public String getId() {
        return (String) doc.get(SolrSearchProviderImpl.F_ID);
    }

    @Override
    public Date getLastModified() {
        return (Date) doc.get(SolrSearchProviderImpl.F_LAST_MODIFIED);
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
        return (String) doc.get(SolrSearchProviderImpl.F_REQUESTED_BY);
    }

    @Override
    public String getTitle() {
        return (String) doc.get(SolrSearchProviderImpl.F_TITLE);
    }

    @Override
    public File getFile() {
        throw new NotImplementedException("File's not available from searches"
                + ", fetch media again from catalogue");
    }

    @Override
    public String toString() {
        return "SolrMediaImpl [" + super.toString() + ", doc=" + doc + "]";
    }
}

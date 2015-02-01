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

import java.util.Iterator;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.totalchange.discodj.catalogue.Catalogue.CatalogueEntity;
import com.totalchange.discodj.search.SearchException;
import com.totalchange.discodj.search.SearchPopulator;
import com.totalchange.discodj.search.SearchProvider;
import com.totalchange.discodj.search.SearchQuery;
import com.totalchange.discodj.search.SearchResults;

@Singleton
public final class SolrSearchProviderImpl implements SearchProvider {
    private static final Logger logger = LoggerFactory
            .getLogger(SolrSearchProviderImpl.class);

    static final String F_ID = "id";
    static final String F_LAST_MODIFIED = "lastModified";
    static final String F_ARTIST = "artist";
    static final String F_ALBUM = "album";
    static final String F_GENRE = "genre";
    static final String F_YEAR = "year";
    static final String F_REQUESTED_BY = "requestedBy";
    static final String F_TITLE = "title";

    static final String F_DECADE = "decade";
    static final String F_TEXT = "text";

    private SolrServer solrServer;

    @Inject
    public SolrSearchProviderImpl(SolrServer solrServer) {
        logger.trace("Creating new SolrSearchProviderImpl for server {}",
                solrServer);
        this.solrServer = solrServer;
    }

    @Override
    public Iterator<CatalogueEntity> listAllAlphabeticallyById()
            throws SearchException {
        logger.trace("Listing all alphabetically by id");
        return new SolrCatalogueEntityIterator(solrServer);
    }

    @Override
    public SearchPopulator createPopulator() throws SolrSearchException {
        logger.trace("Creating new populator");
        return new SolrSearchPopulatorImpl(solrServer);
    }

    @Override
    public SearchResults search(SearchQuery query) throws SolrSearchException {
        SolrQuery sq = new SolrQuery();

        sq.setStart((int) query.getStart());
        sq.setRows((int) query.getRows());

        if (query.getKeywords() != null && !query.getKeywords().isEmpty()) {
            sq.setQuery(F_TEXT + ":" + query.getKeywords() + "*");
        } else {
            sq.setQuery("*:*");
        }

        sq.setFacet(true);
        sq.setFacetMinCount(1);
        sq.setFacetLimit(-1);
        sq.addFacetField(F_ARTIST, F_ALBUM, F_GENRE, F_DECADE);

        for (String id : query.getFacetIds()) {
            sq.addFilterQuery(id);
        }

        try {
            logger.debug("Performing search using query {}", sq);
            QueryResponse res = solrServer.query(sq);
            return new SolrSearchResultsImpl(res);
        } catch (SolrServerException sEx) {
            throw new SolrSearchException(sEx);
        }
    }
}

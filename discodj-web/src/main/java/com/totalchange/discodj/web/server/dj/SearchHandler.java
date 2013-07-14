/**
 * Copyright 2012 Ralph Jones.
 * 
 * This file is part of Jizz.  Jizz is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * Jizz is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Jizz.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.totalchange.discodj.web.server.dj;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.totalchange.discodj.media.Media;
import com.totalchange.discodj.search.SearchProvider;
import com.totalchange.discodj.search.SearchQuery;
import com.totalchange.discodj.search.SearchResults;
import com.totalchange.discodj.web.shared.dj.SearchAction;
import com.totalchange.discodj.web.shared.dj.SearchFacet;
import com.totalchange.discodj.web.shared.dj.SearchResult;
import com.totalchange.discodj.web.shared.dj.SearchResultMedia;

public class SearchHandler implements ActionHandler<SearchAction, SearchResult> {
    private static final int RESULTS_PER_PAGE = 30;

    private static final Logger logger = LoggerFactory
            .getLogger(SearchHandler.class);

    private SearchProvider searchProvider;

    @Inject
    public SearchHandler(SearchProvider searchProvider) {
        this.searchProvider = searchProvider;
    }

    private List<SearchFacet> copyFacets(
            List<com.totalchange.discodj.search.SearchFacet> src) {
        return null;
    }

    @Override
    public Class<SearchAction> getActionType() {
        return SearchAction.class;
    }

    @Override
    public SearchResult execute(SearchAction action, ExecutionContext context)
            throws DispatchException {
        logger.trace("DJ search underway");
        SearchResult result = new SearchResult();

        SearchQuery query = new SearchQuery();
        query.setKeywords(action.getKeywords());
        for (String id : action.getFacetIds()) {
            query.addFacetId(id);
        }
        query.setRows(RESULTS_PER_PAGE);
        query.setStart((action.getPage() - 1) * RESULTS_PER_PAGE);

        SearchResults results = searchProvider.search(query);
        result.setNumPages((int) (results.getNumFound() / RESULTS_PER_PAGE));

        result.setArtistFacets(copyFacets(results.getArtistFacets()));
        result.setAlbumFacets(copyFacets(results.getAlbumFacets()));
        result.setGenreFacets(copyFacets(results.getGenreFacets()));
        result.setDecadeFacets(copyFacets(results.getDecadeFacets()));

        List<SearchResultMedia> resultMedia = new ArrayList<>(results
                .getResults().size());
        for (Media media : results.getResults()) {
            SearchResultMedia srm = new SearchResultMedia();
            srm.setId(media.getId());
            srm.setArtist(media.getArtist());
            srm.setAlbum(media.getAlbum());
            resultMedia.add(srm);
        }
        result.setResults(resultMedia);

        logger.trace("DJ search complete with result {}", result);
        return result;
    }

    @Override
    public void rollback(SearchAction action, SearchResult result,
            ExecutionContext context) throws DispatchException {
        logger.warn("Call to roll back home action will have no effect");
    }
}

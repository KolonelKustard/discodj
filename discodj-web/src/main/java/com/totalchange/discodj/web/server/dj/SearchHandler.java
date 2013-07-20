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

    private List<SearchFacet> copyFacets(List<String> selectedFacetIds,
            List<com.totalchange.discodj.search.SearchFacet> src) {
        if (src == null) {
            return null;
        }

        List<SearchFacet> dest = new ArrayList<>(src.size());
        for (com.totalchange.discodj.search.SearchFacet srcFacet : src) {
            boolean selected = selectedFacetIds != null
                    && selectedFacetIds.contains(srcFacet.getId());

            SearchFacet destFacet = new SearchFacet(srcFacet.getId(),
                    srcFacet.getName(), srcFacet.getNumMatches(), selected);
            dest.add(destFacet);
        }
        return dest;
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
        if (action.getFacetIds() != null) {
            for (String id : action.getFacetIds()) {
                query.addFacetId(id);
            }
        }
        query.setRows(RESULTS_PER_PAGE);
        if (action.getPage() > 1) {
            query.setStart((action.getPage() - 1) * RESULTS_PER_PAGE);
        } else {
            query.setStart(0);
        }

        SearchResults results = searchProvider.search(query);

        int numPages = (int) Math.ceil((double) results.getNumFound()
                / (double) RESULTS_PER_PAGE);
        result.setNumPages(numPages);

        result.setArtistFacets(copyFacets(action.getFacetIds(),
                results.getArtistFacets()));
        result.setAlbumFacets(copyFacets(action.getFacetIds(),
                results.getAlbumFacets()));
        result.setGenreFacets(copyFacets(action.getFacetIds(),
                results.getGenreFacets()));
        result.setDecadeFacets(copyFacets(action.getFacetIds(),
                results.getDecadeFacets()));

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

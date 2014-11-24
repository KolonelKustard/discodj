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
package com.totalchange.discodj.ws.search;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.totalchange.discodj.media.Media;
import com.totalchange.discodj.queue.PlaylistQueue;
import com.totalchange.discodj.search.SearchProvider;
import com.totalchange.discodj.search.SearchQuery;
import com.totalchange.discodj.search.SearchResults;
import com.totalchange.discodj.web.shared.dj.DjMedia;

@Path("search")
public class SearchResource {
    private static final int RESULTS_PER_PAGE = 10;

    private static final Logger logger = LoggerFactory
            .getLogger(SearchResource.class);

    private SearchProvider searchProvider;
    private PlaylistQueue playlistQueue;

    @Inject
    public SearchResource(SearchProvider searchProvider,
            PlaylistQueue playlistQueue) {
        this.searchProvider = searchProvider;
        this.playlistQueue = playlistQueue;
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

    public static DjMedia copyMedia(Media media, PlaylistQueue playlistQueue) {
        if (media == null) {
            return null;
        }

        DjMedia djMedia = new DjMedia();
        djMedia.setId(media.getId());
        djMedia.setArtist(media.getArtist());
        djMedia.setTitle(media.getTitle());

        if (playlistQueue != null) {
            djMedia.setWhenCanBePlayedAgain(playlistQueue
                    .getWhenCanBePlayedAgain(media));
        }

        return djMedia;
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResult execute(SearchAction action) {
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

        List<DjMedia> resultMedia = new ArrayList<>(results.getResults().size());
        for (Media media : results.getResults()) {
            resultMedia.add(copyMedia(media, playlistQueue));
        }
        result.setResults(resultMedia);

        logger.trace("DJ search complete with result {}", result);
        return result;
    }
}

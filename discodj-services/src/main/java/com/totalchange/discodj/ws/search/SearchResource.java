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
package com.totalchange.discodj.ws.search;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.totalchange.discodj.media.Media;
import com.totalchange.discodj.queue.PlaylistQueue;
import com.totalchange.discodj.search.SearchProvider;
import com.totalchange.discodj.search.SearchQuery;
import com.totalchange.discodj.search.SearchResults;
import com.totalchange.discodj.ws.DjMedia;

@Singleton
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

            SearchFacet destFacet = new SearchFacet();
            destFacet.setId(srcFacet.getId());
            destFacet.setName(srcFacet.getName());
            destFacet.setNumMatches(srcFacet.getNumMatches());
            destFacet.setSelected(selected);

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
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResult execute(@QueryParam("q") String keywords,
            @QueryParam("facet") List<String> facetIds, @QueryParam("page") int page) {
        logger.trace("DJ search underway");
        SearchResult result = new SearchResult();

        SearchQuery query = new SearchQuery();
        query.setKeywords(keywords);
        if (facetIds != null) {
            for (String id : facetIds) {
                query.addFacetId(id);
            }
        }
        query.setRows(RESULTS_PER_PAGE);
        if (page > 1) {
            query.setStart((page - 1) * RESULTS_PER_PAGE);
            result.setPage(page);
        } else {
            query.setStart(0);
            result.setPage(1);
        }

        SearchResults results = searchProvider.search(query);

        int numPages = (int) Math.ceil((double) results.getNumFound()
                / (double) RESULTS_PER_PAGE);
        result.setNumPages(numPages);

        result.setArtistFacets(copyFacets(facetIds, results.getArtistFacets()));
        result.setAlbumFacets(copyFacets(facetIds, results.getAlbumFacets()));
        result.setGenreFacets(copyFacets(facetIds, results.getGenreFacets()));
        result.setDecadeFacets(copyFacets(facetIds, results.getDecadeFacets()));

        List<DjMedia> resultMedia = new ArrayList<>(results.getResults().size());
        for (Media media : results.getResults()) {
            resultMedia.add(copyMedia(media, playlistQueue));
        }
        result.setResults(resultMedia);

        logger.trace("DJ search complete with result {}", result);
        return result;
    }
}

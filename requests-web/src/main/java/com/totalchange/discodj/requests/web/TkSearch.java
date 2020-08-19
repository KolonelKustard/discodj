package com.totalchange.discodj.requests.web;

import com.totalchange.discodj.server.media.Media;
import com.totalchange.discodj.server.search.SearchFacet;
import com.totalchange.discodj.server.search.SearchProvider;
import com.totalchange.discodj.server.search.SearchQuery;
import com.totalchange.discodj.server.search.SearchResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.misc.Href;
import org.takes.rq.RqHref;
import org.takes.rs.RsJson;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonStructure;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TkSearch implements Take {
    private static final int PAGE_SIZE = 10;

    private static final Logger logger = LoggerFactory.getLogger(TkSearch.class);

    private final SearchProvider searchProvider;

    public TkSearch(final SearchProvider searchProvider) {
        this.searchProvider = searchProvider;
    }

    @Override
    public Response act(final Request request) throws IOException {
        final SearchResults searchResults = searchProvider.search(query(request));
        return new RsJson(resultsToJson(searchResults));
    }

    private SearchQuery query(final Request request) throws IOException {
        final Href href = new RqHref.Base(request).href();
        return new SearchQuery.Builder()
                .withKeywords(concatQuery(href.param("q")))
                .withFacetIds(facetsToList(href.param("facet")))
                .withStart(workOutTheStart(href.param("page")))
                .withRows(PAGE_SIZE)
                .build();
    }

    private String concatQuery(final Iterable<String> query) {
        final StringBuilder concatted = new StringBuilder();
        query.forEach(queryBit -> concatted.append(queryBit));
        return concatted.toString();
    }

    private List<String> facetsToList(final Iterable<String> facetIds) {
        final List<String> asList = new ArrayList<>();
        facetIds.forEach(asList::add);
        return asList;
    }

    private long workOutTheStart(final Iterable<String> page) {
        final Iterator<String> it = page.iterator();
        if (it.hasNext()) {
            try {
                final int pageInt = Integer.parseInt(it.next());
                return (pageInt - 1) * PAGE_SIZE;
            } catch (NumberFormatException ex) {
                logger.debug("Page param {} isn't a number", page, ex);
                return 0;
            }
        } else {
            return 0;
        }
    }

    private JsonStructure resultsToJson(final SearchResults searchResults) {
        return Json.createObjectBuilder()
                .add("page", 0)
                .add("numPages", searchResults.getNumFound() / PAGE_SIZE)
                .add("artistFacets", facetsToJson(searchResults.getArtistFacets()))
                .add("albumFacets", facetsToJson(searchResults.getAlbumFacets()))
                .add("genreFacets", facetsToJson(searchResults.getGenreFacets()))
                .add("decadeFacets", facetsToJson(searchResults.getDecadeFacets()))
                .add("results", resultsToJson(searchResults.getResults()))
                .build();
    }

    private JsonStructure facetsToJson(final List<SearchFacet> searchFacets) {
        final JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        searchFacets.forEach((searchFacet -> {
            jsonArrayBuilder.add(Json.createObjectBuilder()
                    .add("id", searchFacet.getId())
                    .add("name", searchFacet.getName())
                    .add("numMatches", searchFacet.getNumMatches())
                    .build());
        }));
        return jsonArrayBuilder.build();
    }

    private JsonStructure resultsToJson(final List<Media> results) {
        final JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        results.forEach((result -> {
            jsonArrayBuilder.add(Json.createObjectBuilder()
                    .add("id", result.getId())
                    .add("artist", result.getArtist())
                    .add("title", result.getTitle())
                    .build());
        }));
        return jsonArrayBuilder.build();
    }
}

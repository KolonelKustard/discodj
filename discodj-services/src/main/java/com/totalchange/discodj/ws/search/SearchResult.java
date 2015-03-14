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

import java.util.List;

import com.totalchange.discodj.ws.DjMedia;

public class SearchResult {
    private int page;
    private int numPages;
    private List<SearchFacet> artistFacets;
    private List<SearchFacet> albumFacets;
    private List<SearchFacet> genreFacets;
    private List<SearchFacet> decadeFacets;
    private List<DjMedia> results;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getNumPages() {
        return numPages;
    }

    public void setNumPages(int numPages) {
        this.numPages = numPages;
    }

    public List<SearchFacet> getArtistFacets() {
        return artistFacets;
    }

    public void setArtistFacets(List<SearchFacet> artistFacets) {
        this.artistFacets = artistFacets;
    }

    public List<SearchFacet> getAlbumFacets() {
        return albumFacets;
    }

    public void setAlbumFacets(List<SearchFacet> albumFacets) {
        this.albumFacets = albumFacets;
    }

    public List<SearchFacet> getGenreFacets() {
        return genreFacets;
    }

    public void setGenreFacets(List<SearchFacet> genreFacets) {
        this.genreFacets = genreFacets;
    }

    public List<SearchFacet> getDecadeFacets() {
        return decadeFacets;
    }

    public void setDecadeFacets(List<SearchFacet> decadeFacets) {
        this.decadeFacets = decadeFacets;
    }

    public List<DjMedia> getResults() {
        return results;
    }

    public void setResults(List<DjMedia> results) {
        this.results = results;
    }
}

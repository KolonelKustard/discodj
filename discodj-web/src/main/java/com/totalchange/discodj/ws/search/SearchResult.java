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

import java.util.List;

import com.totalchange.discodj.web.shared.dj.DjMedia;

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

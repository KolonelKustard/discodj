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
package com.totalchange.discodj.web.shared.dj;

import java.util.List;

import net.customware.gwt.dispatch.shared.Result;

public class SearchResult implements Result {
    private List<SearchFacet> artistFacets;
    private List<SearchFacet> albumFacets;
    private List<SearchFacet> genreFacets;
    private List<SearchFacet> decadeFacets;

    List<SearchFacet> getArtistFacets() {
        return artistFacets;
    }

    void setArtistFacets(List<SearchFacet> artistFacets) {
        this.artistFacets = artistFacets;
    }

    List<SearchFacet> getAlbumFacets() {
        return albumFacets;
    }

    void setAlbumFacets(List<SearchFacet> albumFacets) {
        this.albumFacets = albumFacets;
    }

    List<SearchFacet> getGenreFacets() {
        return genreFacets;
    }

    void setGenreFacets(List<SearchFacet> genreFacets) {
        this.genreFacets = genreFacets;
    }

    List<SearchFacet> getDecadeFacets() {
        return decadeFacets;
    }

    void setDecadeFacets(List<SearchFacet> decadeFacets) {
        this.decadeFacets = decadeFacets;
    }
}

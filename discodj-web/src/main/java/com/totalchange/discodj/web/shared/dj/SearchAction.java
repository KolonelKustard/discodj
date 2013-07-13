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

import java.util.ArrayList;
import java.util.List;

import net.customware.gwt.dispatch.shared.Action;

public class SearchAction implements Action<SearchResult> {
    private String keywords;
    private List<String> facetIds = new ArrayList<String>();

    String getKeywords() {
        return keywords;
    }

    void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    List<String> getFacetIds() {
        return facetIds;
    }

    void setFacets(List<String> facetIds) {
        this.facetIds = facetIds;
    }
}

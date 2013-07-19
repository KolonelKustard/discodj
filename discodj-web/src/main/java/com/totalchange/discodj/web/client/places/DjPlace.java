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
package com.totalchange.discodj.web.client.places;

import java.util.List;
import java.util.Map;

import com.google.gwt.place.shared.Place;

public class DjPlace extends Place {
    private String keywords;
    private List<Integer> facetIds;

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public List<Integer> getFacetIds() {
        return facetIds;
    }

    public void setFacetIds(List<Integer> facetIds) {
        this.facetIds = facetIds;
    }

    public static class Tokenizer extends AbstractPlaceTokenizer<DjPlace> {
        @Override
        protected DjPlace getPlace(Map<String, List<String>> params) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        protected Map<String, List<String>> getParams(DjPlace place) {
            // TODO Auto-generated method stub
            return null;
        }
    }
}

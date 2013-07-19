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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.place.shared.Place;

public class DjPlace extends Place {
    private static final String PARAM_KEYWORDS = "q";
    private static final String PARAM_FACET_IDS = "f";

    private String keywords;
    private List<String> facetIds;

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public List<String> getFacetIds() {
        return facetIds;
    }

    public void setFacetIds(List<String> facetIds) {
        this.facetIds = facetIds;
    }

    public static class Tokenizer extends AbstractPlaceTokenizer<DjPlace> {
        @Override
        protected DjPlace getPlace(Map<String, List<String>> params) {
            DjPlace place = new DjPlace();

            List<String> keywords = params.get(PARAM_KEYWORDS);
            if (keywords != null && !keywords.isEmpty()) {
                place.setKeywords(keywords.get(0));
            }

            List<String> facetIds = params.get(PARAM_KEYWORDS);
            if (facetIds != null && !facetIds.isEmpty()) {
                place.setFacetIds(facetIds);
            }

            return place;
        }

        @Override
        protected Map<String, List<String>> getParams(DjPlace place) {
            Map<String, List<String>> params = new HashMap<String, List<String>>();

            if (place.getKeywords() != null && place.getKeywords().length() > 0) {
                List<String> keywords = new ArrayList<String>(1);
                keywords.add(place.getKeywords());
                params.put(PARAM_KEYWORDS, keywords);
            }

            if (place.getFacetIds() != null && !place.getFacetIds().isEmpty()) {
                params.put(PARAM_FACET_IDS, place.getFacetIds());
            }

            return params;
        }
    }
}

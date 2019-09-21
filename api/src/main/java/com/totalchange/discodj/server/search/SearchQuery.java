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
package com.totalchange.discodj.server.search;

import java.util.ArrayList;
import java.util.List;

public class SearchQuery {
    private long start = 0;
    private long rows = 10;
    private String keywords = null;
    private List<String> facetIds = new ArrayList<>();

    public long getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public long getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public void addFacetId(String facetId) {
        this.facetIds.add(facetId);
    }

    public List<String> getFacetIds() {
        return facetIds;
    }
}

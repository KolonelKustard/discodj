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

import java.util.Collections;
import java.util.List;

public class SearchQuery {
    private final long start;
    private final long rows;
    private final String keywords;
    private final List<String> facetIds;

    private SearchQuery(long start, long rows, String keywords, List<String> facetIds) {
        this.start = start;
        this.rows = rows;
        this.keywords = keywords;
        this.facetIds = facetIds;
    }

    public long getStart() {
        return start;
    }

    public long getRows() {
        return rows;
    }

    public String getKeywords() {
        return keywords;
    }

    public List<String> getFacetIds() {
        return facetIds;
    }

    public static final class Builder {
        private long start = 0;
        private long rows = 10;
        private String keywords = null;
        private List<String> facetIds = Collections.emptyList();

        public Builder withStart(long start) {
            this.start = start;
            return this;
        }

        public Builder withRows(long rows) {
            this.rows = rows;
            return this;
        }

        public Builder withKeywords(String keywords) {
            this.keywords = keywords;
            return this;
        }

        public Builder withFacetIds(List<String> facetIds) {
            this.facetIds = facetIds;
            return this;
        }

        public SearchQuery build() {
            return new SearchQuery(start, rows, keywords, facetIds);
        }
    }
}

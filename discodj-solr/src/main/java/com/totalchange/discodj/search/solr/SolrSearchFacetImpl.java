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
package com.totalchange.discodj.search.solr;

import org.apache.solr.client.solrj.response.FacetField.Count;

import com.totalchange.discodj.search.SearchFacet;

final class SolrSearchFacetImpl implements SearchFacet {
    private Count count;
    
    SolrSearchFacetImpl(Count count) {
        this.count = count;
    }
    
    @Override
    public String getId() {
        return count.getAsFilterQuery();
    }

    @Override
    public String getName() {
        return count.getName();
    }

    @Override
    public long getNumMatches() {
        return count.getCount();
    }
}

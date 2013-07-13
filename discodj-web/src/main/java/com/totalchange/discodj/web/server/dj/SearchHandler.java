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
package com.totalchange.discodj.web.server.dj;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.totalchange.discodj.web.shared.dj.SearchAction;
import com.totalchange.discodj.web.shared.dj.SearchResult;

public class SearchHandler implements ActionHandler<SearchAction, SearchResult> {
    private static final Logger logger = LoggerFactory
            .getLogger(SearchHandler.class);

    @Override
    public Class<SearchAction> getActionType() {
        return SearchAction.class;
    }

    @Override
    public SearchResult execute(SearchAction action, ExecutionContext context)
            throws DispatchException {
        logger.trace("DJ search underway");
        SearchResult result = new SearchResult();

        logger.trace("DJ search complete with result {}", result);
        return result;
    }

    @Override
    public void rollback(SearchAction action, SearchResult result,
            ExecutionContext context) throws DispatchException {
        logger.warn("Call to roll back home action will have no effect");
    }
}

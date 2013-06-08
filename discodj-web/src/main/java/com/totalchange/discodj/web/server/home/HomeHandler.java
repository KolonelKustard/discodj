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
package com.totalchange.discodj.web.server.home;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.totalchange.discodj.web.shared.home.HomeAction;
import com.totalchange.discodj.web.shared.home.HomeResult;

public class HomeHandler implements ActionHandler<HomeAction, HomeResult> {
    private static final Logger logger = LoggerFactory
            .getLogger(HomeHandler.class);

    @Override
    public Class<HomeAction> getActionType() {
        return HomeAction.class;
    }

    @Override
    public HomeResult execute(HomeAction action, ExecutionContext context)
            throws DispatchException {
        logger.trace("Jizz Home execution underway");
        HomeResult result = new HomeResult();

        logger.trace("Jizz Home execution complete with result {}", result);
        return result;
    }

    @Override
    public void rollback(HomeAction action, HomeResult result,
            ExecutionContext context) throws DispatchException {
        logger.warn("Call to roll back home action will have no effect");
    }
}

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
package com.totalchange.discodj.web.server.player;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.totalchange.discodj.web.shared.player.GetNextFromPlaylistAction;
import com.totalchange.discodj.web.shared.player.GetNextFromPlaylistResult;

public class GetNextFromPlaylistHandler implements
        ActionHandler<GetNextFromPlaylistAction, GetNextFromPlaylistResult> {
    private static final Logger logger = LoggerFactory
            .getLogger(GetNextFromPlaylistHandler.class);

    @Override
    public Class<GetNextFromPlaylistAction> getActionType() {
        return GetNextFromPlaylistAction.class;
    }

    @Override
    public GetNextFromPlaylistResult execute(GetNextFromPlaylistAction action,
            ExecutionContext context) throws DispatchException {
        logger.trace("Fetching server settings");
        GetNextFromPlaylistResult result = new GetNextFromPlaylistResult();

        logger.trace("Returning server settings {}", result);
        return result;
    }

    @Override
    public void rollback(GetNextFromPlaylistAction action,
            GetNextFromPlaylistResult result, ExecutionContext context)
            throws DispatchException {
        logger.warn("Calls to GetSettingsHandler#rollback do nothing");
    }
}

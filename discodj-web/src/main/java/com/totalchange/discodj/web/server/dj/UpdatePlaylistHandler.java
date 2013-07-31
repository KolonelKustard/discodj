package com.totalchange.discodj.web.server.dj;

import javax.inject.Inject;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.totalchange.discodj.queue.PlaylistQueue;
import com.totalchange.discodj.web.shared.dj.StatusResult;
import com.totalchange.discodj.web.shared.dj.UpdatePlaylistAction;

public class UpdatePlaylistHandler implements
        ActionHandler<UpdatePlaylistAction, StatusResult> {

    private static final Logger logger = LoggerFactory
            .getLogger(UpdatePlaylistHandler.class);

    private PlaylistQueue queue;
    private StatusHandler statusHandler;

    @Inject
    public UpdatePlaylistHandler(PlaylistQueue queue,
            StatusHandler statusHandler) {
        this.queue = queue;
        this.statusHandler = statusHandler;
    }

    @Override
    public Class<UpdatePlaylistAction> getActionType() {
        return UpdatePlaylistAction.class;
    }

    @Override
    public StatusResult execute(UpdatePlaylistAction action,
            ExecutionContext context) throws DispatchException {
        queue.setPlaylist(action.getRevisedPlaylist());

        return statusHandler.execute(null, context);
    }

    @Override
    public void rollback(UpdatePlaylistAction action, StatusResult result,
            ExecutionContext context) throws DispatchException {
        logger.warn("Rollback not supported");
    }
}

package com.totalchange.discodj.web.server.dj;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.totalchange.discodj.media.Media;
import com.totalchange.discodj.queue.PlaylistQueue;
import com.totalchange.discodj.web.shared.dj.DjMedia;
import com.totalchange.discodj.web.shared.dj.StatusAction;
import com.totalchange.discodj.web.shared.dj.StatusResult;

public class StatusHandler implements ActionHandler<StatusAction, StatusResult> {
    private static final Logger logger = LoggerFactory
            .getLogger(StatusHandler.class);

    private PlaylistQueue queue;

    @Inject
    public StatusHandler(PlaylistQueue queue) {
        this.queue = queue;
    }

    @Override
    public Class<StatusAction> getActionType() {
        return StatusAction.class;
    }

    @Override
    public StatusResult execute(StatusAction action, ExecutionContext context)
            throws DispatchException {
        logger.trace("Executing status update");

        StatusResult result = new StatusResult();

        Media nowPlaying = queue.getLastPopped();
        result.setNowPlaying(SearchHandler.copyMedia(nowPlaying));

        List<Media> playlist = queue.getPlaylist();
        List<DjMedia> copied = new ArrayList<>(playlist.size());
        for (Media media : playlist) {
            copied.add(SearchHandler.copyMedia(media));
        }
        result.setPlaylist(copied);

        logger.trace("Executed status update and returning {}", result);
        return result;
    }

    @Override
    public void rollback(StatusAction action, StatusResult result,
            ExecutionContext context) throws DispatchException {
        logger.warn("Call to roll back status action will have no effect");
    }
}

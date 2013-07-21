package com.totalchange.discodj.web.server.inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.totalchange.discodj.web.server.dj.SearchHandler;
import com.totalchange.discodj.web.server.dj.StatusHandler;
import com.totalchange.discodj.web.server.player.GetNextFromPlaylistHandler;
import com.totalchange.discodj.web.shared.dj.SearchAction;
import com.totalchange.discodj.web.shared.dj.StatusAction;
import com.totalchange.discodj.web.shared.player.GetNextFromPlaylistAction;

import net.customware.gwt.dispatch.server.guice.ActionHandlerModule;

public class ActionsModule extends ActionHandlerModule {
    private static Logger logger = LoggerFactory.getLogger(ActionsModule.class);

    @Override
    protected void configureHandlers() {
        logger.trace("Configuring dispatch handler bindings");

        bindHandler(SearchAction.class, SearchHandler.class);
        bindHandler(StatusAction.class, StatusHandler.class);
        bindHandler(GetNextFromPlaylistAction.class,
                GetNextFromPlaylistHandler.class);

        logger.trace("Configured dispatch handler bindings");
    }
}

package com.totalchange.discodj.web.shared.dj;

import java.util.List;

import net.customware.gwt.dispatch.shared.Action;

public class UpdatePlaylistAction implements Action<StatusResult> {
    private List<String> revisedPlaylist;

    public List<String> getRevisedPlaylist() {
        return revisedPlaylist;
    }

    public void setRevisedPlaylist(List<String> revisedPlaylist) {
        this.revisedPlaylist = revisedPlaylist;
    }
}

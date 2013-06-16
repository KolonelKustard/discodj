package com.totalchange.discodj.web.client.views;

import java.util.List;

import com.totalchange.discodj.web.shared.player.Media;

public interface DjView extends CommonView {
    public interface Presenter {
        void setPlaylist(List<Media> playlist);
    }

    void setPresenter(Presenter presenter);

    void setNowPlaying(Media playlist);

    void setResults(int currentPage, int numPages, List<Media> results);

    void setPlaylist(List<Media> playlist);
}

package com.totalchange.discodj.web.client.views;

import java.util.List;

import com.totalchange.discodj.web.shared.dj.SearchResultMedia;
import com.totalchange.discodj.web.shared.player.Media;

public interface DjView extends CommonView {
    public interface Presenter {
        void setPlaylist(List<Media> playlist);
        void search(String keywords);
        void addFacet(String facetId);
        void goToPage(int page);
    }

    void setPresenter(Presenter presenter);

    void setNowPlaying(String artist, String album);

    void setResults(int currentPage, int numPages,
            List<SearchResultMedia> results);

    void setPlaylist(List<SearchResultMedia> playlist);
}

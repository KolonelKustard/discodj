package com.totalchange.discodj.web.client.views;

import java.util.List;

import com.totalchange.discodj.web.shared.dj.SearchFacet;
import com.totalchange.discodj.web.shared.dj.DjMedia;
import com.totalchange.discodj.web.shared.player.Media;

public interface DjView extends CommonView {
    public interface Presenter {
        void setPlaylist(List<Media> playlist);
        void search(String keywords);
        void addFacet(String facetId);
        void removeFacet(String facetId);
        void previousPage();
        void nextPage();
    }

    void setPresenter(Presenter presenter);

    void setNowPlaying(String artist, String album);

    void setResults(int currentPage, int numPages,
            List<DjMedia> results);
    
    void setArtistFacets(List<SearchFacet> artistFacets);
    void setAlbumFacets(List<SearchFacet> artistFacets);
    void setGenreFacets(List<SearchFacet> artistFacets);
    void setDecadeFacets(List<SearchFacet> artistFacets);

    void setPlaylist(List<DjMedia> playlist);
}

package com.totalchange.discodj.web.client.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.totalchange.discodj.web.client.views.DjView;
import com.totalchange.discodj.web.shared.player.Media;

public class DjActivity extends AbstractActivity implements DjView.Presenter {
    private static final Logger logger = Logger.getLogger(DjActivity.class
            .getName());

    private DjView djView;

    @Inject
    public DjActivity(DjView djView) {
        this.djView = djView;

        this.djView.setPresenter(this);
    }

    private Media makeDummyMedia(String name, int num) {
        Media media = new Media();
        media.setId("test" + name + num);
        media.setUrl("http://test/" + name.toLowerCase() + "/" + num);
        media.setArtist(name + " Artist " + num);
        media.setTitle(name + " Title " + num);
        return media;
    }

    private void populateWithDummyData() {
        djView.setNowPlaying(makeDummyMedia("NowPlaying", 0));

        List<Media> results = new ArrayList<Media>();
        for (int num = 0; num < 10; num++) {
            results.add(makeDummyMedia("Result", num));
        }
        djView.setResults(1, 20, results);

        List<Media> playlist = new ArrayList<Media>();
        for (int num = 0; num < 4; num++) {
            playlist.add(makeDummyMedia("Playlist", num));
        }
        djView.setPlaylist(playlist);
    }

    @Override
    public void start(AcceptsOneWidget container, EventBus eventBus) {
        logger.finer("Starting up InitJizzActivity");
        container.setWidget(djView.asWidget());
        populateWithDummyData();
        logger.finer("Finished starting up InitJizzActivity");
    }

    @Override
    public void setPlaylist(List<Media> playlist) {
        // TODO Auto-generated method stub
    }
}

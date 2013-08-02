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
package com.totalchange.discodj.web.client.views.impl;

import java.util.ArrayList;
import java.util.List;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandlerAdapter;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.totalchange.discodj.web.client.views.DjView;
import com.totalchange.discodj.web.shared.dj.DjMedia;
import com.totalchange.discodj.web.shared.dj.SearchFacet;

public class DjViewImpl extends Composite implements DjView {
    interface DjViewUiBinder extends UiBinder<Widget, DjViewImpl> {
    }

    private static DjViewUiBinder uiBinder = GWT.create(DjViewUiBinder.class);

    private Presenter presenter;
    private PickupDragController songDragController;

    @UiField
    AbsolutePanel boundaryPanel;

    @UiField
    FacetsWidget artistFacets;

    @UiField
    FacetsWidget albumFacets;

    @UiField
    FacetsWidget genreFacets;

    @UiField
    FacetsWidget decadeFacets;

    @UiField
    VerticalPanel resultsPanel;

    @UiField
    Button previousButton;

    @UiField
    Button nextButton;

    @UiField
    Label pageLabel;

    @UiField
    VerticalPanel playlistPanel;

    @UiField
    Label nowPlayingLabel;

    @UiField
    TextBox searchTextBox;

    public DjViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        initDnd();
    }

    private void initDnd() {
        this.songDragController = new PickupDragController(boundaryPanel, false);
        songDragController.setBehaviorMultipleSelection(false);

        VerticalPanelDropController playlistDropController = new VerticalPanelDropController(
                playlistPanel);
        songDragController.registerDropController(playlistDropController);

        songDragController.addDragHandler(new DragHandlerAdapter() {
            @Override
            public void onDragEnd(DragEndEvent event) {
                presenter.playlistPossiblyChanged();
            }
        });
    }

    @UiHandler("searchTextBox")
    void searchTextBoxKeyUp(KeyUpEvent ev) {
        presenter.search(searchTextBox.getValue());
    }

    @UiHandler("previousButton")
    void previousButtonClick(ClickEvent ev) {
        presenter.previousPage();
    }

    @UiHandler("nextButton")
    void nextButtonClick(ClickEvent ev) {
        presenter.nextPage();
    }

    @Override
    public void render() {
        // Nothing to be done
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setNowPlaying(String artist, String title) {
        nowPlayingLabel.setText(title + " - " + artist);
    }

    private Widget makeMediaWidget(DjMedia media) {
        MediaWidget mediaWidget = new MediaWidget(media);
        return new FocusPanel(mediaWidget);
    }

    @Override
    public void setResults(int currentPage, int numPages, List<DjMedia> results) {
        resultsPanel.clear();
        for (DjMedia media : results) {
            Widget mediaWidget = makeMediaWidget(media);
            resultsPanel.add(mediaWidget);

            // Make widget draggable (if it can be added to the playlist)
            if (media.getWhenCanBePlayedAgain() > -1) {
                songDragController.makeDraggable(mediaWidget);
            }
        }

        pageLabel.setText("Page " + currentPage + " of " + numPages);
        previousButton.setEnabled(currentPage > 1);
        nextButton.setEnabled(currentPage < numPages);
    }

    @Override
    public void setArtistFacets(List<SearchFacet> facets) {
        artistFacets.setFacets(presenter, facets);
    }

    @Override
    public void setAlbumFacets(List<SearchFacet> facets) {
        albumFacets.setFacets(presenter, facets);
    }

    @Override
    public void setGenreFacets(List<SearchFacet> facets) {
        this.genreFacets.setFacets(presenter, facets);
    }

    @Override
    public void setDecadeFacets(List<SearchFacet> facets) {
        this.decadeFacets.setFacets(presenter, facets);
    }

    @Override
    public List<DjMedia> getPlaylist() {
        List<DjMedia> playlist = new ArrayList<DjMedia>(
                playlistPanel.getWidgetCount());
        for (int num = 0; num < playlistPanel.getWidgetCount(); num++) {
            Object widget = playlistPanel.getWidget(num);

            if (widget instanceof FocusPanel) {
                FocusPanel focusPanel = (FocusPanel) widget;
                widget = focusPanel.getWidget();
            }

            if (widget != null && widget instanceof MediaWidget) {
                MediaWidget mediaWidget = (MediaWidget) widget;
                playlist.add(mediaWidget.getMedia());
            }
        }

        return playlist;
    }

    @Override
    public void setPlaylist(List<DjMedia> playlist) {
        playlistPanel.clear();
        for (DjMedia media : playlist) {
            Widget mediaWidget = makeMediaWidget(media);
            playlistPanel.add(mediaWidget);

            // Make widget draggable
            songDragController.makeDraggable(mediaWidget);
        }
    }
}

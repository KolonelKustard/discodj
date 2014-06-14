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

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
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
    HTMLPanel artistFacets;

    @UiField
    HTMLPanel albumFacets;

    @UiField
    VerticalPanel resultsPanel;

    @UiField
    Button previousButton;

    @UiField
    Button nextButton;

    @UiField
    HTMLPanel pagingPanel;

    @UiField
    InlineLabel pageNumLabel;

    @UiField
    InlineLabel totalPagesLabel;

    @UiField
    VerticalPanel playlistPanel;

    @UiField
    HTMLPanel nowPlaying;

    @UiField
    InlineLabel nowPlayingArtistLabel;

    @UiField
    InlineLabel nowPlayingTitleLabel;

    @UiField
    HTMLPanel nothingPlaying;

    @UiField
    TextBox searchTextBox;

    public DjViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
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
        if (artist != null || title != null) {
            nothingPlaying.setVisible(false);
            nowPlayingArtistLabel.setText(artist);
            nowPlayingTitleLabel.setText(title);
            nowPlaying.setVisible(true);
        } else {
            nowPlaying.setVisible(false);
            nothingPlaying.setVisible(true);
        }
    }

    private void moveWidgetFromSearchResultsToPlaylist(Widget widget) {
        resultsPanel.remove(widget);
        playlistPanel.add(widget);
        presenter.playlistPossiblyChanged();
    }

    private Widget makeMediaWidget(DjMedia media) {
        MediaWidget mediaWidget = new MediaWidget(media);

        // Stops events making their way to the media widget
        final FocusPanel wrapper = new FocusPanel(mediaWidget);

        if (media.getWhenCanBePlayedAgain() <= -1) {
            // Also allow a double click to move it over
            wrapper.addDoubleClickHandler(new DoubleClickHandler() {
                @Override
                public void onDoubleClick(DoubleClickEvent event) {
                    moveWidgetFromSearchResultsToPlaylist(wrapper);
                }
            });
        }

        return wrapper;
    }
    
    private void updateFacets(HTMLPanel panel, List<SearchFacet> facets) {
        panel.clear();
        for (SearchFacet facet : facets) {
            panel.add(new FacetWidget(presenter, facet));
        }
    }

    @Override
    public void setResults(int currentPage, int numPages, List<DjMedia> results) {
        resultsPanel.clear();
        for (DjMedia media : results) {
            Widget mediaWidget = makeMediaWidget(media);
            resultsPanel.add(mediaWidget);
        }

        if (numPages > 1) {
            pageNumLabel.setText(String.valueOf(currentPage));
            totalPagesLabel.setText(String.valueOf(numPages));

            previousButton.setEnabled(currentPage > 1);
            nextButton.setEnabled(currentPage < numPages);

            pagingPanel.setVisible(true);
        } else {
            pagingPanel.setVisible(false);
        }
    }

    @Override
    public void setArtistFacets(List<SearchFacet> facets) {
        updateFacets(artistFacets, facets);
    }

    @Override
    public void setAlbumFacets(List<SearchFacet> facets) {
        updateFacets(albumFacets, facets);
    }

    @Override
    public void setGenreFacets(List<SearchFacet> facets) {
        // TODO Get rid of genre facets altogether
    }

    @Override
    public void setDecadeFacets(List<SearchFacet> facets) {
        // TODO Get rid of decade facets altogether
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

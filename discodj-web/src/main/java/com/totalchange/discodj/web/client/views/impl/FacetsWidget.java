package com.totalchange.discodj.web.client.views.impl;

import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.totalchange.discodj.web.client.views.DjView;
import com.totalchange.discodj.web.shared.dj.SearchFacet;

public class FacetsWidget extends Composite {
    interface FacetsWidgetUiBinder extends UiBinder<Widget, FacetsWidget> {
    }

    private static FacetsWidgetUiBinder uiBinder = GWT
            .create(FacetsWidgetUiBinder.class);

    @UiField
    Label titleLabel;

    @UiField
    VerticalPanel checkboxesPanel;

    private List<SearchFacet> facets;

    public FacetsWidget() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    private void renderFacets(final DjView.Presenter presenter) {
        checkboxesPanel.clear();

        if (facets == null) {
            return;
        }

        for (final SearchFacet facet : facets) {
            final CheckBox checkbox = new CheckBox();
            checkbox.setText(facet.getName() + " (" + facet.getNumMatches()
                    + ")");
            checkbox.setValue(facet.isSelected());
            checkbox.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (checkbox.getValue()) {
                        presenter.addFacet(facet.getId());
                    } else {
                        presenter.removeFacet(facet.getId());
                    }
                }
            });
            checkboxesPanel.add(checkbox);
        }
    }

    public String getTitle() {
        return titleLabel.getText();
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public List<SearchFacet> getFacets() {
        return facets;
    }

    public void setFacets(DjView.Presenter presenter, List<SearchFacet> facets) {
        this.facets = facets;
        renderFacets(presenter);
    }
}

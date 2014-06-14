package com.totalchange.discodj.web.client.views.impl;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.totalchange.discodj.web.client.views.DjView;
import com.totalchange.discodj.web.shared.dj.SearchFacet;

public class FacetWidget extends Composite {
    interface FacetsWidgetUiBinder extends UiBinder<Widget, FacetWidget> {
    }

    private static FacetsWidgetUiBinder uiBinder = GWT
            .create(FacetsWidgetUiBinder.class);

    @UiField
    CheckBox checkbox;

    private DjView.Presenter presenter;
    private SearchFacet facet;

    public FacetWidget(DjView.Presenter presenter, SearchFacet facet) {
        this.presenter = presenter;
        this.facet = facet;

        initWidget(uiBinder.createAndBindUi(this));
        checkbox.setValue(facet.isSelected());
        checkbox.setText(facet.getName() + " (" + facet.getNumMatches() + ")");
    }

    @UiHandler("checkbox")
    void handleClick(ClickEvent e) {
        if (checkbox.getValue()) {
            presenter.addFacet(facet.getId());
        } else {
            presenter.removeFacet(facet.getId());
        }
    }
}

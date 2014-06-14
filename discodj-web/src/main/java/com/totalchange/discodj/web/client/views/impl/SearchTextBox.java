package com.totalchange.discodj.web.client.views.impl;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.TextBox;

public class SearchTextBox extends TextBox {
    public SearchTextBox() {
        super();
        getElement().setId(DOM.createUniqueId());
    }

    public String getPlaceholder() {
        return getElement().getAttribute("placeholder");
    }

    public void setPlaceholder(String placeholder) {
        getElement().setAttribute("placeholder", placeholder);
    }
}

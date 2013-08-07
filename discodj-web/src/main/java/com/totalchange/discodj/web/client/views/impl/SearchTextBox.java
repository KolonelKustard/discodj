package com.totalchange.discodj.web.client.views.impl;

import com.google.gwt.user.client.ui.TextBox;

public class SearchTextBox extends TextBox {
    public String getPlaceholder() {
        return getElement().getAttribute("placeholder");
    }

    public void setPlaceholder(String placeholder) {
        getElement().setAttribute("placeholder", placeholder);
    }
}

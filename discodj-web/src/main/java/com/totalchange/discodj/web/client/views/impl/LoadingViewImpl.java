package com.totalchange.discodj.web.client.views.impl;

import javax.inject.Singleton;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.totalchange.discodj.web.client.views.LoadingView;

@Singleton
public class LoadingViewImpl implements LoadingView {
    interface LoadingViewUiBinder extends UiBinder<Widget, LoadingViewImpl> {
    }

    private static LoadingViewUiBinder uiBinder = GWT
            .create(LoadingViewUiBinder.class);
    
    PopupPanel loadingPanel;
    
    public LoadingViewImpl() {
        loadingPanel = (PopupPanel) uiBinder.createAndBindUi(this);
    }
    
    @Override
    public void show() {
        loadingPanel.center();
    }

    @Override
    public void hide() {
        loadingPanel.hide();
    }
}

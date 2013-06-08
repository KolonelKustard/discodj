package com.totalchange.discodj.web.client.loading;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.totalchange.discodj.web.client.views.LoadingView;

@Singleton
public class LoadingHandler {
    private LoadingView loadingView;

    @Inject
    public LoadingHandler(LoadingView loadingView) {
        this.loadingView = loadingView;
    }

    public void modal() {
        loadingView.show();
    }

    public void done() {
        loadingView.hide();
    }
}

package com.totalchange.discodj.web.client.views;

import com.google.gwt.user.client.ui.IsWidget;

public interface ErrorView extends IsWidget {
    void setMessage(String msg);
    void setErrorMessage(String error);
    void setStackTrace(String stackTrace);
    void show();
}

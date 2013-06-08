package com.totalchange.discodj.web.client.views.impl;

import javax.inject.Singleton;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.totalchange.discodj.web.client.views.ErrorView;

@Singleton
public class ErrorViewImpl implements ErrorView {
    interface ErrorViewUiBinder extends UiBinder<Widget, ErrorViewImpl> {
    }

    private static ErrorViewUiBinder uiBinder = GWT
            .create(ErrorViewUiBinder.class);

    DialogBox dialogBox;
    
    @UiField
    HTMLPanel messagePanel;
    @UiField
    Label messageLabel;

    @UiField
    HTMLPanel errorPanel;
    @UiField
    Label errorLabel;

    @UiField
    DisclosurePanel stackTracePanel;
    @UiField
    Label stackTraceLabel;

    public ErrorViewImpl() {
        dialogBox = (DialogBox) uiBinder.createAndBindUi(this);
    }

    @Override
    public void setMessage(String msg) {
        if ((msg != null) && (msg.length() > 0)) {
            messagePanel.setVisible(true);
            messageLabel.setText(msg);
        } else {
            messagePanel.setVisible(false);
            messageLabel.setText("");
        }
    }

    @Override
    public void setErrorMessage(String detail) {
        if ((detail != null) && (detail.length() > 0)) {
            errorPanel.setVisible(true);
            errorLabel.setText(detail);
        } else {
            errorPanel.setVisible(false);
            errorLabel.setText("");
        }
    }

    @Override
    public void setStackTrace(String stackTrace) {
        if ((stackTrace != null) && (stackTrace.length() > 0)) {
            stackTracePanel.setVisible(true);
            stackTraceLabel.setText(stackTrace);
        } else {
            stackTracePanel.setVisible(false);
            stackTraceLabel.setText("");
        }
    }

    @Override
    public void show() {
        dialogBox.center();
    }

    @Override
    public Widget asWidget() {
        return dialogBox;
    }
    
    @UiHandler("okButton")
    public void okButtonOnClick(ClickEvent e) {
        dialogBox.hide();
    }
}

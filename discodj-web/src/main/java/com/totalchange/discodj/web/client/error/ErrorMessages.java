package com.totalchange.discodj.web.client.error;

import com.google.gwt.i18n.client.Messages;

public interface ErrorMessages extends Messages {
    @DefaultMessage("You specified \"{0}\" for the SMTP server port, but "
            + "that''s not a number.  The SMTP server has to be of the form"
            + "server:port and the :port bit is optional, e.g. 127.0.0.1 or "
            + "smtp.mycompany.com:123.")
    public String invalidSmtpServerPort(String port);
}

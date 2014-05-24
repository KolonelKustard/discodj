package com.totalchange.discodj.xuggler;

import com.totalchange.discodj.catalogue.CatalogueException;
import com.xuggle.xuggler.IError;

final class XugglerException extends CatalogueException {
    private static final long serialVersionUID = 1L;

    private XugglerException(String msg) {
        super(msg);
    }

    static void throwIfInError(int errorCode) throws XugglerException {
        if (errorCode >= 0) {
            return;
        }

        IError err = IError.make(errorCode);
        if (err != null) {
            throw new XugglerException(err.toString());
        } else {
            throw new XugglerException("Unidentified Xuggler exception with "
                    + "error code: " + errorCode);
        }
    }
}

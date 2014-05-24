package com.totalchange.discodj.catalogue;

public class CatalogueException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CatalogueException(String message) {
        super(message);
    }

    public CatalogueException(Throwable cause) {
        super(cause);
    }

    public CatalogueException(String message, Throwable cause) {
        super(message, cause);
    }

    public CatalogueException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}

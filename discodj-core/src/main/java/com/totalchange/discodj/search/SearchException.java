package com.totalchange.discodj.search;

public class SearchException extends RuntimeException {
    private static final long serialVersionUID = -1879212149005719357L;

    public SearchException() {
        super();
    }

    public SearchException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SearchException(String message, Throwable cause) {
        super(message, cause);
    }

    public SearchException(String message) {
        super(message);
    }

    public SearchException(Throwable cause) {
        super(cause);
    }
}

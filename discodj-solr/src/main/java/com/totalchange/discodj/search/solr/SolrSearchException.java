package com.totalchange.discodj.search.solr;

import com.totalchange.discodj.search.SearchException;

final class SolrSearchException extends SearchException {
    private static final long serialVersionUID = 3742440188542647173L;

    public SolrSearchException() {
    }

    public SolrSearchException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SolrSearchException(String message, Throwable cause) {
        super(message, cause);
    }

    public SolrSearchException(String message) {
        super(message);
    }

    public SolrSearchException(Throwable cause) {
        super(cause);
    }
}

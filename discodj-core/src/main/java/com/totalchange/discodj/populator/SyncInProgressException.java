package com.totalchange.discodj.populator;

public class SyncInProgressException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SyncInProgressException(String message) {
        super(message);
    }
}

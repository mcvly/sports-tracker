package org.mcvly.tracker.model.service;

/**
 * @author mcvly
 * @since 09.04.14
 */
public class STServiceException extends Exception {

    public STServiceException() {
    }

    public STServiceException(String message) {
        super(message);
    }

    public STServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

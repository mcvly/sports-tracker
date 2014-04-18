package org.mcvly.tracker.service;

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

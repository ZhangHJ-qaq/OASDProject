package com.haojie.exception;

/**
 * If the user has not typed in the information about the photo completely when uploading, then an instance
 * of this will be thrown.
 */
public class PhotoInfoIncompleteException extends Exception {
    public PhotoInfoIncompleteException() {
        super();
    }

    public PhotoInfoIncompleteException(String message) {
        super(message);
    }
}

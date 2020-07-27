package com.haojie.exception;

/**
 * If the city and country  provided by the frontend when uploading don't match, then an instance of this
 * will be thrown
 */
public class CountryCityMismatchException extends Exception {
    public CountryCityMismatchException() {
        super();
    }

    public CountryCityMismatchException(String message) {
        super(message);
    }
}

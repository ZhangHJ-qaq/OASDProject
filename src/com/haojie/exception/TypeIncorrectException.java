package com.haojie.exception;


/**
 * If the MIME type of the photo user has uploaded is unacceptable, then an instance of this will be thrown.
 */
public class TypeIncorrectException extends Exception{
    public TypeIncorrectException() {
    }

    public TypeIncorrectException(String message) {
        super(message);
    }
}

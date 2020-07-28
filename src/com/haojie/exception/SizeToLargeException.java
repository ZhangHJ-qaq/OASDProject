package com.haojie.exception;

public class SizeToLargeException extends Exception {
    public SizeToLargeException() {
    }

    public SizeToLargeException(String message) {
        super(message);
    }
}

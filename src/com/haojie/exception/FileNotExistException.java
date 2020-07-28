package com.haojie.exception;

/**
 * If user doesn't choose photo when uploading, an instance of this will be thrown
 */
public class FileNotExistException extends Exception {
    public FileNotExistException() {
    }

    public FileNotExistException(String message) {
        super(message);
    }
}

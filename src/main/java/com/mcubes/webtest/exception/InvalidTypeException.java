package com.mcubes.webtest.exception;

public class InvalidTypeException extends RuntimeException {
    public InvalidTypeException() {
        super();
    }

    public InvalidTypeException(String message) {
        super(message);
    }
}

package com.mcubes.webtest.exception;

public class InvalidAttributeException extends RuntimeException {
    public InvalidAttributeException() {
        super();
    }

    public InvalidAttributeException(String message) {
        super(message);
    }
}

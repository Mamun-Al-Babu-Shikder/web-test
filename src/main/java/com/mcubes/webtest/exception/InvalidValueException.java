package com.mcubes.webtest.exception;

public class InvalidValueException extends RuntimeException {
    public InvalidValueException() {
        super();
    }

    public InvalidValueException(String message) {
        super(message);
    }
}

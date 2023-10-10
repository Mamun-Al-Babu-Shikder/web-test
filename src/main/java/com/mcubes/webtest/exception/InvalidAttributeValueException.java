package com.mcubes.webtest.exception;

public class InvalidAttributeValueException extends RuntimeException {
    public InvalidAttributeValueException() {
        super();
    }

    public InvalidAttributeValueException(String message) {
        super(message);
    }
}

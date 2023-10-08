package com.mcubes.webtest.exception;

public class AttributeNotFoundException extends RuntimeException {
    public AttributeNotFoundException() {
        super();
    }

    public AttributeNotFoundException(String message) {
        super(message);
    }
}

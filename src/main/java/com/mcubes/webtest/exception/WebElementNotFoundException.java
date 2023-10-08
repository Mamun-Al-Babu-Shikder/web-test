package com.mcubes.webtest.exception;

public class WebElementNotFoundException extends RuntimeException {
    public WebElementNotFoundException() {
        super();
    }

    public WebElementNotFoundException(String message) {
        super(message);
    }
}

package com.mcubes.webtest.exception;

public class UndefineVariableException extends RuntimeException {
    public UndefineVariableException() {
        super();
    }

    public UndefineVariableException(String message) {
        super(message);
    }
}

package com.mcubes.webtest.exception;

public class UndefineVariableException extends RuntimeException {
    public UndefineVariableException() {
        super();
    }

    public UndefineVariableException(String varName) {
        super("variable named '%s' is not defined!".formatted(varName));
    }
}

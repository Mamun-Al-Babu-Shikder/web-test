package com.mcubes.webtest.exception;

public class StepExecutionException extends RuntimeException {
    public StepExecutionException() {
        super();
    }

    public StepExecutionException(String message) {
        super(message);
    }

    public StepExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}

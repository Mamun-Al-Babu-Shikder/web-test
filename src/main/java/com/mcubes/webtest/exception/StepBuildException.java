package com.mcubes.webtest.exception;

public class StepBuildException extends RuntimeException {
    public StepBuildException() {
        this("can't build the step");
    }

    public StepBuildException(String message) {
        super(message);
    }

    public StepBuildException(String message, Throwable cause) {
        super(message, cause);
    }
}

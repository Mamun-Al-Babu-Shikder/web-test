package com.mcubes.webtest.exception;

public class ExpressionEvaluationException extends RuntimeException {
    public ExpressionEvaluationException() {
        super();
    }

    public ExpressionEvaluationException(String message) {
        super(message);
    }
}

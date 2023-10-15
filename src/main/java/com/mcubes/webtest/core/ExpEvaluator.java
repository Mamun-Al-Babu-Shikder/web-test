package com.mcubes.webtest.core;

import com.mcubes.webtest.exception.ExpressionEvaluationException;
import com.mcubes.webtest.exception.TypeCastingException;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionException;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import static com.mcubes.webtest.enums.Patterns.IS_REQUIRED_EVAL;

public class ExpEvaluator {
    private static final SpelExpressionParser parser = new SpelExpressionParser();

    public static <T> T evaluate(String expression, StepContext stepContext, Class<T> clazz) {
        try {
            Expression exp = parser.parseExpression(expression);
            return exp.getValue(stepContext.context(), clazz);
        } catch (Exception ex) {
            throw new ExpressionEvaluationException("Failed to evaluate the expression [%s], %s".formatted(expression, ex.getMessage()));
        }
    }

    public static Object evaluate(String expression) {
        StepContext stepContext = StepContext.getInstance();
        return evaluate(expression, stepContext, Object.class);
    }

    public static <T> T evaluate(String expression, Class<T> clazz) {
        StepContext stepContext = StepContext.getInstance();
        return evaluate(expression, stepContext, clazz);
    }

    public static String evalAndGetString(String expression) {
        if (evaluate(expression) instanceof String value) return value;
        throw new TypeCastingException("Evaluated value can not converted to 'string' type");
    }

    public static String evalAndGetAsString(String expression) {
        return evaluate(expression).toString();
    }

    public static <T> T evalExpIfNeeded(String expression, Class<T> clazz) {
        final String input = expression.trim();
        if (expression.trim().matches(IS_REQUIRED_EVAL.pattern())) {
            return evaluate(input.substring(2, input.length()-1), clazz);
        }
        return convert(expression, clazz);
    }

    private static <T> T convert(String input, Class<T> clazz) {
        try {
            if (clazz == Integer.class) {
                return clazz.cast(Integer.valueOf(input));
            } else if (clazz == Long.class) {
                return clazz.cast(Long.valueOf(input));
            } else if (clazz == Double.class) {
                return clazz.cast(Double.valueOf(input));
            } else if (clazz == Float.class) {
                return clazz.cast(Float.valueOf(input));
            } else if (clazz == Boolean.class) {
                return clazz.cast(Boolean.valueOf(input));
            } else if (clazz == String.class) {
                return clazz.cast(input);
            } else {
                // Handle custom conversions for other data types here
                return null;
            }
        } catch (Exception e) {
            throw new TypeCastingException("Can't converted the input to specific type");
        }
    }

}

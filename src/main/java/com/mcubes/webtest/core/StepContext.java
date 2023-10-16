package com.mcubes.webtest.core;

import com.mcubes.webtest.annotation.ResolvableMethod;
import com.mcubes.webtest.exception.TypeCastingException;
import com.mcubes.webtest.exception.UndefineVariableException;
import com.mcubes.webtest.util.UtilityMethods;
import org.openqa.selenium.WebDriver;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Map;

public class StepContext {
    private static StepContext context;
    private WebDriver webDriver;
    private final StandardEvaluationContext local;
    private final ThreadLocal<StandardEvaluationContext> global;

    private StepContext(WebDriver webDriver) {
        this.local = null;
        StandardEvaluationContext context = new StandardEvaluationContext();
        registerMethods(context);
        this.webDriver = webDriver;
        this.global = ThreadLocal.withInitial(() -> context);
    }

    private StepContext(StandardEvaluationContext context, WebDriver webDriver) {
        registerMethods(context);
        this.local = context;
        this.webDriver = webDriver;
        this.global = null;
    }

    public synchronized static StepContext getInstance(WebDriver webDriver) {
        if (context == null) {
            context = new StepContext(webDriver);
        }
        return context;
    }

    public synchronized static StepContext getInstance() {
        return getInstance((WebDriver) null);
    }

    public StepContext getInstance(Map<String, Object> variables, WebDriver webDriver) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariables(variables);
        return new StepContext(context, webDriver);
    }

    public StepContext getInstance(Map<String, Object> variables) {
       return getInstance(variables, (WebDriver) null);
    }

    public StandardEvaluationContext context() {
        return local != null ? local : global.get();
    }

    public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public WebDriver getWebDriver() {
        return this.webDriver;
    }

    public void set(String key, Object value) {
        context().setVariable(key, value);
    }

    private Object get(String key) {
        Object value = context().lookupVariable(key);
        if (value == null) {
            throw new UndefineVariableException("Failed to find value for variable [name='%s']".formatted(key));
        }
        return value;
    }

    public <T> T getValue(String name, Class<T> clazz) {
        try {
            return clazz.cast(get(name));
        } catch (Exception e) {
            throw new TypeCastingException("Can't converted the value to specific type [%s]".formatted(clazz.getName()));
        }
    }

    private void registerMethods(StandardEvaluationContext context) {
        for (Method method : UtilityMethods.class.getDeclaredMethods()) {
            ResolvableMethod annotation = method.getDeclaredAnnotation(ResolvableMethod.class);
            if (annotation != null) {
                context.registerFunction(annotation.value(), method);
            }
        }
    }
}

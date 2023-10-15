package com.mcubes.webtest.core;

import com.mcubes.webtest.annotation.ResolvableMethod;
import com.mcubes.webtest.exception.TypeCastingException;
import com.mcubes.webtest.exception.UndefineVariableException;
import com.mcubes.webtest.util.UtilityMethods;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Map;

public class StepContext {
    private static StepContext context;
    private final StandardEvaluationContext local;
    private final ThreadLocal<StandardEvaluationContext> global;

    private StepContext() {
        this.local = null;
        StandardEvaluationContext context = new StandardEvaluationContext();
        registerMethods(context);
        this.global = ThreadLocal.withInitial(() -> context);
    }

    private StepContext(StandardEvaluationContext context) {
        registerMethods(context);
        this.local = context;
        this.global = null;
    }

    public synchronized static StepContext getInstance() {
        if (context == null) {
            context = new StepContext();
        }
        return context;
    }

    public StepContext getInstance(Map<String, Object> variables) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariables(variables);
        return new StepContext(context);
    }

    public StandardEvaluationContext context() {
        return local != null ? local : global.get();
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





    /*
    public boolean getBool(String name) {
        if (get(name) instanceof Boolean value) return value;
        throw new TypeCastingException(name, "boolean");
    }

    public long getLong(String name) {
        if (get(name) instanceof Long value) return value;
        throw new TypeCastingException(name, "integer");
    }

    public double getDouble(String name) {
        if (get(name) instanceof Double value) return value;
        throw new TypeCastingException(name, "float");
    }

    public String getString(String name) {
        if (get(name) instanceof String value) return value;
        throw new TypeCastingException(name, "string");
    }

    public WebElement getWebElement(String name) {
        if (get(name) instanceof WebElement element) return element;
        throw new TypeCastingException(name, "web_element");
    }

    public List<WebElement> getListOfWebElement(String name) {
        try {
            return (List<WebElement>) get(name);
        } catch (Exception ex) {
            throw new TypeCastingException(name, "web_elements");
        }
    }

    public Collection<?> getCollection(String name) {
        if (get(name) instanceof Collection<?> value) return value;
        throw new TypeCastingException(name, "iterable");
    }

    public String getValueAsString(String name) {
        Object value = get(name);
        return value.toString();
    }
     */

}

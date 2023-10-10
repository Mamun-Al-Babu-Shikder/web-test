package com.mcubes.webtest.core;

import com.mcubes.webtest.enums.VariableType;
import com.mcubes.webtest.exception.TypeCastingException;
import com.mcubes.webtest.exception.UndefineVariableException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GlobalStorageForStepExecution {
    private static final ThreadLocal<Map<String, Object>> local = ThreadLocal.withInitial(HashMap::new);

    private static Map<String, Object> map() {
        return local.get();
    }

    public static void set(String key, Object value) {
        map().put(key, value);
    }

    private static Object get(String key) {
        Object value = map().get(key);
        if (value == null) {
            throw new UndefineVariableException("Failed to find value for variable [name='%s']".formatted(key));
        }
        return value;
    }

    public static boolean getBool(String name) {
         if (get(name) instanceof Boolean value) return value;
         throw new TypeCastingException(name, VariableType.BOOLEAN);
    }

    public static long getLong(String name) {
        if (get(name) instanceof Long value) return value;
        throw new TypeCastingException(name, VariableType.INTEGER);
    }

    public static double getDouble(String name) {
        if (get(name) instanceof Double value) return value;
        throw new TypeCastingException(name, VariableType.FLOAT);
    }

    public static String getString(String name) {
        if (get(name) instanceof String value) return value;
        throw new TypeCastingException(name, VariableType.STRING);
    }

    public static Collection<?> getCollection(String name) {
        if (get(name) instanceof Collection<?> value) return value;
        throw new TypeCastingException(name, "iterable");
    }

    public static String getValueAsString(String name) {
        Object value = get(name);
        return value.toString();
    }

}

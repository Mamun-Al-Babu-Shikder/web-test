package com.mcubes.webtest.core;

import java.util.HashMap;
import java.util.Map;

public class ThreadLocalStorage {
    private static final ThreadLocal<Map<String, Object>> local = ThreadLocal.withInitial(HashMap::new);

    private static Map<String, Object> map() {
        return local.get();
    }

    public static Object get(String key) {
        return map().get(key);
    }

    public static void set(String key, Object value) {
        map().put(key, value);
    }
}

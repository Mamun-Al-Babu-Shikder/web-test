package com.mcubes.webtest.enums;

public enum NavigationType {
    REFRESH, BACK, FORWARD, TO;

    public static NavigationType from(String type) {
        return NavigationType.valueOf(type.toUpperCase());
    }
}

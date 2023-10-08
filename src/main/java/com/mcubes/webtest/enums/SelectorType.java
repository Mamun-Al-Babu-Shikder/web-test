package com.mcubes.webtest.enums;

public enum SelectorType {
    TAG, NAME, ID, XPATH, CLASS_NAME, CSS_SELECTOR, LINK_TEXT, PARTIAL_LINK_TEXT;

    public static SelectorType from(String type) {
        return SelectorType.valueOf(type.toUpperCase());
    }
}

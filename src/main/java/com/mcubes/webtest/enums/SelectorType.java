package com.mcubes.webtest.enums;

import com.mcubes.webtest.exception.InvalidTypeException;

import static com.mcubes.webtest.constants.JsonAttributeKeys.SELECTOR_TYPE;

public enum SelectorType {
    TAG, NAME, ID, XPATH, CLASS_NAME, CSS_SELECTOR, LINK_TEXT, PARTIAL_LINK_TEXT;

    public static SelectorType from(String type) {
        String trimmedType = type.trim();
        try {
            return SelectorType.valueOf(trimmedType.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new InvalidTypeException("Failed to resolve [%s='%s']".formatted(SELECTOR_TYPE, trimmedType));
        }
    }
}

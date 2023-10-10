package com.mcubes.webtest.enums;

import com.mcubes.webtest.exception.InvalidTypeException;

import java.util.Arrays;

public enum ActionType {
    OPEN_URL("open_url"), NAVIGATION("navigation"), CLICK("click"), ENTER_TEXT("enter_text"),
    DEF_VAR("def_var"), FOREACH_LOOP("foreach_loop"),
    DELAY("delay"), PRINT("print"), PRINTLN("println")
    ;

    private final String value;

    ActionType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static ActionType from(String type) {
        final String trimmedType = type.trim();
        return Arrays.stream(ActionType.values())
                .filter(p -> p.value().equals(trimmedType))
                .findFirst()
                .orElseThrow(() -> new InvalidTypeException("Failed to resolve step type [type='%s']".formatted(trimmedType)));
    }
}

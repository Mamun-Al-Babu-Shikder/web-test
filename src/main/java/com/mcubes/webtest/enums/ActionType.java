package com.mcubes.webtest.enums;

import com.mcubes.webtest.exception.InvalidTypeException;

import java.util.Arrays;

public enum ActionType {
    OPEN_URL("open_url"),
    OPEN_WINDOW("open_window"),
    SWITCH_WINDOW("switch_window"),
    MANAGE_WINDOW("manage_window"),
    NAVIGATION("navigation"),
    FIND_ELEMENT("find_element"),
    FIND_ELEMENTS("find_elements"),
    WEB_ELEMENT_OPT("web_element_opt"),
    @Deprecated
    CLICK("click"),
    @Deprecated
    ENTER_TEXT("enter_text"),

    DEFINE_VAR("def_var"),
    IF_STATEMENT("if"),
    IF_ELSE_STATEMENT("if_else"),
    SWITCH_CASE_STATEMENT("switch"),
    FOREACH_LOOP("foreach"),
    WHILE_LOOP("while"),
    DELAY("delay"),
    PRINT("print"),
    PRINTLN("println"),
    EVALUATION("eval")
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
                .orElseThrow(() -> new InvalidTypeException("Failed to resolve step action type ['%s']".formatted(trimmedType)));
    }
}

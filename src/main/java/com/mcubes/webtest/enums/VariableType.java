package com.mcubes.webtest.enums;

import com.mcubes.webtest.exception.InvalidTypeException;

import java.util.Arrays;

public enum VariableType {
    BOOLEAN("bol"), INTEGER("int"), FLOAT("float"), STRING("str"),
    ELEMENT("element"), ELEMENT_LIST("list<element>");

    private final String value;

    VariableType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static VariableType from(String type) {
        return Arrays.stream(VariableType.values())
                .filter(p -> p.getValue().equals(type))
                .findFirst()
                .orElseThrow(InvalidTypeException::new);
    }
}

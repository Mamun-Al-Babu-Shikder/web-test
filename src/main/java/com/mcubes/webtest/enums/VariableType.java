package com.mcubes.webtest.enums;

import com.mcubes.webtest.exception.InvalidAttributeValueException;
import com.mcubes.webtest.exception.InvalidTypeException;

import java.util.Arrays;

import static com.mcubes.webtest.enums.ActionType.DEF_VAR;

public enum VariableType {
    BOOLEAN("bool", "true|false"), INTEGER("int", "[+-]?\\d{1,15}"),
    FLOAT("float", "[+-]?(\\d{1,15}|\\d{0,15}\\.\\d{1,15})"), STRING("str", ".+"),
    STRING_LIST("list<str>", "\\[\\s*'[^']*'\\s*(,\\s*'[^']*'\\s*)*]"),
    ELEMENT("element", ".+"), ELEMENT_LIST("list<element>", ".+");

    private final String type;
    private final String regex;

    VariableType(String type, String regex) {
        this.type = type;
        this.regex = regex;
    }

    public String getType() {
        return type;
    }

    public void validateValueWithThrow(String value) {
        if (!value.matches(regex)) {
            throw new InvalidAttributeValueException("Invalid value [%s] found for variable type ['%s']".formatted(value, this.getType()));
        }
    }

    public boolean isIterable() {
        return switch (this) {
            case STRING_LIST, ELEMENT_LIST -> true;
            default -> false;
        };
    }

    public static VariableType from(String type) {
        final String trimmedType = type.trim();
        return Arrays.stream(VariableType.values())
                .filter(p -> p.getType().equals(trimmedType))
                .findFirst()
                .orElseThrow(() -> new InvalidTypeException("Failed to resolve variable type ['%s'] at step type [type='%s']".formatted(trimmedType, DEF_VAR.value())));
    }
}

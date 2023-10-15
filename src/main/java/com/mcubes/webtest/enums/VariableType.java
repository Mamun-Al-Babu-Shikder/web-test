package com.mcubes.webtest.enums;

public enum VariableType {
    BOOLEAN("bool", "true|false"), INTEGER("int", "[+-]?\\d{1,15}"),
    FLOAT("float", "[+-]?(\\d{1,15}|\\d{0,15}\\.\\d{1,15})"), STRING("str", ".+"),
    STRING_LIST("list<str>", "\\[\\s*'[^']*'\\s*(,\\s*'[^']*'\\s*)*]"),
    ELEMENT("element", ".+"), ELEMENT_LIST("list<element>", ".+"),
    //LIST("list", "")
    ;

    private final String type;
    private final String regex;

    VariableType(String type, String regex) {
        this.type = type;
        this.regex = regex;
    }

    public String getType() {
        return type;
    }
}

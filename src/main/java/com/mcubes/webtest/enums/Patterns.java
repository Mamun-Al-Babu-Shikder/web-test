package com.mcubes.webtest.enums;


import static com.mcubes.webtest.util.VariableHandler.SQM;

public enum Patterns {
    INT_VALUE("^-?\\d{1,15}$"), FLOAT_VALUE("^-?(\\d{1,15}|\\d{0,15}\\.\\d{1,15})$"),
    STRING_VALUE("^"+SQM+"[^"+SQM+"]+"+SQM+"$"), VAR_NAME("^[_$a-z][\\w$]*$"),
    EVAL_VAR_NAME("^#[_$a-z][\\w$]*$"), EVAL_VAR_NAME2("\\$\\{[a-zA-Z_]\\w*}"),
    DELAY_TIME("\\d{1,15}"),
    IS_REQUIRED_EVAL("^\\$\\{.+}$")
    ;

    private final String pattern;

    Patterns(String pattern) {
        this.pattern = pattern;
    }

    public String pattern() {
        return pattern;
    }
}

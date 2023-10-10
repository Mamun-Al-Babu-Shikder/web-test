package com.mcubes.webtest.constants;

public class Constants {
    public static final String VAR_PREFIX = "${";
    public static final String VAR_SUFFIX = "}";
    public static final String VAR_NAME_PATTERN = "[a-zA-Z_]\\w+";
    public static final String EVAL_VAR_NAME_PATTERN = "\\$\\{[a-zA-Z_]\\w+}";
    public static final String VAR_RESOLVE_PATTERN = "\\$\\{\\w+}";
    public static final String MATH_RESOLVE_PATTERN = "\\$\\{[\\w|+|-|\\*|/|%|\\s]+}";
    public static final String NUM_PATTERN = "[+-]?\\d+";
    public static final String POS_NUM_PATTERN = "[+]?\\d+";
    public static final String NEG_NUM_PATTERN = "[-]?\\d+";
    public static final String DELAY_TIME_PATTERN = "[+]?\\d{1,15}";
}

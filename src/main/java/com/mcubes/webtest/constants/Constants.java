package com.mcubes.webtest.constants;

public class Constants {
    public static final String VAR_PREFIX = "${";
    public static final String VAR_SUFFIX = "}";
    public static final String VAR_NAME_PATTERN = "\\w+";
    public static final String VAR_RESOLVE_PATTERN = "\\$\\{\\w+}";
    public static final String MATH_RESOLVE_PATTERN = "\\$\\{[\\w|+|-|\\*|/|%|\\s]+}";
    public static final String NUM_PATTERN = "[+-]?\\d+";
}

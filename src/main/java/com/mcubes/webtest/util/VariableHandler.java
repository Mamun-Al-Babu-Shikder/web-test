package com.mcubes.webtest.util;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mcubes.webtest.enums.Patterns.*;

public class VariableHandler {
    public static final char SQM = '`';

    public static Object getValue(String input) {
        input = input.trim();
        List<Function<String, Object>> functions = Arrays.asList(
                VariableHandler::boolValue,
                VariableHandler::intValue,
                VariableHandler::floatValue,
                VariableHandler::stringValue,
                VariableHandler::list,
                VariableHandler::map
        );
        for (Function<String, Object> fun : functions) {
            Object value = fun.apply(input);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    private static Object boolValue(String input) {
        if (input.equals("true") || input.equals("false")) {
            return Boolean.valueOf(input);
        }
        return null;
    }

    private static Object intValue(String input) {
        String value = extractValue(INT_VALUE.pattern(), input);
        return value != null ? Long.valueOf(value) : null;
    }

    private static Object floatValue(String input) {
        String value = extractValue(FLOAT_VALUE.pattern(), input);
        return value != null ? Double.valueOf(value) : null;
    }

    private static Object stringValue(String input) {
        String value = extractValue(STRING_VALUE.pattern(), input);
        if (value == null) return null;
        value = value.substring(1, value.length() - 1);
        return value;
    }

    private static String extractValue(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches() ? matcher.group() : null;
    }

    private static Object list(String input) {
        if (input.startsWith("[") && input.endsWith("]")) {
            input = input.substring(1, input.length() - 1);
            return nestedList(input);
        }
        return null;
    }

    private static Object nestedList(String input) {
        List<Object> list = new LinkedList<>();
        String[] elements = splitNested(input);
        for (String element : elements) {
            Object value = getValue(element.trim());
            if (value == null) {
                return null;
            }
            list.add(value);
        }
        return list;
    }


    private static Object map(String input) {
        if (input.startsWith("{") && input.endsWith("}")) {
            input = input.substring(1, input.length() - 1);
            return nestedMap(input);
        }
        return null;
    }

    private static Object nestedMap(String input) {
        Map<Object, Object> map = new LinkedHashMap<>();
        String[] keyValues = splitNested(input);
        for (String keyValue : keyValues) {
            String[] parts = keyValue.split(":", 2);
            if (parts.length != 2) {
                return null;
            }

            Object key = getValue(parts[0].trim());
            Object value = getValue(parts[1].trim());
            if (key == null || value == null) {
                return null;
            }
            map.put(key, value);
        }
        return map;
    }

    private static String[] splitNested(String input) {
        int level = 0;
        StringBuilder currentElement = new StringBuilder();
        String[] elements = new String[]{};
        boolean stringStart = false;
        for (char c : input.toCharArray()) {
            if (c == '[' || c == '{') {
                level++;
            } else if (c == ']' || c == '}') {
                level--;
            }

            if (c == ',' && level == 0 && !stringStart) {
                currentElement = new StringBuilder(currentElement.toString().trim());
                elements = appendToArray(elements, currentElement.toString());
                currentElement = new StringBuilder();
            } else {
                currentElement.append(c);
                stringStart ^= c == SQM;
            }
        }

        currentElement = new StringBuilder(currentElement.toString().trim());
        elements = appendToArray(elements, currentElement.toString());

        return elements;
    }

    private static String[] appendToArray(String[] array, String element) {
        String[] newArray = new String[array.length + 1];
        System.arraycopy(array, 0, newArray, 0, array.length);
        newArray[array.length] = element;
        return newArray;
    }

}

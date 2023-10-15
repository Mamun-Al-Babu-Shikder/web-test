package com.mcubes.webtest.util;

import com.mcubes.webtest.enums.SelectorType;
import com.mcubes.webtest.exception.InvalidAttributeValueException;
import com.mcubes.webtest.exception.WebElementNotFoundException;
import com.mcubes.webtest.exception.WebElementSelectionException;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.mcubes.webtest.constants.Constants.VAR_PREFIX;
import static com.mcubes.webtest.enums.Patterns.EVAL_VAR_NAME;
import static com.mcubes.webtest.enums.Patterns.VAR_NAME;

public class Utils {

    public static WebElement resolveWebElementFrom(WebDriver driver, SelectorType selectorType, String selector) {
        try {
            By by = resolveByFromSelector(selectorType, selector);
            return driver.findElement(by);
        } catch (NoSuchElementException e) {
            throw new WebElementNotFoundException("Web element not found according to [selector='%s'] by [selector_type='%s']".formatted(selector, selectorType.name().toLowerCase()));
        }
    }

    public static List<WebElement> resolveWebElementsFrom(WebDriver driver, SelectorType selectorType, String selector) {
        try {
            By by = resolveByFromSelector(selectorType, selector);
            return driver.findElements(by);
        } catch (NoSuchElementException e) {
            throw new WebElementNotFoundException("Web elements not found according to [selector='%s'] by [selector_type='%s']".formatted(selectorType.name().toLowerCase(), selector));
        }
    }

    private static By resolveByFromSelector(SelectorType selectorType, String value) {
        try {
            return switch (selectorType) {
                case TAG -> By.tagName(value);
                case NAME -> By.name(value);
                case ID -> By.id(value);
                case XPATH -> By.xpath(value);
                case CLASS_NAME -> By.className(value);
                case CSS_SELECTOR -> By.cssSelector(value);
                case LINK_TEXT -> By.linkText(value);
                case PARTIAL_LINK_TEXT -> By.partialLinkText(value);
            };
        } catch (Exception ex) {
            throw new WebElementSelectionException("Found invalid web element selector [selector=%s]".formatted(selectorType.name().toLowerCase()));
        }
    }


    public static String validateAndGetEvalVarName(String varName) {
        if (varName != null) {
            varName = varName.trim();
            if (varName.matches(EVAL_VAR_NAME.pattern())) {
                return varName.replace(VAR_PREFIX, "");
            }
        }
        throw new InvalidAttributeValueException("Invalid variable name found [name=%s]".formatted(varName));
    }

    public static String validateAndGetVarName(String varName) {
        if (varName != null) {
            varName = varName.trim();
            if (varName.matches(VAR_NAME.pattern())) {
                return varName;
            }
        }
        throw new InvalidAttributeValueException("Invalid variable name found [name=%s]".formatted(varName));
    }

    /*
    public static String resolveVariables(String value) {
        Pattern pattern = Pattern.compile(EVAL_VAR_NAME.pattern());
        Matcher matcher = pattern.matcher(value);
        Map<String, String> replacements = new LinkedHashMap<>();
        while (matcher.find()) {
            String varName = matcher.group();
            String actualName = validateAndGetActualVarName(matcher.group());
            String strValue = GlobalStorageForStepExecution.getValueAsString(actualName);
            replacements.put(varName, strValue);
        }
        if (replacements.isEmpty()) {
            return value;
        }
        return resolvePatterns(value, replacements);
    }

    private static String resolvePatterns(String input, Map<String, String> replacements) {
        StringBuilder modifiedString = new StringBuilder();
        StringBuilder currentPattern = new StringBuilder();
        boolean inPattern = false;
        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            if (currentChar == '$' && i + 2 < input.length() && input.charAt(i + 1) == '{') {
                inPattern = true;
                currentPattern.setLength(0);
                currentPattern.append(currentChar);
                currentPattern.append(input.charAt(i + 1));
                i++;
            } else if (inPattern && currentChar == '}') {
                currentPattern.append(currentChar);
                String key = currentPattern.toString();
                String replacement = replacements.getOrDefault(key, key);
                modifiedString.append(replacement);
                inPattern = false;
                currentPattern.setLength(0);
            } else if (inPattern) {
                currentPattern.append(currentChar);
            } else {
                modifiedString.append(currentChar);
            }
        }
        return modifiedString.toString();
    }

     */


}

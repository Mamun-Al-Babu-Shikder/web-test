package com.mcubes.webtest.util;

import com.mcubes.webtest.core.ThreadLocalStorage;
import com.mcubes.webtest.enums.SelectorType;
import com.mcubes.webtest.exception.IllegalAttributeException;
import com.mcubes.webtest.exception.UndefineVariableException;
import com.mcubes.webtest.exception.WebElementNotFoundException;
import com.mcubes.webtest.exception.WebElementSelectionException;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mcubes.webtest.constants.Constants.*;

public class Utils {

    public static WebElement resolveWebElementFrom(WebDriver driver, String selector) {
        try {
            By by = resolveByFromSelector(selector);
            return driver.findElement(by);
        } catch (NoSuchElementException e) {
            throw new WebElementNotFoundException("web element not found according to [selector=%s]".formatted(selector));
        }
    }

    public static List<WebElement> resolveWebElementsFrom(WebDriver driver, String selector) {
        try {
            By by = resolveByFromSelector(selector);
            return driver.findElements(by);
        } catch (NoSuchElementException e) {
            throw new WebElementNotFoundException("web elements not found according to [selector=%s]".formatted(selector));
        }
    }

    public static By resolveByFromSelector(String selector) {
        try {
            String[] typeValuePair = selector.split(":", 2);
            SelectorType selectorType = SelectorType.from(typeValuePair[0]);
            String value = typeValuePair[1];
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
            throw new WebElementSelectionException("found invalid [selector=%s], define selector prefix properly".formatted(selector));
        }
    }

    public static String resolveVariables(String value) {
        Pattern pattern = Pattern.compile(VAR_RESOLVE_PATTERN);
        Matcher matcher = pattern.matcher(value);
        while (matcher.find()) {
            String varName = matcher.group();
            String actualName = varName.replace(VAR_PREFIX, "").replace(VAR_SUFFIX, "");
            Object obj = ThreadLocalStorage.get(actualName);
            if (obj == null) {
                throw new UndefineVariableException(actualName);
            }
            value = value.replace(varName, obj.toString());
        }
        return value;
    }

    public static String validateAndGetActualVarName(String value) {
        String varName = value.trim();
        if (varName.matches(VAR_RESOLVE_PATTERN)) {
            return varName.replace(VAR_PREFIX, "").replace(VAR_SUFFIX, "");
        }
        throw new IllegalAttributeException("invalid variable name found [variable_name=%s]".formatted(varName));
    }

    public static String getActualVarName(String value) {
        try {
            return validateAndGetActualVarName(value);
        } catch (IllegalAttributeException ex) {
            return null;
        }
    }

    private static String evaluationMathExpression(String value) {
        Pattern pattern = Pattern.compile(MATH_RESOLVE_PATTERN);
        Matcher matcher = pattern.matcher(value);
        while (matcher.find()) {
            String varName = matcher.group();
            String expression = varName.replace(VAR_PREFIX, "")
                    .replace(VAR_SUFFIX, "")
                    .replaceAll("\\s+", "");
            System.out.println(doEvaluationOnExpression(expression));
        }
        return value;
    }

    private static String doEvaluationOnExpression(String value) {
        Pattern pattern = Pattern.compile(VAR_NAME_PATTERN);
        Matcher matcher = pattern.matcher(value);
        while (matcher.find()) {
            String varName = matcher.group();
            if (!Character.isDigit(varName.charAt(0))) {
                Object obj = ThreadLocalStorage.get(varName);
                value = value.replace(varName, obj.toString());
            }
        }
        return value;
    }
}

package com.mcubes.webtest.util;

import com.mcubes.webtest.annotation.ResolvableMethod;
import com.mcubes.webtest.core.StepContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.*;

public class UtilityMethods {

    @ResolvableMethod("list")
    public static List<Object> createList(Object... values) {
        return new ArrayList<>(List.of(values));
    }

    @ResolvableMethod("set")
    public static Set<Object> createSet(Object... values) {
        return new TreeSet<>(Set.of(values));
    }

    @ResolvableMethod("is_null")
    public static Boolean isNull(Object object) {
        return object == null;
    }

    @ResolvableMethod("str")
    public static String objectToString(Object object) {
        return object.toString();
    }

    @ResolvableMethod("click")
    public static void clickOnWebElement(WebElement element) {
        element.click();
    }

    @ResolvableMethod("enter_text")
    public static void enterTextOnWebElement(WebElement element, CharSequence... charSequences) {
        element.sendKeys(charSequences);
    }

    @ResolvableMethod("clear")
    public static void clearWebElement(Object object) {
        if (object instanceof WebElement element) {
            element.clear();
        }
    }

    @ResolvableMethod("first_window")
    public static String firstWindowHandler() {
        return listOfWindowHandlers().get(0);
    }

    @ResolvableMethod("last_window")
    public static String lastWindowHandler() {
        List<String> handlers = listOfWindowHandlers();
        return handlers.get(handlers.size() - 1);
    }

    private static List<String> listOfWindowHandlers() {
        StepContext context = StepContext.getInstance();
        WebDriver driver = context.getWebDriver();
        if (driver == null || driver.getWindowHandles().isEmpty()) {
            throw new RuntimeException("Window not found!");
        }
        return List.copyOf(driver.getWindowHandles());
    }

    @ResolvableMethod("is_displayed")
    public static boolean isWebElementDisplayed(WebElement element) {
        return element.isDisplayed();
    }

}


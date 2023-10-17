package com.mcubes.webtest.util;

import com.mcubes.webtest.annotation.ResolvableMethod;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class UtilityMethods {

    @ResolvableMethod("list")
    public static List<Object> createList() {
        return new ArrayList<>();
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

}


package com.mcubes.webtest.actions.web;

import com.mcubes.webtest.actions.Action;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static com.mcubes.webtest.util.Utils.resolveWebElementFrom;

public abstract class AbstractWebElementAction implements Action {
    private final String selector;

    protected AbstractWebElementAction(String selector) {
        this.selector = selector;
    }

    @Override
    public void trigger(WebDriver driver) {
        WebElement element = resolveWebElementFrom(driver, selector);
        trigger(element);
    }

    protected abstract void trigger(WebElement element);
}

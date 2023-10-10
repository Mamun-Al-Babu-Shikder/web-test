package com.mcubes.webtest.actions.web;

import com.mcubes.webtest.actions.Action;
import com.mcubes.webtest.enums.SelectorType;
import com.mcubes.webtest.enums.VariableType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static com.mcubes.webtest.util.Utils.resolveWebElementFrom;

public abstract class AbstractWebElementAction implements Action {
    private final SelectorType selectorType;
    private final String selector;

    public AbstractWebElementAction(SelectorType selectorType, String selector) {
        this.selectorType = selectorType;
        this.selector = selector;
    }

    @Override
    public void trigger(WebDriver driver) {
        WebElement element = resolveWebElementFrom(driver, selectorType, selector);
        trigger(element);
    }

    protected abstract void trigger(WebElement element);
}

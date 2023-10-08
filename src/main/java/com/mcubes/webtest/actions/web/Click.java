package com.mcubes.webtest.actions.web;

import com.mcubes.webtest.core.ThreadLocalStorage;
import org.openqa.selenium.WebElement;

public class Click extends AbstractWebElementAction {
    public Click(String selector) {
        super(selector);
    }

    @Override
    protected void trigger(WebElement element) {
        element.click();
    }
}

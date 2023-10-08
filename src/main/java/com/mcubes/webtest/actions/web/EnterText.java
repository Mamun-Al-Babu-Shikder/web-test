package com.mcubes.webtest.actions.web;

import com.mcubes.webtest.util.Utils;
import org.openqa.selenium.WebElement;

public class EnterText extends AbstractWebElementAction {
    private final String text;

    public EnterText(String selector, String text) {
        super(selector);
        this.text = text;
    }

    @Override
    protected void trigger(WebElement element) {
        String text = Utils.resolveVariables(this.text);
        element.sendKeys(text);
    }
}

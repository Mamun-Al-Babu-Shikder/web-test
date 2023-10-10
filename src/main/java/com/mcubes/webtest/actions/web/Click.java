package com.mcubes.webtest.actions.web;

import com.mcubes.webtest.enums.SelectorType;
import org.json.JSONObject;
import org.openqa.selenium.WebElement;

import static com.mcubes.webtest.constants.JsonAttributeKeys.SELECTOR;
import static com.mcubes.webtest.constants.JsonAttributeKeys.SELECTOR_TYPE;

public class Click extends AbstractWebElementAction {
    public Click(SelectorType selectorType, String selector) {
        super(selectorType, selector);
    }

    public static Click from(JSONObject object) {
        String type = object.getString(SELECTOR_TYPE).trim();
        String selector = object.getString(SELECTOR);
        return new Click(SelectorType.from(type), selector);
    }

    @Override
    protected void trigger(WebElement element) {
        element.click();
    }
}

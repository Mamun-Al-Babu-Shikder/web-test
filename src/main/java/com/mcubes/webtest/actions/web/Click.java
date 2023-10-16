package com.mcubes.webtest.actions.web;

import com.mcubes.webtest.core.StepContext;
import com.mcubes.webtest.enums.SelectorType;
import com.mcubes.webtest.util.UtilityMethods;
import org.json.JSONObject;
import org.openqa.selenium.WebElement;

import static com.mcubes.webtest.constants.JsonAttributeKeys.SELECTOR;
import static com.mcubes.webtest.constants.JsonAttributeKeys.SELECT_BY;

public class Click extends AbstractWebElementAction {
    public Click(SelectorType selectorType, String selector) {
        super(selectorType, selector);
    }

    public static Click from(JSONObject object) {
        String type = object.getString(SELECT_BY).trim();
        String selector = object.getString(SELECTOR);
        return new Click(SelectorType.from(type), selector);
    }

    @Override
    protected void trigger(StepContext stepContext, WebElement element) {
        UtilityMethods.clickOnWebElement(element);
    }
}

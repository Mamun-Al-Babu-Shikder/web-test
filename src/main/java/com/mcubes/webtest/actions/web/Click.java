package com.mcubes.webtest.actions.web;

import com.mcubes.webtest.core.StepContext;
import com.mcubes.webtest.util.UtilityMethods;
import org.json.JSONObject;
import org.openqa.selenium.WebElement;

@Deprecated
public class Click extends AbstractWebElementAction {

    public Click(JSONObject object) {
        super(object);
    }

    @Override
    protected void trigger(StepContext stepContext, WebElement element) {
        UtilityMethods.clickOnWebElement(element);
    }
}

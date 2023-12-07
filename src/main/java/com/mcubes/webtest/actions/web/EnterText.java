package com.mcubes.webtest.actions.web;

import com.mcubes.webtest.core.ExpEvaluator;
import com.mcubes.webtest.core.StepContext;
import com.mcubes.webtest.util.UtilityMethods;
import org.json.JSONObject;
import org.openqa.selenium.WebElement;

import static com.mcubes.webtest.constants.JsonAttributeKeys.TEXT;

public class EnterText extends AbstractWebElementAction {
    private final String text;

    public EnterText(JSONObject object) {
        super(object);
        this.text = object.getString(TEXT);
    }

    @Override
    protected void trigger(StepContext stepContext, WebElement element) {
        String text = ExpEvaluator.evalExpIfNeeded(stepContext, this.text, String.class);
        UtilityMethods.enterTextOnWebElement(element, text);
    }
}

package com.mcubes.webtest.actions.web;

import com.mcubes.webtest.core.ExpEvaluator;
import com.mcubes.webtest.core.StepContext;
import com.mcubes.webtest.enums.SelectorType;
import com.mcubes.webtest.util.UtilityMethods;
import org.json.JSONObject;
import org.openqa.selenium.WebElement;

import static com.mcubes.webtest.constants.JsonAttributeKeys.*;

public class EnterText extends AbstractWebElementAction {
    private final String text;

    public EnterText(SelectorType selectorType, String selector, String text) {
        super(selectorType, selector);
        this.text = text;
    }

    public static EnterText from(JSONObject object) {
        String type = object.getString(SELECT_BY).trim();
        String selector = object.getString(SELECTOR);
        String text = object.getString(TEXT);
        return new EnterText(SelectorType.from(type), selector, text);
    }

    @Override
    protected void trigger(StepContext stepContext, WebElement element) {
        String text = ExpEvaluator.evalExpIfNeeded(stepContext, this.text, String.class);
        UtilityMethods.enterTextOnWebElement(element, text);
    }
}

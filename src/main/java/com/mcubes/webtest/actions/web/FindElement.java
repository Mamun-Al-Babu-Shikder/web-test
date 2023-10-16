package com.mcubes.webtest.actions.web;

import com.mcubes.webtest.core.StepContext;
import com.mcubes.webtest.enums.SelectorType;
import com.mcubes.webtest.util.Utils;
import org.json.JSONObject;
import org.openqa.selenium.WebElement;

import static com.mcubes.webtest.constants.JsonAttributeKeys.*;

public class FindElement extends AbstractWebElementAction {
    private final String varName;
    public FindElement(SelectorType selectorType, String selector, String varName) {
        super(selectorType, selector);
        this.varName = varName;
    }

    public static FindElement from(JSONObject object) {
        String selectorType = object.getString(SELECT_BY).trim();
        String selector = object.getString(SELECTOR);
        String varName = Utils.validateAndGetVarName(object.getString(VAR));
        return new FindElement(SelectorType.from(selectorType), selector, varName);
    }

    @Override
    protected void trigger(StepContext stepContext, WebElement element) {
        stepContext.set(varName, element);
    }
}

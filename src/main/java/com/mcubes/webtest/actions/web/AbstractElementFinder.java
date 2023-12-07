package com.mcubes.webtest.actions.web;

import com.mcubes.webtest.actions.Action;
import com.mcubes.webtest.core.StepContext;
import com.mcubes.webtest.core.WebElementProvider;
import com.mcubes.webtest.util.Utils;
import org.json.JSONObject;

import static com.mcubes.webtest.constants.JsonAttributeKeys.VAR;

public abstract class AbstractElementFinder implements Action {
    private final String varName;
    private final WebElementProvider webElementProvider;

    public AbstractElementFinder(JSONObject object) {
        this.webElementProvider = WebElementProvider.from(object);
        this.varName = Utils.validateAndGetVarName(object.getString(VAR));
    }

    @Override
    public void trigger(StepContext stepContext) {
        if (this instanceof FindElement) {
            stepContext.set(varName, webElementProvider.getWebElement(stepContext));
        } else if (this instanceof FindElements) {
            stepContext.set(varName, webElementProvider.getWebElements(stepContext));
        }
    }
}

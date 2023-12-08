package com.mcubes.webtest.actions.web;

import com.mcubes.webtest.actions.Action;
import com.mcubes.webtest.core.ExpEvaluator;
import com.mcubes.webtest.core.StepContext;
import com.mcubes.webtest.core.WebElementProvider;
import org.json.JSONObject;
import org.openqa.selenium.WebElement;

import static com.mcubes.webtest.constants.JsonAttributeKeys.ELEMENT;

@Deprecated
public abstract class AbstractWebElementAction implements Action {
    private String elementObject;
    private WebElementProvider webElementProvider;

    public AbstractWebElementAction(JSONObject object) {
        Object elementObject = object.get(ELEMENT);
        if (elementObject instanceof JSONObject obj) {
            this.webElementProvider = WebElementProvider.from(obj);
        } else {
            this.elementObject = elementObject.toString();
        }
    }

    @Override
    public void trigger(StepContext stepContext) {
        WebElement element = webElementProvider != null ? webElementProvider.getWebElement(stepContext)
                : ExpEvaluator.evaluate(stepContext, elementObject, WebElement.class);
        trigger(stepContext, element);
    }

    protected abstract void trigger(StepContext stepContext, WebElement element);
}

package com.mcubes.webtest.actions.web;

import com.mcubes.webtest.actions.Action;
import com.mcubes.webtest.core.ExpEvaluator;
import com.mcubes.webtest.core.StepContext;
import com.mcubes.webtest.enums.SelectorType;
import org.openqa.selenium.WebElement;

import static com.mcubes.webtest.util.Utils.resolveWebElementFrom;

public abstract class AbstractWebElementAction implements Action {
    private final SelectorType selectorType;
    private final String selector;

    public AbstractWebElementAction(SelectorType selectorType, String selector) {
        this.selectorType = selectorType;
        this.selector = selector;
    }

    @Override
    public void trigger(StepContext stepContext) {
        String selector = ExpEvaluator.evalExpIfNeeded(stepContext, this.selector, String.class);
        WebElement element = resolveWebElementFrom(stepContext.getWebDriver(), selectorType, selector);
        trigger(stepContext, element);
    }

    protected abstract void trigger(StepContext stepContext, WebElement element);
}

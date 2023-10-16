package com.mcubes.webtest.actions.web;

import com.mcubes.webtest.actions.Action;
import com.mcubes.webtest.core.StepContext;

public class OpenURL implements Action {
    private final String url;

    public OpenURL(String url) {
        this.url = url;
    }

    @Override
    public void trigger(StepContext stepContext) {
        stepContext.getWebDriver().get(url);
    }
}

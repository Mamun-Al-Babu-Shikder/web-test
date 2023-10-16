package com.mcubes.webtest.actions.web;

import com.mcubes.webtest.actions.Action;
import com.mcubes.webtest.core.ExpEvaluator;
import com.mcubes.webtest.core.StepContext;
import com.mcubes.webtest.enums.NavigationType;
import org.openqa.selenium.WebDriver;

public class Navigator implements Action {
    private final String url;
    private final NavigationType navigationType;

    public Navigator(NavigationType navigationType, String url) {
        this.url = url;
        this.navigationType = navigationType;
    }

    @Override
    public void trigger(StepContext stepContext) {
        WebDriver.Navigation navigation = stepContext.getWebDriver().navigate();
        switch (navigationType) {
            case REFRESH -> navigation.refresh();
            case BACK -> navigation.back();
            case FORWARD -> navigation.forward();
            case TO -> navigation.to(ExpEvaluator.evalExpIfNeeded(stepContext, url, String.class));
        }
    }
}

package com.mcubes.webtest.actions.web;

import com.mcubes.webtest.actions.Action;
import com.mcubes.webtest.core.ExpEvaluator;
import com.mcubes.webtest.core.StepContext;
import com.mcubes.webtest.exception.AttributeNotFoundException;
import com.mcubes.webtest.exception.InvalidAttributeValueException;
import com.mcubes.webtest.util.Utils;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;

import static com.mcubes.webtest.constants.JsonAttributeKeys.*;
import static com.mcubes.webtest.enums.ActionType.OPEN_WINDOW;

public class OpenWindow implements Action {
    private static final String NEW_WINDOW = "NEW_WINDOW";
    private static final String NEW_TAB = "NEW_TAB";

    private final String url;
    private final String to;
    private final String handlerVarName;

    public OpenWindow(String url, String to, String handlerVarName) {
        this.url = url;
        this.to = to;
        this.handlerVarName = handlerVarName;
    }

    public static OpenWindow from(JSONObject object) {
        String url = object.optString(URL, null);
        String to = object.optString(TO, null);

        if (url == null && to == null) {
            throw new AttributeNotFoundException("Attribute 'url' and 'to' missing for step action type '%s'. Please provide both or one of them.".formatted(OPEN_WINDOW.value()));
        }

        if (to != null) {
            to = to.trim();
            if (!(to.equals(NEW_WINDOW) || to.equals(NEW_TAB))) {
                throw new InvalidAttributeValueException("Invalid value found for attribute 'to' at step action type '%s'. Value will be '%s' or '%s'".formatted(OPEN_WINDOW.value(), NEW_WINDOW, NEW_TAB));
            }
        }

        String varName = object.optString(VAR, null);
        if (varName != null) {
            varName = Utils.validateAndGetVarName(varName);
        }
        return new OpenWindow(url, to, varName);
    }

    @Override
    public void trigger(StepContext stepContext) {
        WebDriver driver = stepContext.getWebDriver();
        if (driver == null) {
            driver = new ChromeDriver();
            stepContext.setWebDriver(driver);
        } else {
            WebDriver.TargetLocator locator = driver.switchTo();
            if (to != null) {
                switch (to) {
                    case NEW_WINDOW -> locator.newWindow(WindowType.WINDOW);
                    case NEW_TAB -> locator.newWindow(WindowType.TAB);
                }
            }
        }
        if (url != null) {
            driver.get(ExpEvaluator.evalExpIfNeeded(stepContext, url, String.class));
        }
        if (handlerVarName != null) {
            stepContext.set(handlerVarName, driver.getWindowHandle());
        }
    }
}

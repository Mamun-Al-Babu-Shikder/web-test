package com.mcubes.webtest.actions.web;

import com.mcubes.webtest.actions.Action;
import com.mcubes.webtest.core.ExpEvaluator;
import com.mcubes.webtest.core.StepContext;
import com.mcubes.webtest.exception.InvalidValueException;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import static com.mcubes.webtest.constants.JsonAttributeKeys.VALUE;
import static com.mcubes.webtest.enums.Patterns.POSITIVE_NUMBER;

public class SwitchWindow implements Action {
    private final String value;

    public SwitchWindow(String value) {
        this.value = value;
    }

    public static SwitchWindow from(JSONObject object) {
        String value = object.getString(VALUE);
        return new SwitchWindow(value);
    }

    @Override
    public void trigger(StepContext stepContext) {
        String value = ExpEvaluator.evalExpIfNeeded(stepContext, this.value, String.class);
        try {
            WebDriver driver = stepContext.getWebDriver();
            if (value.trim().matches(POSITIVE_NUMBER.pattern())) {
                int index = Integer.parseInt(value);
                value = driver.getWindowHandles()
                        .stream()
                        .toList()
                        .get(index);
            }
            driver.switchTo().window(value);
        } catch (Exception ex) {
            throw new InvalidValueException("Failed to resolve the 'window' according to the value '%s'".formatted(value));
        }
    }
}

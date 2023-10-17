package com.mcubes.webtest.actions.web;

import com.mcubes.webtest.actions.Action;
import com.mcubes.webtest.core.ExpEvaluator;
import com.mcubes.webtest.core.StepContext;
import com.mcubes.webtest.util.Utils;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static com.mcubes.webtest.constants.JsonAttributeKeys.URL;
import static com.mcubes.webtest.constants.JsonAttributeKeys.VAR;

public class OpenURL implements Action {
    private final String url;
    private final String handlerVarName;

    public OpenURL(String url, String handlerVarName) {
        this.url = url;
        this.handlerVarName = handlerVarName;
    }

    public static OpenURL from(JSONObject object) {
        String url = object.getString(URL);
        String varName = object.optString(VAR, null);
        if (varName != null) {
            varName = Utils.validateAndGetVarName(varName);
        }
        return new OpenURL(url, varName);
    }

    @Override
    public void trigger(StepContext stepContext) {
        WebDriver driver = stepContext.getWebDriver();
        if (driver == null) {
            driver = new ChromeDriver();
            stepContext.setWebDriver(driver);
        }
        String url = ExpEvaluator.evalExpIfNeeded(stepContext, this.url, String.class);
        driver.get(url);
        if (handlerVarName != null) {
            stepContext.set(handlerVarName, driver.getWindowHandle());
        }
    }
}

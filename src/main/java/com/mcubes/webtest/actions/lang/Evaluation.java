package com.mcubes.webtest.actions.lang;

import com.mcubes.webtest.actions.Action;
import com.mcubes.webtest.core.ExpEvaluator;
import com.mcubes.webtest.core.StepContext;
import com.mcubes.webtest.util.Utils;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import static com.mcubes.webtest.constants.JsonAttributeKeys.EXPRESSION;
import static com.mcubes.webtest.constants.JsonAttributeKeys.VAR;

public class Evaluation implements Action {
    private final String varName;
    private final String exprssion;

    public Evaluation(String varName, String exprssion) {
        this.varName = varName;
        this.exprssion = exprssion;
    }

    public static Evaluation from(JSONObject object) {
        String varName = object.optString(VAR, null);
        if (varName != null) {
            varName = Utils.validateAndGetVarName(varName);
        }
        String expression = object.getString(EXPRESSION);
        return new Evaluation(varName, expression);
    }

    @Override
    public void trigger(WebDriver driver) {
        Object object = ExpEvaluator.evaluate(exprssion);
        if (varName != null) {
            StepContext.getInstance().set(varName, object);
        }
    }
}

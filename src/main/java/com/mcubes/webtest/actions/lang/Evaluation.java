package com.mcubes.webtest.actions.lang;

import com.mcubes.webtest.actions.Action;
import com.mcubes.webtest.core.ExpEvaluator;
import com.mcubes.webtest.core.StepContext;
import com.mcubes.webtest.util.Utils;
import org.json.JSONObject;

import static com.mcubes.webtest.constants.JsonAttributeKeys.EXPRESSION;
import static com.mcubes.webtest.constants.JsonAttributeKeys.VAR;

public class Evaluation implements Action {
    private final String varName;
    private final String expression;

    public Evaluation(String varName, String exprssion) {
        this.varName = varName;
        this.expression = exprssion;
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
    public void trigger(StepContext stepContext) {
        Object object = ExpEvaluator.evaluate(stepContext, expression);
        if (varName != null) {
            stepContext.set(varName, object);
        }
    }
}

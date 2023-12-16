package com.mcubes.webtest.actions.lang;

import com.mcubes.webtest.actions.Action;
import com.mcubes.webtest.core.ExpEvaluator;
import com.mcubes.webtest.core.Step;
import com.mcubes.webtest.core.StepContext;
import org.json.JSONObject;

import java.util.List;

import static com.mcubes.webtest.constants.JsonAttributeKeys.CONDITION;
import static com.mcubes.webtest.constants.JsonAttributeKeys.STEPS;

public class WhileLoop implements Action {
    private final String condition;
    private final List<Step> steps;

    public WhileLoop(JSONObject object) {
        this.condition = object.getString(CONDITION);
        this.steps = Step.build(object.getJSONArray(STEPS));
    }

    @Override
    public void trigger(StepContext stepContext) {
        while (true) {
            Boolean value = ExpEvaluator.evaluate(stepContext, condition, Boolean.class);
            if (value == null)
                throw new NullPointerException("'null' value found for the condition of 'while' loop");
            else if (value == Boolean.FALSE)
                break;
            steps.forEach(s -> s.execute(stepContext));
        }
    }
}

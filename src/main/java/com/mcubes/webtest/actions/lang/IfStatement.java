package com.mcubes.webtest.actions.lang;

import com.mcubes.webtest.actions.Action;
import com.mcubes.webtest.core.ExpEvaluator;
import com.mcubes.webtest.core.Step;
import com.mcubes.webtest.exception.ExpressionEvaluationException;
import com.mcubes.webtest.exception.InvalidConditionException;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import java.util.List;

import static com.mcubes.webtest.constants.JsonAttributeKeys.CONDITION;
import static com.mcubes.webtest.constants.JsonAttributeKeys.STEPS;

public class IfStatement implements Action {
    private final String condition;
    private final List<Step> steps;

    public IfStatement(String condition, List<Step> steps) {
        this.condition = condition;
        this.steps = steps;
    }

    public static IfStatement from(JSONObject object) {
        String condition = object.getString(CONDITION).trim();
        // validate condition here
        List<Step> steps = Step.build(object.getJSONArray(STEPS));
        return new IfStatement(condition, steps);
    }

    @Override
    public void trigger(WebDriver driver) {
        Boolean value;
        try {
            value = ExpEvaluator.evaluate(condition, Boolean.class);
        } catch (ExpressionEvaluationException e)  {
            throw new InvalidConditionException("Invalid condition [%s] found while execute if statement".formatted(condition));
        }

        if (value == Boolean.TRUE) {
            steps.forEach(s -> s.execute(driver));
        }
    }
}

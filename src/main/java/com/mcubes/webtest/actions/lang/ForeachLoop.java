package com.mcubes.webtest.actions.lang;

import com.mcubes.webtest.actions.Action;
import com.mcubes.webtest.core.ExpEvaluator;
import com.mcubes.webtest.core.Step;
import com.mcubes.webtest.core.StepContext;
import com.mcubes.webtest.exception.ExpressionEvaluationException;
import com.mcubes.webtest.exception.InvalidAttributeValueException;
import com.mcubes.webtest.exception.TypeCastingException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.List;

import static com.mcubes.webtest.constants.JsonAttributeKeys.*;
import static com.mcubes.webtest.enums.ActionType.FOREACH_LOOP;
import static com.mcubes.webtest.enums.Patterns.VAR_NAME;

public class ForeachLoop implements Action {
    private final String var;
    private final String items;
    private final List<Step> steps;

    public ForeachLoop(String var, String items, List<Step> steps) {
        this.var = var;
        this.items = items;
        this.steps = steps;
    }

    public static ForeachLoop from(JSONObject object) {
        String var = object.optString(VAR, null);
        if (var != null && !var.matches(VAR_NAME.pattern())) {
            throw new InvalidAttributeValueException("Invalid variable name found [name=%s]".formatted(var));
        }
        //String itemsVarName = Utils.validateAndGetActualVarName(object.getString(ITEMS).trim());
        String itemsVarName = object.getString(ITEMS);
        List<Step> steps = Step.build(object.getJSONArray(STEPS));
        return new ForeachLoop(var == null ? null : var.trim(), itemsVarName, steps);
    }

    @Override
    public void trigger(StepContext stepContext) {
        Collection<?> items;
        try {
            items = ExpEvaluator.evaluate(stepContext, this.items, Collection.class);
        } catch (ExpressionEvaluationException ex) {
            throw new TypeCastingException("Failed to convert value from 'items' to iterable type for step action [type=%s]"
                    .formatted(FOREACH_LOOP.value()));
        }
        for (Object item : items) {
            if (var != null)
                stepContext.set(var, item);
            steps.forEach(s -> s.execute(stepContext));
        }
    }
}

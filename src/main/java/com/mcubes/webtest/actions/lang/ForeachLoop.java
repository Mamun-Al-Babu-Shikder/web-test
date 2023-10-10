package com.mcubes.webtest.actions.lang;

import com.mcubes.webtest.actions.Action;
import com.mcubes.webtest.core.GlobalStorageForStepExecution;
import com.mcubes.webtest.core.Step;
import com.mcubes.webtest.exception.TypeCastingException;
import com.mcubes.webtest.util.Utils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static com.mcubes.webtest.constants.Constants.VAR_PREFIX;
import static com.mcubes.webtest.constants.Constants.VAR_SUFFIX;
import static com.mcubes.webtest.constants.JsonAttributeKeys.*;

public class ForeachLoop implements Action {
    private final String var;
    private final String itemsVarName;
    private final List<Step> steps;

    public ForeachLoop(String var, String itemsVarName, List<Step> steps) {
        this.var = var;
        this.itemsVarName = itemsVarName;
        this.steps = steps;
    }

    public static ForeachLoop from(JSONObject object) {
        String var = object.optString(VAR, null);
        String itemsVarName = Utils.validateAndGetActualVarName(object.getString(ITEMS).trim());
        List<Step> steps = build(object.getJSONArray(STEPS));
        return new ForeachLoop(var, itemsVarName, steps);
    }

    private static List<Step> build(JSONArray array) {
        List<Step> steps = new LinkedList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            steps.add(Step.build(object));
        }
        return steps;
    }

    @Override
    public void trigger(WebDriver driver) {
        try {
             Collection<?> items = GlobalStorageForStepExecution.getCollection(itemsVarName);
            for (Object item : items) {
                if (var != null)
                    GlobalStorageForStepExecution.set(var, item);
                steps.forEach(s->s.execute(driver));
            }
        } catch (TypeCastingException ex) {
            throw new TypeCastingException("Failed to convert variable [items=%s%s%s] to iterable for [type=foreach_loop]".formatted(VAR_PREFIX,itemsVarName, VAR_SUFFIX));
        }
    }
}

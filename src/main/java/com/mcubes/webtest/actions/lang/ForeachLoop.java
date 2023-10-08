package com.mcubes.webtest.actions.lang;

import com.mcubes.webtest.actions.Action;
import com.mcubes.webtest.core.Step;
import com.mcubes.webtest.core.ThreadLocalStorage;
import com.mcubes.webtest.exception.TypeMismatchException;
import com.mcubes.webtest.util.Utils;
import static com.mcubes.webtest.constants.Constants.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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
        String itemsVarName = Utils.validateAndGetActualVarName(object.getString(ITEMS));
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
        Collection<?> items;
        try {
             items = (Collection<?>) ThreadLocalStorage.get(itemsVarName);
        } catch (ClassCastException ex) {
            throw new TypeMismatchException("failed to convert [items=%s%s%s] to iterable for [type=foreach_loop]".formatted(VAR_PREFIX,itemsVarName, VAR_SUFFIX));
        }
        if (items == null) {
            throw new NullPointerException("foreach loop items is null, value iterable object not found for [items=%s%s%s]".formatted(VAR_PREFIX,itemsVarName, VAR_SUFFIX));
        }
        for (Object item : items) {
            if (var != null) {
                ThreadLocalStorage.set(var, item);
            }
            for (Step step : steps) {
                step.execute(driver);
            }
        }
    }
}

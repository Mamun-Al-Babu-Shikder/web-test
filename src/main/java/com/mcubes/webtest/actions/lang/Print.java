package com.mcubes.webtest.actions.lang;

import com.mcubes.webtest.actions.Action;
import com.mcubes.webtest.core.ExpEvaluator;
import com.mcubes.webtest.core.StepContext;
import com.mcubes.webtest.enums.ActionType;
import org.json.JSONObject;

import static com.mcubes.webtest.constants.JsonAttributeKeys.TYPE;
import static com.mcubes.webtest.constants.JsonAttributeKeys.VALUE;
import static com.mcubes.webtest.enums.ActionType.PRINTLN;

public class Print implements Action {
    public String value;
    public boolean newline;

    public Print(String value, boolean newline) {
        this.value = value;
        this.newline = newline;
    }

    public static Print from(JSONObject object) {
        ActionType type = ActionType.from(object.getString(TYPE));
        String value = object.getString(VALUE);
        return new Print(value, type == PRINTLN);
    }

    @Override
    public void trigger(StepContext stepContext) {
        String string = ExpEvaluator.evalExpIfNeeded(stepContext, value, String.class);
        System.out.print(string);
        if (newline) System.out.println();
    }
}

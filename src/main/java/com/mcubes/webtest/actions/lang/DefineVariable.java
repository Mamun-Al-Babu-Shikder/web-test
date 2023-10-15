package com.mcubes.webtest.actions.lang;

import com.mcubes.webtest.actions.Action;
import com.mcubes.webtest.core.ExpEvaluator;
import com.mcubes.webtest.core.StepContext;
import com.mcubes.webtest.exception.InvalidAttributeValueException;
import com.mcubes.webtest.util.Utils;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import static com.mcubes.webtest.constants.JsonAttributeKeys.NAME;
import static com.mcubes.webtest.constants.JsonAttributeKeys.VALUE;
import static com.mcubes.webtest.enums.Patterns.VAR_NAME;

public class DefineVariable implements Action {
    private final String name;
    private final String value;

    public DefineVariable(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static DefineVariable from(JSONObject object) {
        String name = Utils.validateAndGetVarName(object.getString(NAME));
        String input = object.getString(VALUE);
        // validate input here
        return new DefineVariable(name, input);
    }

    @Override
    public void trigger(WebDriver driver) {
        StepContext.getInstance().set(name, ExpEvaluator.evaluate(value));
    }
}

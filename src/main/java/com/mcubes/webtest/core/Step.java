package com.mcubes.webtest.core;

import com.mcubes.webtest.actions.Action;
import com.mcubes.webtest.actions.ActionFactory;
import com.mcubes.webtest.enums.ActionType;
import com.mcubes.webtest.exception.StepBuildException;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import static com.mcubes.webtest.constants.JsonAttributeKeys.TYPE;

public class Step {
    private final Action action;

    private Step(Action action) {
        this.action = action;
    }

    public static Step build(JSONObject object) throws StepBuildException {
        ActionType actionType = ActionType.from(object.getString(TYPE));
        Action action = ActionFactory.getAction(actionType, object);
        return new Step(action);
    }

    public void execute(WebDriver driver) {
        action.trigger(driver);
    }
}

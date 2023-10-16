package com.mcubes.webtest.core;

import com.mcubes.webtest.actions.Action;
import com.mcubes.webtest.actions.ActionFactory;
import com.mcubes.webtest.enums.ActionType;
import com.mcubes.webtest.exception.StepBuildException;
import com.mcubes.webtest.exception.StepExecutionException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import static com.mcubes.webtest.constants.JsonAttributeKeys.TYPE;

public class Step {
    private final ActionType type;
    private final Action action;

    private Step(ActionType type, Action action) {
        this.type = type;
        this.action = action;
    }

    public ActionType getType() {
        return type;
    }

    public static Step build(JSONObject object) {
        try {
            ActionType actionType = ActionType.from(object.getString(TYPE));
            Action action = ActionFactory.getAction(actionType, object);
            return new Step(actionType, action);
        } catch (Exception ex) {
            throw new StepBuildException("Failed to build step", ex);
        }
    }

    public static List<Step> build(JSONArray array) {
        List<Step> steps = new LinkedList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            steps.add(Step.build(object));
        }
        return steps;
    }

    public void execute(StepContext stepContext) {
        if (stepContext == null) {
            throw new NullPointerException("Found 'null' value for Step-Context.");
        }
        try {
            action.trigger(stepContext);
        } catch (Exception ex) {
            throw new StepExecutionException("Failed to execute the step.", ex);
        }
    }
}

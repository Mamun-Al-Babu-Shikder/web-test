package com.mcubes.webtest.actions;

import com.mcubes.webtest.actions.lang.*;
import com.mcubes.webtest.actions.web.*;
import com.mcubes.webtest.enums.ActionType;
import com.mcubes.webtest.enums.NavigationType;
import com.mcubes.webtest.exception.AttributeNotFoundException;
import org.json.JSONException;
import org.json.JSONObject;


import static com.mcubes.webtest.constants.JsonAttributeKeys.*;

public class ActionFactory {
    public static Action getAction(ActionType type, JSONObject object) {
        try {
            return switch (type) {
                case OPEN_URL -> new OpenURL(object.getString(URL));
                case NAVIGATION -> {
                    NavigationType navigationType = NavigationType.from(object.getString(VALUE));
                    yield new Navigator(navigationType, navigationType == NavigationType.TO ? object.getString(URL) : null);
                }
                case FIND_ELEMENT -> FindElement.from(object);
                case CLICK -> Click.from(object);
                case ENTER_TEXT -> EnterText.from(object);
                case DEFINE_VAR -> DefineVariable.from(object);
                case IF_STATEMENT -> IfStatement.from(object);
                case IF_ELSE_STATEMENT -> IfElseStatement.from(object);
                case SWITCH_CASE_STATEMENT -> null;
                case FOREACH_LOOP -> ForeachLoop.from(object);
                case DELAY -> Delay.from(object);
                case PRINT, PRINTLN -> Print.from(object);
                case EVALUATION -> Evaluation.from(object);
            };
        } catch (JSONException ex) {
            throw new AttributeNotFoundException("Expected attribute missing for step action type ['%s'], %s".formatted(type.value(), ex.getMessage()));
        }
    }
}

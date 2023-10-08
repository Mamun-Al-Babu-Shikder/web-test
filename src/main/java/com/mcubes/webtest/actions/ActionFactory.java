package com.mcubes.webtest.actions;

import com.mcubes.webtest.actions.lang.DefineVariable;
import com.mcubes.webtest.actions.lang.Delay;
import com.mcubes.webtest.actions.lang.ForeachLoop;
import com.mcubes.webtest.actions.web.Click;
import com.mcubes.webtest.actions.web.EnterText;
import com.mcubes.webtest.actions.web.Navigator;
import com.mcubes.webtest.actions.web.OpenURL;
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
                case CLICK -> new Click(object.getString(SELECTOR));
                case ENTER_TEXT -> new EnterText(object.getString(SELECTOR), object.getString(TEXT));
                case DEF_VAR -> DefineVariable.from(object);
                case FOREACH_LOOP -> ForeachLoop.from(object);
                case DELAY -> Delay.from(object);
            };
        } catch (JSONException ex) {
            throw new AttributeNotFoundException("expected attribute missing for step [type=%s], %s".formatted(type.name().toLowerCase(), ex.getMessage()));
        }
    }
}

package com.mcubes.webtest.actions.lang;

import com.mcubes.webtest.actions.Action;
import com.mcubes.webtest.core.ThreadLocalStorage;
import com.mcubes.webtest.enums.VariableType;
import com.mcubes.webtest.exception.TypeCastingException;
import com.mcubes.webtest.util.Utils;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import static com.mcubes.webtest.constants.JsonAttributeKeys.NAME;
import static com.mcubes.webtest.constants.JsonAttributeKeys.VALUE;

public class DefineVariable implements Action {
    private final String name;
    private final String type;
    private final String value;

    public DefineVariable(String name, String type, String value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public static DefineVariable from(JSONObject object) {
        String[] typeValuePair = object.getString(VALUE).split(":", 2);
        return new DefineVariable(object.getString(NAME), typeValuePair[0], typeValuePair[1]);
    }

    @Override
    public void trigger(WebDriver driver) {
        String value = Utils.resolveVariables(this.value);
        try {
            Object obj = switch (VariableType.from(type)) {
                case BOOLEAN -> Boolean.valueOf(value.trim());
                case INTEGER -> Long.parseLong(value);
                case FLOAT -> Double.parseDouble(value);
                case STRING -> value;
                case ELEMENT -> Utils.resolveWebElementFrom(driver, value);
                case ELEMENT_LIST -> Utils.resolveWebElementsFrom(driver, value);
            };
            ThreadLocalStorage.set(name, obj);
        } catch (NumberFormatException ex) {
            throw new TypeCastingException("can't cast the variable [%s] to type [%s]".formatted(name, type));
        }
    }


}

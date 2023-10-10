package com.mcubes.webtest.actions.lang;

import com.mcubes.webtest.actions.Action;
import com.mcubes.webtest.core.GlobalStorageForStepExecution;
import com.mcubes.webtest.enums.SelectorType;
import com.mcubes.webtest.enums.VariableType;
import com.mcubes.webtest.exception.InvalidAttributeException;
import com.mcubes.webtest.exception.TypeCastingException;
import com.mcubes.webtest.util.Utils;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import static com.mcubes.webtest.constants.Constants.VAR_NAME_PATTERN;
import static com.mcubes.webtest.constants.JsonAttributeKeys.*;

public class DefineVariable implements Action {
    private final String name;
    private final VariableType type;
    private final String value;
    private final SelectorType selectorType;

    public DefineVariable(String name, VariableType type, String value, SelectorType selectorType) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.selectorType = selectorType;
    }

    public static DefineVariable from(JSONObject object) {
        String name = object.getString(NAME).trim();
        if (!name.matches(VAR_NAME_PATTERN)) {
            throw new InvalidAttributeException("Invalid variable name ['%s'] found at step ['%s']".formatted(name, object.getString(TYPE)));
        }
        VariableType varType = VariableType.from(object.getString(VAR_TYPE).trim());
        String value = object.getString(VALUE);
        SelectorType selectorType = switch (varType) {
            case ELEMENT, ELEMENT_LIST -> SelectorType.from(object.getString(SELECTOR_TYPE));
            default -> null;
        };
        varType.validateValueWithThrow(value);
        return new DefineVariable(name, varType, value, selectorType);
    }

    @Override
    public void trigger(WebDriver driver) {
        //String value = Utils.resolveVariables(this.value);
        //String trimmedValue = value.trim();
        try {
            Object var = switch (type) {
                case BOOLEAN -> Boolean.valueOf(value);
                case INTEGER -> Long.parseLong(value);
                case FLOAT -> Double.parseDouble(value);
                case STRING -> value;
                case STRING_LIST -> value;
                case ELEMENT -> Utils.resolveWebElementFrom(driver, selectorType, value);
                case ELEMENT_LIST -> Utils.resolveWebElementsFrom(driver, selectorType, value);
            };
            GlobalStorageForStepExecution.set(name, var);
        } catch (NumberFormatException ex) {
            throw new TypeCastingException("Unable cast the variable [%s] to type [%s]".formatted(name, type.name()));
        }
    }


}

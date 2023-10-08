package com.mcubes.webtest.actions.lang;

import com.mcubes.webtest.actions.Action;
import com.mcubes.webtest.core.ThreadLocalStorage;
import com.mcubes.webtest.enums.DelayTimeUnit;
import com.mcubes.webtest.exception.IllegalAttributeException;
import com.mcubes.webtest.exception.TypeMismatchException;
import com.mcubes.webtest.exception.UndefineVariableException;
import com.mcubes.webtest.util.Utils;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import static com.mcubes.webtest.constants.JsonAttributeKeys.TIME;
import static com.mcubes.webtest.constants.JsonAttributeKeys.TIME_UNIT;

public class Delay implements Action {
    private final Long time;
    private final String timeVarName;
    public final DelayTimeUnit timeUnit;

    public Delay(Long time, String timeVarName, DelayTimeUnit timeUnit) {
        this.time = time;
        this.timeVarName = timeVarName;
        this.timeUnit = timeUnit;
    }

    public static Delay from(JSONObject object) {
        Long time = null;
        String varName;
        String timeValue = object.getString(TIME).trim();
        try {
            varName = Utils.getActualVarName(timeValue);
            if (varName == null) {
                time = Long.parseLong(timeValue);
            }
            if (time != null && time < 0) {
                throw new RuntimeException();
            }
        } catch (Exception ex) {
            throw new IllegalAttributeException("can't resolve delay time from '%s'".formatted(timeValue));
        }
        String unitName = object.optString(TIME_UNIT, null);
        return new Delay(time, varName, unitName == null ? DelayTimeUnit.MILLISECONDS : DelayTimeUnit.from(unitName));
    }

    @Override
    public void trigger(WebDriver driver) {
        if (time != null) {
            timeUnit.delay(time);
        } else {
            Long value;
            try {
                value = (Long) ThreadLocalStorage.get(timeVarName);
            } catch (Exception ex) {
                throw new TypeMismatchException("failed to convert the value of variable '%s' to delay time".formatted(timeVarName));
            }
            if (value == null) {
                throw new UndefineVariableException(timeVarName);
            }
            if (value < 0) {
                throw new TypeMismatchException("can't resolve negative value '%d' for delay time".formatted(value));
            }
            timeUnit.delay(value);
        }
    }
}

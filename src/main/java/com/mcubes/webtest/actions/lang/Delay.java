package com.mcubes.webtest.actions.lang;

import com.mcubes.webtest.actions.Action;
import com.mcubes.webtest.constants.Constants;
import com.mcubes.webtest.core.ExpEvaluator;
import com.mcubes.webtest.core.StepContext;
import com.mcubes.webtest.enums.DelayTimeUnit;
import com.mcubes.webtest.exception.InvalidAttributeValueException;
import com.mcubes.webtest.exception.InvalidValueException;
import com.mcubes.webtest.util.Utils;
import org.json.JSONObject;

import static com.mcubes.webtest.constants.JsonAttributeKeys.TIME_UNIT;
import static com.mcubes.webtest.constants.JsonAttributeKeys.VALUE;
import static com.mcubes.webtest.enums.Patterns.DELAY_TIME;
import static com.mcubes.webtest.enums.Patterns.EVAL_VAR_NAME;

public class Delay implements Action {
    private final long time;
    private final String timeVarName;
    public final DelayTimeUnit timeUnit;

    public Delay(long time, String timeVarName, DelayTimeUnit timeUnit) {
        this.time = time;
        this.timeVarName = timeVarName;
        this.timeUnit = timeUnit;
    }

    public static Delay from(JSONObject object) {
        long time = 0;
        String varName = null;
        String value = object.getString(VALUE).trim();
        String unitName = object.optString(TIME_UNIT, null);
        if (value.matches(EVAL_VAR_NAME.pattern())) {
            varName = Utils.validateAndGetEvalVarName(value);
        } else if (value.matches(DELAY_TIME.pattern())) {
            time = Long.parseLong(value);
        } else {
            throw new InvalidAttributeValueException("Failed to resolve the value for delay [value=%s]".formatted(value));
        }
        return new Delay(time, varName, unitName == null ? DelayTimeUnit.MILLISECONDS : DelayTimeUnit.from(unitName));
    }

    @Override
    public void trigger(StepContext stepContext) {
        long value = timeVarName == null ? time :
                ExpEvaluator.evaluate(stepContext, Constants.VAR_PREFIX + timeVarName, Long.class);
        if (value < 0) {
            throw new InvalidValueException("Delay time can't be negative value, found [value=%d]".formatted(value));
        }
        timeUnit.delay(value);
    }
}

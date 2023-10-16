package com.mcubes.webtest.actions;

import com.mcubes.webtest.core.StepContext;

public interface Action {
    void trigger(StepContext stepContext);
}

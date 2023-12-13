package com.mcubes.webtest.actions.web;

import com.mcubes.webtest.actions.Action;
import com.mcubes.webtest.core.StepContext;
import com.mcubes.webtest.exception.InvalidTypeException;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import java.util.Arrays;

import static com.mcubes.webtest.constants.JsonAttributeKeys.OPERATION;

public class ManageWindow implements Action {
    private final OperationType operationType;

    public ManageWindow(JSONObject object) {
        this.operationType = OperationType.from(object.getString(OPERATION));
    }

    @Override
    public void trigger(StepContext stepContext) {
        WebDriver.Window window = stepContext.getWebDriver().manage().window();
        switch (operationType) {
            case MAXIMIZE -> window.maximize();
            case MINIMIZE -> window.minimize();
            case FULL_SCREEN -> window.fullscreen();
        }
    }

    public enum OperationType {
        MINIMIZE("MINIMIZE"),
        MAXIMIZE("MAXIMIZE"),
        FULL_SCREEN("FULL_SCREEN")
        ;

        private final String value;

        OperationType(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }

        public static OperationType from(String type) {
            final String trimmedType = type.trim();
            return Arrays.stream(OperationType.values())
                    .filter(p -> p.value().equals(trimmedType))
                    .findFirst()
                    .orElseThrow(() -> new InvalidTypeException("Failed to resolve window manage operation type ['%s']"
                            .formatted(trimmedType)));
        }
    }
}

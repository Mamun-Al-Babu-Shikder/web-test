package com.mcubes.webtest.actions.web;

import com.mcubes.webtest.actions.Action;
import com.mcubes.webtest.core.ExpEvaluator;
import com.mcubes.webtest.core.StepContext;
import com.mcubes.webtest.core.WebElementProvider;
import com.mcubes.webtest.exception.InvalidTypeException;
import com.mcubes.webtest.util.Utils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.*;

import static com.mcubes.webtest.constants.JsonAttributeKeys.*;

public class WebElementOperation implements Action {
    private String varName;
    private final List<Object> params = new ArrayList<>();
    private final OperationType operationType;
    private String elementObject;
    private WebElementProvider webElementProvider;

    public WebElementOperation(JSONObject object) {
        Object elementObject = object.get(ELEMENT);
        if (elementObject instanceof JSONObject obj) {
            this.webElementProvider = WebElementProvider.from(obj);
        } else {
            this.elementObject = elementObject.toString();
        }

        String operationType = object.getString(OPERATION);
        this.operationType = OperationType.from(operationType);

        String varName = object.optString(VAR);
        if (varName != null)
            this.varName = Utils.validateAndGetVarName(varName);

        JSONArray params = object.optJSONArray(PARAMS);
        if (params != null) {
            for (int i=0; i<params.length(); i++) {
                this.params.add(params.get(i));
            }
        }

        if (this.operationType == OperationType.ENTER_TEXT && this.params.isEmpty()) {
            throw new IllegalArgumentException();
        } else if (this.operationType == OperationType.GET_ATTR && this.params.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void trigger(StepContext stepContext) {
        WebElement element = webElementProvider != null ? webElementProvider.getWebElement(stepContext)
                : ExpEvaluator.evaluate(stepContext, elementObject, WebElement.class);


        Object returnValue = switch (operationType) {
            case CLICK -> {
                element.click();
                yield null;
            }
            case ENTER_TEXT -> {
                StringJoiner sj = new StringJoiner(" ");
                params.forEach(p -> sj.add(p.toString()));
                element.sendKeys(sj.toString());
                yield null;
            }
            case IS_DISPLAYED -> element.isDisplayed();
            case IS_SELECTED -> element.isSelected();
            case GET_ATTR -> element.getAttribute(params.get(0).toString());
            default -> null;
        };

        if (varName != null) {
            stepContext.set(varName, returnValue);
        }
    }

    private enum OperationType {
        CLICK("click"),
        ENTER_TEXT("enter_text"),
        IS_DISPLAYED("is_displayed"),
        IS_SELECTED("is_selected"),
        GET_ATTR("get_attr")
        ;

        private final String value;

        OperationType(String value) {
            this.value = value;
        }

        public static OperationType from(String type) {
            final String trimmedType = type.trim();
            return Arrays.stream(OperationType.values())
                    .filter(p -> p.value.equals(trimmedType))
                    .findFirst()
                    .orElseThrow(() -> new InvalidTypeException("Failed to resolve web element operation type ['%s']".formatted(trimmedType)));
        }
    }
}

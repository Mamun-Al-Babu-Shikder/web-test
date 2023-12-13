package com.mcubes.webtest.actions.web;

import com.mcubes.webtest.actions.Action;
import com.mcubes.webtest.core.ExpEvaluator;
import com.mcubes.webtest.core.StepContext;
import com.mcubes.webtest.core.WebElementProvider;
import com.mcubes.webtest.exception.AttributeNotFoundException;
import com.mcubes.webtest.exception.InvalidAttributeValueException;
import com.mcubes.webtest.exception.InvalidTypeException;
import com.mcubes.webtest.exception.WebElementOperationException;
import com.mcubes.webtest.util.Utils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import static com.mcubes.webtest.actions.web.WebElementOperation.OperationType.ENTER_TEXT;
import static com.mcubes.webtest.actions.web.WebElementOperation.OperationType.GET_ATTR;
import static com.mcubes.webtest.constants.JsonAttributeKeys.*;
import static com.mcubes.webtest.enums.ActionType.WEB_ELEMENT_OPT;

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

        String varName = object.optString(VAR, null);
        if (varName != null)
            this.varName = Utils.validateAndGetVarName(varName);

        Object paramsObject = object.opt(PARAMS);

        if (paramsObject == null && (this.operationType == ENTER_TEXT || this.operationType == GET_ATTR)) {
            throw new AttributeNotFoundException("Required attribute 'params' not found for '%s' -> '%s'"
                    .formatted(WEB_ELEMENT_OPT.value(), this.operationType.value()));
        }

        if (paramsObject != null) {
            if (paramsObject instanceof JSONArray paramsArray) {
                for (int i = 0; i < paramsArray.length(); i++) {
                    this.params.add(paramsArray.get(i));
                }
            } else {
                this.params.add(paramsObject);
            }
        }

        if ((this.operationType == ENTER_TEXT || this.operationType == GET_ATTR) && this.params.isEmpty()) {
            throw new InvalidAttributeValueException("The '%s' -> '%s' requires at least 1 'params'"
                    .formatted(WEB_ELEMENT_OPT.value(), this.operationType.value()));
        }
    }

    @Override
    public void trigger(StepContext stepContext) {
        WebElement element = webElementProvider != null ? webElementProvider.getWebElement(stepContext)
                : ExpEvaluator.evaluate(stepContext, elementObject, WebElement.class);

        Object returnValue;
        try {
            returnValue = switch (operationType) {
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
                case GET_TEXT -> element.getText();
                case GET_TAG_NAME -> element.getTagName();
                case DOM_ATTR -> element.getDomAttribute(params.get(0).toString());
                case DOM_PROP -> element.getDomProperty(params.get(0).toString());
                case CLEAR -> {
                    element.clear();
                    yield null;
                }
            };
        } catch (Exception ex) {
            throw new WebElementOperationException("Error occurred while execute '%s' -> '%s'"
                    .formatted(WEB_ELEMENT_OPT.value(), operationType.value()));
        }

        if (varName != null) {
            stepContext.set(varName, returnValue);
        }
    }

    public enum OperationType {
        CLICK("click"),
        ENTER_TEXT("enter_text"),
        IS_DISPLAYED("is_displayed"),
        IS_SELECTED("is_selected"),
        GET_ATTR("get_attr"),
        GET_TEXT("get_text"),
        GET_TAG_NAME("get_tag"),
        DOM_ATTR("dom_attr"),
        DOM_PROP("dom_prop"),
        CLEAR("clear");

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
                    .filter(p -> p.value.equals(trimmedType))
                    .findFirst()
                    .orElseThrow(() -> new InvalidTypeException("Failed to resolve web element operation type ['%s']"
                            .formatted(trimmedType)));
        }
    }
}

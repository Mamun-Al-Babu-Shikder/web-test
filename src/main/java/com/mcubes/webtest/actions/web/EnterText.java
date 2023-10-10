package com.mcubes.webtest.actions.web;

import com.mcubes.webtest.enums.SelectorType;
import com.mcubes.webtest.util.Utils;
import org.json.JSONObject;
import org.openqa.selenium.WebElement;

import static com.mcubes.webtest.constants.JsonAttributeKeys.*;

public class EnterText extends AbstractWebElementAction {
    private final String text;

    public EnterText(SelectorType selectorType, String selector, String text) {
        super(selectorType, selector);
        this.text = text;
    }

    public static EnterText from(JSONObject object) {
        String type = object.getString(SELECTOR_TYPE).trim();
        String selector = object.getString(SELECTOR);
        String text = object.getString(TEXT);
        return new EnterText(SelectorType.from(type), selector, text);
    }

    @Override
    protected void trigger(WebElement element) {
        String text = Utils.resolveVariables(this.text);
        element.sendKeys(text);
    }
}

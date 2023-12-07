package com.mcubes.webtest.core;

import com.mcubes.webtest.enums.SelectorType;
import com.mcubes.webtest.exception.WebElementSelectionException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.mcubes.webtest.constants.JsonAttributeKeys.SELECTOR;
import static com.mcubes.webtest.constants.JsonAttributeKeys.SELECT_BY;

public class WebElementProvider {
    private final SelectorType selectorType;
    private final String selector;

    private WebElementProvider(SelectorType selectorType, String selector) {
        this.selectorType = selectorType;
        this.selector = selector;
    }

    public static WebElementProvider from(JSONObject object) {
        String selectorType = object.getString(SELECT_BY);
        String selector = object.getString(SELECTOR);
        return new WebElementProvider(SelectorType.from(selectorType), selector);
    }

    public WebElement getWebElement(StepContext stepContext) {
        By by = resolveByFromSelector(stepContext);
        return stepContext.getWebDriver().findElement(by);
    }

    public List<WebElement> getWebElements(StepContext stepContext) {
        By by = resolveByFromSelector(stepContext);
        return stepContext.getWebDriver().findElements(by);
    }

    private By resolveByFromSelector(StepContext stepContext) {
        String selector = ExpEvaluator.evalExpIfNeeded(stepContext, this.selector, String.class);
        try {
            return switch (this.selectorType) {
                case TAG -> By.tagName(selector);
                case NAME -> By.name(selector);
                case ID -> By.id(selector);
                case XPATH -> By.xpath(selector);
                case CLASS_NAME -> By.className(selector);
                case CSS_SELECTOR -> By.cssSelector(selector);
                case LINK_TEXT -> By.linkText(selector);
                case PARTIAL_LINK_TEXT -> By.partialLinkText(selector);
            };
        } catch (Exception ex) {
            throw new WebElementSelectionException("Found invalid web element selector [selector=%s]".formatted(selectorType.name().toLowerCase()));
        }
    }
}

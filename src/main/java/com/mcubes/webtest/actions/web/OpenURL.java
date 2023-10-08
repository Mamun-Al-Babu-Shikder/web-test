package com.mcubes.webtest.actions.web;

import com.mcubes.webtest.actions.Action;
import org.openqa.selenium.WebDriver;

public class OpenURL implements Action {
    private final String url;

    public OpenURL(String url) {
        this.url = url;
    }

    @Override
    public void trigger(WebDriver driver) {
        driver.get(url);
    }
}

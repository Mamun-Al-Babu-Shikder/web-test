package com.mcubes.webtest.component;

import com.mcubes.webtest.core.Step;

import java.util.List;

public record Branch(String condition, List<Step> steps) {
    public Branch {
        if (condition != null) {

        }
    }
}

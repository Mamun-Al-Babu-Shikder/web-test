package com.mcubes.webtest;

import com.mcubes.webtest.core.Step;
import com.mcubes.webtest.core.StepContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public final class TestUtils {

    private TestUtils() {}

    public static List<Step> stepsFromJson(final String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray array = jsonObject.getJSONArray("steps");
        List<Step> steps = new LinkedList<>();

        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            steps.add(Step.build(object));
        }

        return steps;
    }
}

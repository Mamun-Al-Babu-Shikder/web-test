package com.mcubes.webtest.actions.lang;

import com.mcubes.webtest.core.Step;
import com.mcubes.webtest.core.StepContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

class IfStatementTest {
    @Test
    void truthConditionTest() throws JSONException {
        final String json = """
                {
                  "steps": [
                    {
                      "type": "if",
                      "condition": "5 < 10 && 5 > -10",
                      "steps": [
                        {
                          "type": "println",
                          "value": "YES"
                        }
                      ]
                    },
                    {
                      "type": "if",
                      "condition": " 5 == 10 ",
                      "steps": [
                        {
                          "type": "println",
                          "value": "YES"
                        }
                      ]
                    }
                  ]
                }
                """;

        JSONObject jsonObject = new JSONObject(json);
        JSONArray array = jsonObject.getJSONArray("steps");
        List<Step> steps = new LinkedList<>();

        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            steps.add(Step.build(object));
        }

        for (Step step : steps) {
            step.execute(StepContext.getInstance());
        }
    }
}
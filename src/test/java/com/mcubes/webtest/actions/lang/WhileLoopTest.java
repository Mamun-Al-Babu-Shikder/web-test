package com.mcubes.webtest.actions.lang;

import com.mcubes.webtest.core.ExpEvaluator;
import com.mcubes.webtest.core.Step;
import com.mcubes.webtest.core.StepContext;
import com.mcubes.webtest.exception.StepExecutionException;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.mcubes.webtest.TestUtils.stepsFromJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Step [type=while] test")
class WhileLoopTest {

    private StepContext context;

    @BeforeEach
    void beforeEachTest() {
        context = StepContext.getInstance();
    }

    @Test
    void trueConditionTest() throws JSONException {
        String json = """
                {
                  "steps": [
                    {
                      "type": "def_var",
                      "name": "i",
                      "value": "1"
                    },
                    {
                      "type": "while",
                      "condition": "#i <= 10",
                      "steps": [
                        {
                          "type": "def_var",
                          "name": "i",
                          "value": "#i+1"
                        }
                      ]
                    }
                  ]
                }
                """;

        List<Step> steps = stepsFromJson(json);
        steps.forEach(step -> step.execute(context));
        assertEquals(11, ExpEvaluator.evaluate(context, "#i", Integer.class));
    }

    @Test
    void falseCondition() throws JSONException {
        String json = """
                {
                  "steps": [
                    {
                      "type": "def_var",
                      "name": "i",
                      "value": "1"
                    },
                    {
                      "type": "while",
                      "condition": "#i > 10",
                      "steps": [
                        {
                          "type": "def_var",
                          "name": "i",
                          "value": "#i+1"
                        }
                      ]
                    }
                  ]
                }
                """;

        List<Step> steps = stepsFromJson(json);
        steps.forEach(step -> step.execute(context));
        assertEquals(1, ExpEvaluator.evaluate(context, "#i", Integer.class));
    }

    @Test
    void nullCondition() throws JSONException {
        String json = """
                {
                  "steps": [
                    {
                      "type": "def_var",
                      "name": "null_var",
                      "value": null
                    },
                    {
                      "type": "while",
                      "condition": "#null_var",
                      "steps": []
                    }
                  ]
                }
                """;

        final Exception[] exception = new Exception[1];
        List<Step> steps = stepsFromJson(json);
        assertThrows(StepExecutionException.class, () -> {
            try {
                steps.forEach(step -> step.execute(context));
            } catch (Exception ex) {
                exception[0] = ex;
                throw ex;
            }
        });

        assertEquals(NullPointerException.class, exception[0].getCause().getClass());
    }
}

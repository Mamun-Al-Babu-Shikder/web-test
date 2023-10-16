package com.mcubes.webtest.actions.lang;

import com.mcubes.webtest.actions.Action;
import com.mcubes.webtest.component.Branch;
import com.mcubes.webtest.core.ExpEvaluator;
import com.mcubes.webtest.core.Step;
import com.mcubes.webtest.core.StepContext;
import com.mcubes.webtest.exception.InvalidAttributeValueException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import static com.mcubes.webtest.constants.JsonAttributeKeys.*;

public class IfElseStatement implements Action {
    private final List<Branch> branches;
    private final Branch elseBranch;

    public IfElseStatement(List<Branch> branches, Branch elseBranch) {
        this.branches = branches;
        this.elseBranch = elseBranch;
    }

    public static IfElseStatement from(JSONObject object) {
        JSONArray ifElseBranches = object.getJSONArray(BRANCH);
        Branch elseBranch = null;
        List<Branch> branches = new LinkedList<>();
        for (int i = 0; i < ifElseBranches.length(); i++) {
            JSONObject caseObject = ifElseBranches.getJSONObject(i);
            String condition = caseObject.optString(CONDITION, null);
            List<Step> steps = Step.build(caseObject.getJSONArray(STEPS));
            Branch branch = new Branch(condition, steps);
            if (condition == null) {
                elseBranch = branch;
                break;
            }
            branches.add(branch);
        }
        if ((branches.size() + (elseBranch != null ? 1 : 0)) != ifElseBranches.length()) {
            throw new InvalidAttributeValueException("Field to resolve if-else branches");
        }
        return new IfElseStatement(branches, elseBranch);
    }

    @Override
    public void trigger(StepContext stepContext) {
        boolean isExecutedAnyBranch = false;
        for (Branch branch : branches) {
            if (ExpEvaluator.evaluate(stepContext, branch.condition(), Boolean.class)) {
                branch.steps().forEach(s -> s.execute(stepContext));
            }
        }
        if (!isExecutedAnyBranch && elseBranch != null) {
            elseBranch.steps().forEach(s -> s.execute(stepContext));
        }
    }
}

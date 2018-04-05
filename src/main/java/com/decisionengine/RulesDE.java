package com.decisionengine;

import com.proto.gen.RuleOuterClass.*;

public class RulesDE extends DecisionEngineType {
    protected Rules rules;

    //Input -> Set of Rules
    //Matches set of rules to statment on process command
    //Output -> Statement
    public RulesDE(Rules rules){
        this.rules = rules;
    }

    public String process(String input){
        String response;

        return "";
    }

}

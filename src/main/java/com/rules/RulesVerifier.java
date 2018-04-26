package com.rules;

import com.proto.gen.RuleOuterClass;

import java.io.InvalidObjectException;

public class RulesVerifier {

    /**
     * For each rule, they should have an expected match pair and an expected fail pair. This will check the
     * rules upon load and make sure they are working as expected.
     * @return
     */
    public boolean checkRules(RuleOuterClass.Rules rules) throws InvalidObjectException {

        for(RuleOuterClass.Rule rule : rules.getRulesList()){
            if(!checkRule(rule)){
                throw new InvalidObjectException("Error with rules. It doesn't seem to match with test");
            };
        }
        return true;
    }


    /**
     * Check are rule and assert true statement is true and false statement is false.
     * @param rule
     * @return
     */
    public boolean checkRule(RuleOuterClass.Rule rule){
        return false;
    }

}

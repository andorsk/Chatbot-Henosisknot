package com.rules;


import com.decisionengine.DecisionEngineType;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;
import edu.stanford.nlp.pipeline.CoreDocument;

import java.util.ArrayList;
import java.util.List;

public class RulesEngine extends DecisionEngineType {

    public MatcherType mMatcher;
    public RulesEngine(){
        init();
    }

    public void init(){
        mMatcher = new Matcher1(com.config.PrimaryConfig.PREDEFINED_STATEMENTS_LOC);
    }

    @Override
    public String process(CoreDocument doc) {
        return null;
    }


}

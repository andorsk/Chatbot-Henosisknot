package com.rules;


import com.decisionengine.DecisionEngineType;
import com.nlp.nlu.ParseEngineType;
import edu.stanford.nlp.pipeline.CoreDocument;

public class RulesEngine extends DecisionEngineType {

    public MatcherType mMatcher;
    public ParseEngineType mParser;

    public RulesEngine(){
        init();
    }
    public RulesEngine(String ruleFile, ParseEngineType parser){
        this.mParser = parser;
        init(ruleFile);
    }

    public void init(){
        init(com.config.PrimaryConfig.PREDEFINED_STATEMENTS_LOC);
    }

    public void init(String rulesFile){
        this.mMatcher = new Matcher1(rulesFile, mParser);
    }

    @Override
    public String process(CoreDocument doc) {
        return null;
    }


}

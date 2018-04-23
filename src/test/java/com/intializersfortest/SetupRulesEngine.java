package com.intializersfortest;


import com.nlp.nlu.IntroductionParser;
import com.nlp.nlu.ParseEngineType;
import com.rules.RulesEngine;
import org.testng.annotations.BeforeClass;

public class SetupRulesEngine {

    protected ParseEngineType mParser;
    protected RulesEngine mRulesEngine;
    @BeforeClass()

    public void SetupRulesEngine(){
         System.out.println("Setting up Rules Engine");
         mParser = new IntroductionParser("tokenize,ssplit,pos,lemma,ner,tokensregex");//tokenize,ssplit,pos,lemma,ner,regexner
         mRulesEngine = new RulesEngine();
    }

}

package com.nlptests;

import com.nlp.nlu.IntroductionParser;
import com.nlp.nlu.ParsingEngine;
import junit.framework.TestCase;

public class SemanticParsingTest extends TestCase {

    public SemanticParsingTest(String name){
        super(name);
    }

    public void test() throws Exception{
        ParsingEngine pe = new ParsingEngine();
        pe.setParsingType(new IntroductionParser());
        pe.runParse("Test text");
    }

}
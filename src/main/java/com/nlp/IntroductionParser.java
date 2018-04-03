package com.nlp;

import edu.stanford.nlp.simple.*;

public class IntroductionParser extends ParseEngineType {

    @Override
    public void parse(String text){
        Sentence sent = new Sentence(text);
    }
}

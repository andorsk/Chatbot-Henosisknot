package com.nlptests;

import com.nlp.nlu.IntroductionParser;
import com.nlp.nlu.ParsingEngine;
import junit.framework.TestCase;
import edu.stanford.nlp.pipeline.*;

public class SemanticParsingTest extends TestCase {

    public SemanticParsingTest(String name){
        super(name);
    }

    public void SentenceNERTest() {
        ParsingEngine pe = new ParsingEngine();
        pe.setParsingType(new IntroductionParser("tokenize,ssplit,pos,lemma,ner"));
        CoreDocument doc = pe.parse("My name is Tom Martell. How are you Henosisknot.com?");
        assertEquals("PERSON", doc.sentences().get(0).nerTags().get(3));
        assertEquals("PERSON", doc.sentences().get(0).nerTags().get(4));
        assertEquals("O", doc.sentences().get(0).nerTags().get(1));
        assertEquals("URL", doc.sentences().get(1).nerTags().get(3));
    }

}
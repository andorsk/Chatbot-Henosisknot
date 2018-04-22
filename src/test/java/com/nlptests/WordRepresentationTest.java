package com.nlptests;

import com.nlp.wordrepresentation.wordofbags.WOB;
import org.junit.Test;

public class WordRepresentationTest {

    public void testWOB(){

        final String testSentence = "I am happy for I am here today";
        //Collection<String> tokens = parse.tokenize(testSentence)
        //WOB wob = new WOB(tokens);

        //assertEquals(wob.get("I"), 2)
        //assertEquals(wob.get("happy"), 1)

        final String testSentence2 = "I am happy that there is life in this world";
        //Test adding union of tokens
        //Collection<String> tokens = parse.tokenize(testSentence2)
        //WOB wob2 = new WOB(tokens);
        //wob.UnionOf(wob2)

        //assertEquals(wob.get("I"), 3)
        //assertEquals(wob.get("happy"), 2)

    }
}

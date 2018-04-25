package com.rulestest;

import edu.stanford.nlp.ling.SentenceUtils;
import edu.stanford.nlp.ling.tokensregex.TokenSequenceMatcher;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;
import edu.stanford.nlp.util.CoreMap;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class TestTokenRegExMatches {
    @Test
    public void testSimpleMatch(){

        final String phrase = "How are you doing$";
        TokenSequencePattern pattern = TokenSequencePattern.compile(phrase);

        //create matcher
        List<? extends CoreMap> corelist = SentenceUtils.toCoreLabelList("How", "are", "you", "doing");
        Assert.assertTrue(pattern.getMatcher(corelist).matches());

        corelist = SentenceUtils.toCoreLabelList("How", "are", "you", "doing", "?");
        Assert.assertFalse(pattern.getMatcher(corelist).matches());

    }

    /**
     * Test if sentence contains phrase
     */
    @Test
    public void testSimpleSentenceContainsFind(){

        final String phrase = "John";
        TokenSequencePattern pattern = TokenSequencePattern.compile(phrase);

        //create matcher
        List<? extends CoreMap> corelist = SentenceUtils.toCoreLabelList("How", "are", "you", "doing", "John", "guy");
        Assert.assertTrue(pattern.getMatcher(corelist).find());

        corelist = SentenceUtils.toCoreLabelList("How", "are", "you", "doing", "?");
        Assert.assertFalse(pattern.getMatcher(corelist).find());

    }

    /**
     * Test if sentence contains phrase
     */
    @Test
    public void testStartsWithandContainsandEndsFind(){

        final String phrase = "/How/*/person/"; //Sequence patten is different than usual regex because it
        //it is matched.

        TokenSequencePattern p = TokenSequencePattern.compile(phrase); //compile into sequence

        String str = "How are you doing John person";
        Assert.assertTrue(str.matches("[(^How)].*person$"));

        //create token list
        List<? extends CoreMap> corelist = SentenceUtils.toCoreLabelList("How", "are", "you", "doing", "John", "person");
        Assert.assertTrue(p.getMatcher(corelist).find()); //match

        corelist = SentenceUtils.toCoreLabelList("How", "are", "you", "doing", "John");
        Assert.assertFalse(p.getMatcher(corelist).find());


    }
}

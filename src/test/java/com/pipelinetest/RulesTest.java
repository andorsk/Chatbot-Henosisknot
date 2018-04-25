package com.pipelinetest;

import com.intializersfortest.SetupRulesEngine;
import edu.stanford.nlp.ling.tokensregex.SequenceMatchResult;
import edu.stanford.nlp.util.CoreMap;
import javafx.util.Pair;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Test class for the rules engine
 */
public class RulesTest extends SetupRulesEngine {

    @Test
    public void testMatcherOnSuppliedRegex() {


        Pair<String, String> p1 = new Pair<>("How are you", "How are you");
        long time = System.currentTimeMillis();
        Assert.assertTrue(isMatchOnSuppliedRegEx(p1.getKey(), p1.getValue()));
        System.out.println("Match required " + (System.currentTimeMillis() - time) + "milliseconds");

        Pair<String, String> p2 = new Pair<>("How are *", "What's going on?");
        Assert.assertFalse(isMatchOnSuppliedRegEx(p2.getKey(), p2.getValue()));

//        Pair<String, String> p3 = new Pair<>("I know what I am. What are you?", "I know what I am. What are you?");
//        Assert.assertTrue(isMatchOnSuppliedRegEx(p3.getKey(), p3.getValue()));

        Pair<String, String> p4 = new Pair<>("This should fail", "");
        Assert.assertFalse(isMatchOnSuppliedRegEx(p4.getKey(), p4.getValue()), "Somehow no supplied regex returned true match");

        Pair<String, String> p5 = new Pair<>("[ner:PERSON]+ [pos:VBZ] /an?/\n" + "/artist|painter/", "Picasso is an artist");
        Assert.assertTrue(isMatchOnSuppliedRegEx(p5.getKey(), p5.getValue()), "Picasso is an artist failed");

        Pair<String, String> p6 = new Pair<>("([ner:NUMBER]+) /km|kilometers?/", "five thousand kilometers");
        Assert.assertTrue(isMatchOnSuppliedRegEx(p6.getKey(), p6.getValue()), "five thousand kilometers failed");

        Pair<String, String> p7 = new Pair<>("([ner:NUMBER]+) /km|kilometers?/", "whatsup");
        Assert.assertFalse(isMatchOnSuppliedRegEx(p7.getKey(), p7.getValue()), "whatsup should have failed on distance test");

    }

    @Test
    public void testFindMatchInRegExList() {
        System.out.println("Testing Regex list");
        List<String> regexList = new ArrayList<String>();

        regexList.add("([ner:NUMBER]+) /km|kilometers?/");
        regexList.add("[ner:PERSON]+ [pos:VBZ] /an?/ /artist|painter/");
        regexList.add("[ner:PERSON]+ [pos:VBZ] /an?/ /artist|painter/");

        Assert.assertEquals(findMatchedRegex(regexList, "Picasso is an artist").size(), 1);
        Assert.assertEquals(findMatchedRegex(regexList, "Who there?").size(), 0);
        Assert.assertEquals(findMatchedRegex(regexList, "Fifty km").size(), 1);
    }

    @Test
    public void testRuleMatcherInFile(){
        Assert.assertEquals(mRulesEngine.mMatcher.match("John has cancer").size(), 1);
        Assert.assertEquals(mRulesEngine.mMatcher.match("Picasso is an artist").size(), 1);
        Assert.assertEquals(mRulesEngine.mMatcher.match("Picasso is poop").size(), 0);
    }

    private List<SequenceMatchResult<CoreMap>> findMatchedRegex(Collection<String> regexlist, String sentence) {
        List<SequenceMatchResult<CoreMap>> matched = mRulesEngine.mMatcher.getMatchListFromStringRegExList(regexlist, this.mParser.parse(sentence).tokens());
        return matched;
    }

    private boolean isMatchOnSuppliedRegEx(String pattern, String sentence) {
        try {
            return mRulesEngine.mMatcher.isMatch(pattern, this.mParser.parse(sentence).tokens());
        } catch (ValueException e) {
            return false;
        }
    }


}

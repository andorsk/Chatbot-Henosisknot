package com.rulestest;

import edu.stanford.nlp.ling.SentenceUtils;
import edu.stanford.nlp.ling.tokensregex.*;
import edu.stanford.nlp.util.CoreMap;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Tests triggers and matches.
 */
public class TestMatchTriggers {

    @Test
    public void testOptionalTrigger(){
        final String phrase = "^How are you? doing?";

        List<TokenSequencePattern> patterns = new ArrayList<TokenSequencePattern>();
        patterns.add(TokenSequencePattern.compile(phrase));

        MultiPatternMatcher.SequencePatternTrigger<CoreMap> trigger =
                new MultiPatternMatcher.BasicSequencePatternTrigger<CoreMap>(
                        new CoreMapNodePatternTrigger(patterns));

        Collection<SequencePattern<CoreMap>> triggered = trigger.apply(SentenceUtils.toCoreLabelList("one", "two", "three"));
        Assert.assertEquals(0, triggered.size());

        triggered = trigger.apply(SentenceUtils.toCoreLabelList("this", "shouldn't", "work"));
        Assert.assertEquals(0, triggered.size());

        triggered = trigger.apply(SentenceUtils.toCoreLabelList("is", "do", "this", "working"));
        Assert.assertEquals(0, triggered.size());

        triggered = trigger.apply(SentenceUtils.toCoreLabelList("How", "are", "doing", "?"));
        Assert.assertEquals(1, triggered.size());

        triggered = trigger.apply(SentenceUtils.toCoreLabelList("How", "are", "you"));
        Assert.assertEquals(1, triggered.size());

    }

    @Test
    public void testOptionalTrigger2(){
        final String phrase = "How are ( you | I ) doing?";

        List<TokenSequencePattern> patterns = new ArrayList<TokenSequencePattern>();
        patterns.add(TokenSequencePattern.compile(phrase));

        MultiPatternMatcher.SequencePatternTrigger<CoreMap> trigger =
                new MultiPatternMatcher.BasicSequencePatternTrigger<CoreMap>(
                        new CoreMapNodePatternTrigger(patterns));

        Collection<SequencePattern<CoreMap>> triggered = trigger.apply(SentenceUtils.toCoreLabelList("one", "two", "three"));
        Assert.assertEquals(0, triggered.size());

        triggered = trigger.apply(SentenceUtils.toCoreLabelList("this", "shouldn't", "work"));
        Assert.assertEquals(0, triggered.size());

        triggered = trigger.apply(SentenceUtils.toCoreLabelList("How", "is", "this", "working"));
        Assert.assertEquals(1, triggered.size());

        triggered = trigger.apply(SentenceUtils.toCoreLabelList("How", "are", "I", "doing", "?"));
        Assert.assertEquals(1, triggered.size());

        triggered = trigger.apply(SentenceUtils.toCoreLabelList("How", "are", "doing", "?"));
        Assert.assertEquals(1, triggered.size());

        triggered = trigger.apply(SentenceUtils.toCoreLabelList("How", "are", "you", "doing", "?"));
        Assert.assertEquals(1, triggered.size());

    }

    @Test
    public void testOptionalTrigger3(){
        final String phrase = "How are ( you | I ) doing";

        List<TokenSequencePattern> patterns = new ArrayList<TokenSequencePattern>();
        patterns.add(TokenSequencePattern.compile(phrase));

        MultiPatternMatcher.SequencePatternTrigger<CoreMap> trigger =
                new MultiPatternMatcher.BasicSequencePatternTrigger<CoreMap>(
                        new CoreMapNodePatternTrigger(patterns));

        Collection<SequencePattern<CoreMap>> triggered = trigger.apply(SentenceUtils.toCoreLabelList("one", "two", "three"));
        Assert.assertEquals(0, triggered.size());

        triggered = trigger.apply(SentenceUtils.toCoreLabelList("this", "shouldn't", "work"));
        Assert.assertEquals(0, triggered.size());

        triggered = trigger.apply(SentenceUtils.toCoreLabelList("How", "is", "this", "working"));
        Assert.assertEquals(0, triggered.size());

        triggered = trigger.apply(SentenceUtils.toCoreLabelList("How", "are", "I", "doing"));
        Assert.assertEquals(1, triggered.size());

        triggered = trigger.apply(SentenceUtils.toCoreLabelList("How", "are", "doing", "?"));
        Assert.assertEquals(1, triggered.size());

        triggered = trigger.apply(SentenceUtils.toCoreLabelList("How", "are", "you", "doing", "?"));
        Assert.assertEquals(1, triggered.size());

    }

    @Test
    public void testContainsTrigger(){
        String p = "([(/one/)]+)";

        List<TokenSequencePattern> patterns = new ArrayList<TokenSequencePattern>();
        patterns.add(TokenSequencePattern.compile(p));

        MultiPatternMatcher.SequencePatternTrigger<CoreMap> trigger =
                new MultiPatternMatcher.BasicSequencePatternTrigger<CoreMap>(
                        new CoreMapNodePatternTrigger(patterns));

        Collection<SequencePattern<CoreMap>> triggered = trigger.apply(SentenceUtils.toCoreLabelList("one", "two", "three"));
        Assert.assertEquals(1, triggered.size());

        triggered = trigger.apply(SentenceUtils.toCoreLabelList("one", "is", "enough"));
        Assert.assertEquals(1, triggered.size());

        triggered = trigger.apply(SentenceUtils.toCoreLabelList("How", "is", "this", "working"));
        Assert.assertEquals(0, triggered.size());

        triggered = trigger.apply(SentenceUtils.toCoreLabelList("there", "are", "two", "doing", "?"));
        Assert.assertEquals(0, triggered.size());

        triggered = trigger.apply(SentenceUtils.toCoreLabelList( "two", "three", "four","one"));
        Assert.assertEquals(1, triggered.size());

        triggered = trigger.apply(SentenceUtils.toCoreLabelList("How", "are", "you", "doing", "?"));
        Assert.assertEquals(0, triggered.size());

    }
    @Test
    public void testContainsTrigger2(){
        String p = "which ( one | two ) is?" ;

        List<TokenSequencePattern> patterns = new ArrayList<TokenSequencePattern>();
        patterns.add(TokenSequencePattern.compile(p));

        MultiPatternMatcher.SequencePatternTrigger<CoreMap> trigger =
                new MultiPatternMatcher.BasicSequencePatternTrigger<CoreMap>(
                        new CoreMapNodePatternTrigger(patterns));

        Collection<SequencePattern<CoreMap>> triggered = trigger.apply(SentenceUtils.toCoreLabelList("one", "two", "three"));
        Assert.assertEquals(0, triggered.size());

        triggered = trigger.apply(SentenceUtils.toCoreLabelList("one", "is", "enough"));
        Assert.assertEquals(0, triggered.size());

        triggered = trigger.apply(SentenceUtils.toCoreLabelList( "way", "this", "works"));
        Assert.assertEquals(0, triggered.size());

        triggered = trigger.apply(SentenceUtils.toCoreLabelList("there", "are", "two", "doing", "?"));
        Assert.assertEquals(0, triggered.size());

        triggered = trigger.apply(SentenceUtils.toCoreLabelList( "which", "two", "is"));
        Assert.assertEquals(1, triggered.size());

        triggered = trigger.apply(SentenceUtils.toCoreLabelList("How", "are", "you", "doing", "?"));
        Assert.assertEquals(0, triggered.size());

    }

    public void testOptionalTrigger6() throws Exception {
        List<TokenSequencePattern> patterns = new ArrayList<TokenSequencePattern>();
        patterns.add(TokenSequencePattern.compile("which word ( should | would ) be matched?"));

        MultiPatternMatcher.SequencePatternTrigger<CoreMap> trigger =
                new MultiPatternMatcher.BasicSequencePatternTrigger<CoreMap>(
                        new CoreMapNodePatternTrigger(patterns));

        Collection<SequencePattern<CoreMap>> triggered = trigger.apply(SentenceUtils.toCoreLabelList("one", "two", "three"));
        Assert.assertEquals(0, triggered.size());

        triggered = trigger.apply(SentenceUtils.toCoreLabelList("which"));
        Assert.assertEquals(1, triggered.size());

        triggered = trigger.apply(SentenceUtils.toCoreLabelList("matched"));
        Assert.assertEquals(0, triggered.size());

        triggered = trigger.apply(SentenceUtils.toCoreLabelList("should"));
        Assert.assertEquals(0, triggered.size());

        triggered = trigger.apply(SentenceUtils.toCoreLabelList("which", "word", "be"));
        Assert.assertEquals(1, triggered.size());

        triggered = trigger.apply(SentenceUtils.toCoreLabelList("which", "word", "should", "be", "matched"));
        Assert.assertEquals(1, triggered.size());

        triggered = trigger.apply(SentenceUtils.toCoreLabelList("which", "word", "would", "be", "matched"));
        Assert.assertEquals(1, triggered.size());
    }

}


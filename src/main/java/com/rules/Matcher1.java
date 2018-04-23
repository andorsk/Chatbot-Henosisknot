package com.rules;

import com.nlp.nlu.ParseEngineType;
import com.nlp.nlu.ParsingEngine;
import com.proto.gen.MessageOuterClass;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.tokensregex.*;
import edu.stanford.nlp.ling.tokensregex.types.Value;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Matcher1 extends MatcherType {
    String mRuleFile;
    ParseEngineType mParser;
    CoreMapExpressionExtractor<MatchedExpression> mExtractor = null;

    public Matcher1(String ruleFile) {
        mRuleFile = ruleFile;
    }


    public Matcher1(String ruleFile, ParseEngineType parser) {
        mRuleFile = ruleFile;
        mParser = parser;
    }

    /**
     * Simple Match for a regex with an annotated text. For tokenregex rules visit https://nlp.stanford.edu/software/tokensregex.html
     * Returns true if there is a match between the tokenregex and the annotated text. Returns false if no match.
     * Not optimized for speed currently as it needs to constantly compile Pattern Expressions. Please use
     * getMatchListFromStringRegExList method for a list of regex and getMatchRegExFromFile for full read.
     *
     * @param tregex
     * @param annotatedtext
     * @return
     */
    public boolean isMatch(String tregex, List<? extends CoreMap> annotatedtext) {
        TokenSequencePattern pattern = TokenSequencePattern.compile(tregex);
        TokenSequenceMatcher matcher = pattern.getMatcher(annotatedtext);
        return matcher.matches();
    }


    /**
     * Provide a collection of tokenregex and returns a set of matched expressions. See https://nlp.stanford.edu/software/tokensregex.html
     * for more info.
     *
     * @param regexlist
     * @param sentence
     * @return
     */
    public List<SequenceMatchResult<CoreMap>> getMatchListFromStringRegExList(Collection<String> regexlist, List<CoreLabel> sentence) {
        List<TokenSequencePattern> sp = convertStringPatternListToTokenSequence(regexlist);
        MultiPatternMatcher multiMatcher = TokenSequencePattern.getMultiPatternMatcher(sp);
        List<SequenceMatchResult<CoreMap>> ss = multiMatcher.findNonOverlapping(sentence);
        return ss;
    }

    /**
     * Returns matched expressions from file. Extractor will be initalized once, but always
     * checked.
     *
     * @param sentence
     * @return
     */
    public List<MatchedExpression> match(CoreMap sentence) {
        initExtractor();
        List<MatchedExpression> matchedExpressions = mExtractor.extractExpressions(sentence);
        return matchedExpressions;
    }

    /**
     * Inits extractor only if it hasn't been initalized already.
     */
    private void initExtractor() {
        if (mExtractor == null) {
            CoreMapExpressionExtractor<MatchedExpression> extractor = CoreMapExpressionExtractor
                    .createExtractorFromFiles(TokenSequencePattern.getNewEnv(), mRuleFile);
            mExtractor = extractor;

        }
    }

    /**
     * Takes a string and reports the first matched expression.
     *
     * @param input
     * @return
     * @throws NullPointerException
     */
    public List<MessageOuterClass.UtterenceMatch> match(String input) throws NullPointerException {
        ArrayList<MessageOuterClass.UtterenceMatch> ret = new ArrayList<MessageOuterClass.UtterenceMatch>();
        if (mParser == null) {
            throw new NullPointerException("No Parser Specified. Please specify parser");
        }
        List<CoreMap> sentences = mParser.parse(input).annotation().get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            List<MatchedExpression> matched = match(sentence);
            if (matched.size() > 0) {
                Iterator<MatchedExpression> iter = matched.iterator();
                while (iter.hasNext()) {
                    MatchedExpression ex = iter.next();
                    String text = ex.getText();
                    Value val = ex.getValue();
                    MessageOuterClass.UtterenceMatch um = MessageOuterClass.UtterenceMatch.newBuilder()
                            .setUtterance(ex.getText())
                            .setResponse(ex.getValue().toString())
                            .build();
                    System.out.println("---Matched " + text + ":" + val.toString());
                    ret.add(um);
                }
            }
            }
        return ret;
    }
}




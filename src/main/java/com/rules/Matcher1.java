package com.rules;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.tokensregex.*;
import edu.stanford.nlp.util.CoreMap;

import java.util.Collection;
import java.util.List;

public class Matcher1 extends MatcherType {
    String mRuleFile;
    CoreMapExpressionExtractor<MatchedExpression> mExtractor;

    public Matcher1(String ruleFile) {
        mRuleFile = ruleFile;
    }


    /**
     * Simple Match for a regex with an annotated text. For tokenregex rules visit https://nlp.stanford.edu/software/tokensregex.html
     * Returns true if there is a match between the tokenregex and the annotated text. Returns false if no match.
     * Not optimized for speed currently as it needs to constantly compile Pattern Expressions. Please use
     * getMatchListFromStringRegExList method for a list of regex and getMatchRegExFromFile for full read.
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
     * @param regexlist
     * @param sentence
     * @return
     */
    public List<SequenceMatchResult<CoreMap>> getMatchListFromStringRegExList(Collection<String> regexlist, List<CoreLabel>  sentence){
        List<TokenSequencePattern> sp = convertStringPatternListToTokenSequence(regexlist);
        MultiPatternMatcher multiMatcher = TokenSequencePattern.getMultiPatternMatcher(sp);
        List<SequenceMatchResult<CoreMap>> ss =  multiMatcher.findNonOverlapping(sentence);
        return ss;
    }

    /**
     * Returns matched expressions from file. Extractor will be initalized once,
     * @param sentence
     * @return
     */
    public List<MatchedExpression> getMatchRegExFromFile(CoreMap sentence) {

        initExtractor();
        List<MatchedExpression> matchedExpressions = mExtractor.extractExpressions(sentence);

        for (MatchedExpression matched : matchedExpressions) {
            System.out.println("Matched " + matched.getText() + ":" + matched.getValue());
        }

        return matchedExpressions;
    }

    /**
     * Inits extractor only if it hasn't been initalized already.
     */
    private void initExtractor(){
        if(mExtractor == null){
            CoreMapExpressionExtractor<MatchedExpression> extractor = CoreMapExpressionExtractor
                    .createExtractorFromFiles(TokenSequencePattern.getNewEnv(), mRuleFile);

        }
    }

    public String match(String input) {
        return "";
    }

    public String findMatchedResponse(String rule) {
        return "";
    }

    //generates a score based on the union of two objects.
    //J(x,y) = |x intersect y | / |x union y| * 100
    //OE is an array of strings
    void JaccardSimilarityCoefficientForOE(String[] list1, String[] String2) {

    }

    //Given two regex, find the similarity between pattern.
    //Note: Is this the right way of doing it? Pattern and intent are different.
    void testPatternSimilarityScore(String regex1, String regex2) {

    }
}

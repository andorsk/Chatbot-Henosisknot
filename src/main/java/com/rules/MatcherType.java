package com.rules;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.tokensregex.SequenceMatchResult;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class MatcherType implements MatcherInterface {

    public abstract boolean isMatch(String tregex, List<? extends CoreMap> annotatedtext);

    public abstract List<SequenceMatchResult<CoreMap>> getMatchListFromStringRegExList(Collection<String> regexlist, List<CoreLabel>  sentence);

    public List<TokenSequencePattern> convertStringPatternListToTokenSequence(Collection<String> patternlist){
        List<TokenSequencePattern> sequencePatterns = new ArrayList<TokenSequencePattern>();
        for(String pattern: patternlist){
            TokenSequencePattern tspattern = TokenSequencePattern.compile(pattern);
            sequencePatterns.add(tspattern);
        }
        return sequencePatterns;
    }
}

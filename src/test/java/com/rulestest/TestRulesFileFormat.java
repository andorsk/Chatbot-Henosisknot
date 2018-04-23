package com.rulestest;

import edu.stanford.nlp.ling.tokensregex.CoreMapExpressionExtractor;
import edu.stanford.nlp.ling.tokensregex.MatchedExpression;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;
import org.testng.annotations.Test;

public class TestRulesFileFormat {

    @Test
    public void TestValidFormat(){

        String mRuleFile = "/users/andor/workspace/Chatbot-Henosisknot/src/test/resources/rules/base.txt";
        CoreMapExpressionExtractor<MatchedExpression> extractor = CoreMapExpressionExtractor
                .createExtractorFromFiles(TokenSequencePattern.getNewEnv(), mRuleFile);

    }
}

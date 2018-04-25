package com.rulestest;

import edu.stanford.nlp.ling.tokensregex.CoreMapExpressionExtractor;
import edu.stanford.nlp.ling.tokensregex.MatchedExpression;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;
import org.testng.annotations.Test;

import java.io.File;

public class TestRulesFileFormat {

    @Test
    public void TestValidFormat(){
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("rules/base.txt").getFile());

        CoreMapExpressionExtractor<MatchedExpression> extractor = CoreMapExpressionExtractor
                .createExtractorFromFiles(TokenSequencePattern.getNewEnv(), file.getAbsolutePath());

    }
}

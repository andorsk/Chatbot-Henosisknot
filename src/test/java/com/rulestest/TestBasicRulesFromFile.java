package com.rulestest;


import com.rules.RulesHelpers;
import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.tokensregex.CoreMapExpressionExtractor;
import edu.stanford.nlp.ling.tokensregex.MatchedExpression;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * This uses a tokenized annotator to test match, which is different from the custom annotation class.
 */
public class TestBasicRulesFromFile {
    StanfordCoreNLP mPipeline;
    CoreMapExpressionExtractor<MatchedExpression> mExtractor;

    @BeforeClass
    public void setUp() throws IOException {
        //Setup Stanford's NLP Library with
        // set up pipeline properties
        Properties props = new Properties();
        // set the list of annotators to run
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma");
        // set a property for an annotator, in this case the coref annotator is being set to use the neural algorithm

        mPipeline = new StanfordCoreNLP(props);

        System.out.println("Set up test basic from file");

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("rules/test/test.txt").getFile());
        String filePath = file.getAbsolutePath();

        if (!new File(filePath).exists()) {
            throw new IOException("Invalid path supplied in setup of test: " + filePath);
        }
        mExtractor = RulesHelpers.createExtractorFromFile(filePath);
    }

    @Test
    public void TestRuleFile1() {
        testString("I was born on", true);
        testString("Shouldn't match", false);
        testString("This should match", true);
        testString("No", false);
    }

    private void testString(String text, boolean match) {
        int _b = match ? 1: 0;
        CoreDocument document = new CoreDocument(text);
        mPipeline.annotate(document);
        Assert.assertEquals(mExtractor.extractExpressions(document.annotation().get(CoreAnnotations.SentencesAnnotation.class).get(0)).size(), _b);
        List<MatchedExpression> s = mExtractor.extractExpressions(document.annotation().get(CoreAnnotations.SentencesAnnotation.class).get(0));
    }

}

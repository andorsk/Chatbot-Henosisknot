package com.rulestest;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.proto.gen.RuleOuterClass;
import com.rules.RulesHelpers;
import com.rules.RulesVerifier;
import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.tokensregex.CoreMapExpressionExtractor;
import edu.stanford.nlp.ling.tokensregex.MatchedExpression;
import edu.stanford.nlp.ling.tokensregex.SequenceMatchRules;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;
import edu.stanford.nlp.ling.tokensregex.parser.ParseException;
import edu.stanford.nlp.ling.tokensregex.parser.TokenSequenceParseException;
import edu.stanford.nlp.ling.tokensregex.types.Value;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class TestNewRulesLoader {
    StanfordCoreNLP mPipeline;
    CoreMapExpressionExtractor<MatchedExpression> mExtractor;

    RuleOuterClass.Rule r1 = RuleOuterClass.Rule.newBuilder()
            .setGuid(UUID.randomUUID().toString())
            .setPattern(" ( /This/ /should/ /match/ ) ")
                .setPositiveMatch("This should match")
                .setNegativeMatch("No match")
                .setResult("This is the result")
                .setRuleType("tokens") //set stage as well.
                .setStage(0)
                .build();

    RuleOuterClass.Rule r2 = RuleOuterClass.Rule.newBuilder()
            .setGuid(UUID.randomUUID().toString())
            .setPattern(" ( /What/ /is/ /my/ /name/ ) ")
            .setPositiveMatch("What is my name")
            .setNegativeMatch("This shouldn't match")
            .setResult("This is the result")
            .setRuleType("tokens") //set stage as well.
            .setStage(0)
            .build();

    @BeforeTest
    public void setup() {
        //Setup Stanford's NLP Library with
        // set up pipeline properties
        Properties props = new Properties();
        // set the list of annotators to run
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma");
        // set a property for an annotator, in this case the coref annotator is being set to use the neural algorithm

        mPipeline = new StanfordCoreNLP(props);
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ObjectOutputStream os = new ObjectOutputStream(out);
            os.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }


    @Test
    public void TestSingleRule() {

        RuleOuterClass.Rules rules = RuleOuterClass.Rules.newBuilder()
                .addRules(r1)
                .build();

        // 2. Create custom extractor that bypasses the need to read from File
        mExtractor = new CoreMapExpressionExtractor<MatchedExpression>(TokenSequencePattern.getNewEnv());
        try {
            RulesHelpers.updateExtractorRules(mExtractor, rules);
            RulesVerifier rv = new RulesVerifier(mExtractor, mPipeline, rules);
            Assert.assertTrue(rv.verifyRules());
        } catch (TokenSequenceParseException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        CoreDocument document = new CoreDocument("This should match");
        mPipeline.annotate(document);

        // 4. Go through each sentence and match. Print out if there's a match.
        List<CoreMap> sentences = document.annotation().get(CoreAnnotations.SentencesAnnotation.class);

        testString("This should match", true);
        testString("No. Not match", false);
        testString("This should match. This should not", true);


    }


    @Test
    public void TestMultiRule() {

        RuleOuterClass.Rules rules = RuleOuterClass.Rules.newBuilder()
                .addRules(r1)
                .addRules(r2)
                .build();

        // 2. Create custom extractor that bypasses the need to read from File
        mExtractor = new CoreMapExpressionExtractor<MatchedExpression>(TokenSequencePattern.getNewEnv());
        try {
            RulesHelpers.updateExtractorRules(mExtractor, rules);
            RulesVerifier rv = new RulesVerifier(mExtractor, mPipeline, rules);
            Assert.assertTrue(rv.verifyRules());
        } catch (TokenSequenceParseException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        CoreDocument document = new CoreDocument("This should match");
        mPipeline.annotate(document);

        // 4. Go through each sentence and match. Print out if there's a match.
        List<CoreMap> sentences = document.annotation().get(CoreAnnotations.SentencesAnnotation.class);

        testString("This should match", true);
        testString("No. Not match", false);
        testString("This should match. This should not", true);
        testString("What is my name", true);
        testString("What is my name. This should match", true);
    }

    @Test
    public void TestMultiRuleWithOverlap() {

        r1 = RuleOuterClass.Rule.newBuilder()
                .setGuid(UUID.randomUUID().toString())
                .setPattern(" ( /This/ ) ")
                .setPositiveMatch("This")
                .setNegativeMatch("No match")
                .setResult("This is result for 1")
                .setRuleType("tokens") //set stage as well.
                .setStage(0)
                .build();

        r2 = RuleOuterClass.Rule.newBuilder()
                .setGuid(UUID.randomUUID().toString())
                .setPattern(" ( /This/ /should/ /match/ ) ")
                .setPositiveMatch("This should match")
                .setNegativeMatch("This shouldn't match")
                .setResult("This is the result for 2")
                .setRuleType("tokens") //set stage as well.
                .setStage(0)
                .build();

        r2 = RuleOuterClass.Rule.newBuilder()
                .setGuid(UUID.randomUUID().toString())
                .setPattern(" ( /This/ /should/ ) ")
                .setPositiveMatch("This should")
                .setNegativeMatch("Who should match")
                .setResult("This is the result for 3")
                .setRuleType("tokens") //set stage as well.
                .setStage(0)
                .build();

        RuleOuterClass.Rules rules = RuleOuterClass.Rules.newBuilder()
                .addRules(r1)
                .addRules(r2)
                .build();

        // 2. Create custom extractor that bypasses the need to read from File
        mExtractor = new CoreMapExpressionExtractor<MatchedExpression>(TokenSequencePattern.getNewEnv());
        try {
            RulesHelpers.updateExtractorRules(mExtractor, rules);
            RulesVerifier rv = new RulesVerifier(mExtractor, mPipeline, rules);
            Assert.assertTrue(rv.verifyRules());
        } catch (TokenSequenceParseException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        CoreDocument document = new CoreDocument("This should match");
        mPipeline.annotate(document);

        // 4. Go through each sentence and match. Print out if there's a match.
        List<CoreMap> sentences = document.annotation().get(CoreAnnotations.SentencesAnnotation.class);

        getResultsString("This should match", "This is the result for 2");
        getResultsString("No. Not match", null);
        getResultsString("This should match. This should not", "This is the result for 2");
        getResultsString("What is my name", null);
        getResultsString("What is my name. This should match", null);


    }

    /**
     * Early performance test to see if there is any expected lag with loads of rules.
     * Answer: There is and results start becoming unacceptable after 10000 rules. By 1000000
     * it's basically unusable.
     *
     * Testing benchmark on rule list size 1
     * 41ms
     * Testing benchmark on rule list size 10
     * 2ms
     * Testing benchmark on rule list size 100
     * 19ms
     * Testing benchmark on rule list size 1000
     * 189ms
     * Testing benchmark on rule list size 10000
     * 243ms
     * Testing benchmark on rule list size 100000
     * 6140ms
     * Testing benchmark on rule list size 1000000
     */
    @Test
    @BenchmarkOptions(concurrency = 2, warmupRounds = 0, benchmarkRounds = 5)
    public void TestMatchBenchmarkAcrossHighRuleSet(){

        RuleOuterClass.Rules.Builder rulesBuilder = RuleOuterClass.Rules.newBuilder();

        //Write it to file.
        final int[] NUM_OF_RULES_LIST = new int[]{1, 10, 100, 1000, 10000, 100000, 1000000, 100000000};

        for(int i = 0; i < NUM_OF_RULES_LIST.length; i++){

            System.out.println("Testing benchmark on rule list size " +  NUM_OF_RULES_LIST[i]);
            for(int j = 0; j < NUM_OF_RULES_LIST[i]; j++){
                rulesBuilder.addRules(RuleOuterClass.Rule.newBuilder()
                        .setGuid(UUID.randomUUID().toString())
                        .setPattern(" ( /Rule/ / " +  j + " / ) ")
                        .setPositiveMatch("Rule " + j)
                        .setNegativeMatch("Rule " + (j + 1))
                        .setResult("This is the result")
                        .setRuleType("tokens") //set stage as well.
                        .setStage(0)
                        .build());
            }

            try {
                CoreDocument document = new CoreDocument("Rule " + NUM_OF_RULES_LIST[i]);
                mPipeline.annotate(document);
                CoreMapExpressionExtractor<MatchedExpression> extractor = new CoreMapExpressionExtractor<MatchedExpression> (TokenSequencePattern.getNewEnv()); //this might run in parallel
                RulesHelpers.updateExtractorRules(extractor, rulesBuilder.build());

                long time = System.currentTimeMillis();
                extractor.extractExpressions(document.annotation().get(CoreAnnotations.SentencesAnnotation.class).get(0)).size();
                System.out.println(System.currentTimeMillis()- time);

            } catch (TokenSequenceParseException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


    void getResultsString(String text, String expected) {
        CoreDocument document = new CoreDocument(text);
        mPipeline.annotate(document);
        List<MatchedExpression> me = mExtractor.extractExpressions(document.annotation().get(CoreAnnotations.SentencesAnnotation.class).get(0));
        for(MatchedExpression m : me){
            String res = m.getValue().toString();
            System.out.println("res is " + res + " doc " + document.annotation().get(CoreAnnotations.SentencesAnnotation.class).get(0));
            Assert.assertEquals(res, expected);
        }
    }

    void testString(String text, boolean match) {
        int _b = match ? 1: 0;
        CoreDocument document = new CoreDocument(text);
        mPipeline.annotate(document);
        Assert.assertEquals(mExtractor.extractExpressions(document.annotation().get(CoreAnnotations.SentencesAnnotation.class).get(0)).size(), _b);
    }
}

package com.rulestest;

import com.proto.gen.RuleOuterClass;
import com.rules.RulesHelpers;
import com.rules.RulesVerifier;
import com.rulesparser.RuleParser;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.tokensregex.CoreMapExpressionExtractor;
import edu.stanford.nlp.ling.tokensregex.MatchedExpression;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;
import edu.stanford.nlp.ling.tokensregex.parser.ParseException;
import edu.stanford.nlp.ling.tokensregex.parser.TokenSequenceParseException;
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
    final String basePath = "/hi";

    RuleOuterClass.Rule ruleDef = RuleOuterClass.Rule.newBuilder()
            .setRuleType("CLASS")
            .setValue("com.annotation.ExtendedCoreAnnotation$EmbeddedRule")
            .setIsAssignment(true)
            .setGuid(UUID.randomUUID().toString())
            .setName("embedded_rule")
            .build();

    RuleOuterClass.Rule r1 = RuleOuterClass.Rule.newBuilder()
            .setGuid(UUID.randomUUID().toString())
            .setPattern(" ( /This/ /should/ /match/ ) ")
            .setPositiveMatch("This should match")
            .setNegativeMatch("No match")
            .setResult("This is the result")
            .setRuleType("tokens") //set stage as well.
            .setIsExtraction(true)
            .setStage(0)
            .build();

    RuleOuterClass.Rule r2 = RuleOuterClass.Rule.newBuilder()
            .setGuid(UUID.randomUUID().toString())
            .setPattern(" ( /What/ /is/ /my/ /name/ ) ")
            .setPositiveMatch("What is my name")
            .setNegativeMatch("This shouldn't match")
            .setResult("This is the result")
            .setIsExtraction(true)
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
                .addRules(ruleDef)
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
                .addRules(ruleDef)
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

        RuleOuterClass.Rule r1 = RuleOuterClass.Rule.newBuilder()
                .setGuid(UUID.randomUUID().toString())
                .setPattern(" ( /This/ ) ")
                .setPositiveMatch("This")
                .setNegativeMatch("No match")
                .setResult("This is result for 1")
                .setRuleType("tokens") //set stage as well.
                .setIsExtraction(true)
                .setStage(0)
                .build();

        RuleOuterClass.Rule r2 = RuleOuterClass.Rule.newBuilder()
                .setGuid(UUID.randomUUID().toString())
                .setPattern(" ( /This/ /should/ /match/ ) ")
                .setPositiveMatch("This should match")
                .setNegativeMatch("What is a match")
                .setResult("This is the result for 2")
                .setIsExtraction(true)
                .setRuleType("tokens") //set stage as well.
                .setStage(0)
                .build();

        RuleOuterClass.Rule r3 = RuleOuterClass.Rule.newBuilder()
                .setGuid(UUID.randomUUID().toString())
                .setPattern(" ( /This/ /should/ ) ")
                .setPositiveMatch("This should")
                .setNegativeMatch("Who should match")
                .setResult("This is the result for 3")
                .setIsExtraction(true)
                .setRuleType("tokens") //set stage as well.
                .setStage(0)
                .build();


        RuleOuterClass.Rule r4 = RuleOuterClass.Rule.newBuilder()
                .setGuid(UUID.randomUUID().toString())
                .setPattern(" ( /This/ /should/ /match/ ) ")
                .setPositiveMatch("This should match")
                .setNegativeMatch("What is a match")
                .setResult("This is the result for 4")
                .setRuleType("tokens") //set stage as well.
                .setIsExtraction(true)
                .setStage(0)
                .build();

        RuleOuterClass.Rules rules = RuleOuterClass.Rules.newBuilder()
                .addRules(ruleDef)
                .addRules(r1)
                .addRules(r2)
                .addRules(r3)
                .addRules(r4)
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
        getResultsString("This should", "This is the result for 3");

    }

    @Test
    public void TestNERTagMatch() {

        RuleOuterClass.Rule r1 = RuleOuterClass.Rule.newBuilder()
                .setGuid(UUID.randomUUID().toString())
                .setPattern("([ner: PERSON]+) /was|is/ /an?/ []{0,3} /painter|artist/ ")
                .setPositiveMatch("John was an artist")
                .setNegativeMatch("This was not an artist")
                .setResult("John was in fact, an artist")
                .setIsExtraction(true)
                .setRuleType("tokens") //set stage as well.
                .setStage(0)
                .build();


        RuleOuterClass.Rules rules = RuleOuterClass.Rules.newBuilder()
                .addRules(ruleDef)
                .addRules(r1)
                .build();

        testString("John was an artist", true);
        testString("John could not paint for his life", false);
    }


    /**
     * Test File Loader from direct file
     */
    @Test
    public void TestRuleFileLoader() {
        RuleParser rp = new RuleParser(basePath);
        RuleOuterClass.Rules rls = rp.parseFiles();

        CoreMapExpressionExtractor<MatchedExpression> extractor = new CoreMapExpressionExtractor<MatchedExpression>(TokenSequencePattern.getNewEnv());
        try {
            RulesHelpers.updateExtractorRules(extractor, rls);
        } catch (TokenSequenceParseException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        testString("This should match", true);
        testString("No. Not match", false);
        testString("This should match. This should not", true);
    }

    /**
     * Test Rules Directory from loader.
     */
    public void TestRuleDirectoryLoader() {
        RuleParser rp = new RuleParser(basePath);
        RuleOuterClass.Rules rls = rp.parseFiles();

        CoreMapExpressionExtractor<MatchedExpression> extractor = new CoreMapExpressionExtractor<MatchedExpression>(TokenSequencePattern.getNewEnv());
        try {
            RulesHelpers.updateExtractorRules(extractor, rls);
        } catch (TokenSequenceParseException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        testString("This should match", true);
        testString("No. Not match", false);
        testString("This should match. This should not", true);
    }

    //get the results string and check against expected.
    void getResultsString(String text, String expected) {
        CoreDocument document = new CoreDocument(text);
        mPipeline.annotate(document);
        List<MatchedExpression> me = mExtractor.extractExpressions(document.annotation().get(CoreAnnotations.SentencesAnnotation.class).get(0));
        for (MatchedExpression m : me) {
            String res = m.getValue().get().toString();
            Assert.assertEquals(res, expected);
        }
        System.out.println("label is " + document.annotation().get(CoreAnnotations.TextAnnotation.class).toString());
        System.out.println("me is " + me.toString());
        System.out.println("Hiii");
    }

    //Test if string actually matches.
    void testString(String text, boolean match) {
        int _b = match ? 1 : 0;
        CoreDocument document = new CoreDocument(text);
        mPipeline.annotate(document);
        Assert.assertEquals(mExtractor.extractExpressions(document.annotation().get(CoreAnnotations.SentencesAnnotation.class).get(0)).size(), _b);
    }
}

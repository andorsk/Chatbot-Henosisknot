package com.rulestest;

import com.proto.gen.RuleOuterClass;
import com.rules.RulesHelpers;
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

    @Deprecated
    public void TestSingleRule() {

        // 1. Create Rule
        RuleOuterClass.Rule r1 = null;

        r1 = RuleOuterClass.Rule.newBuilder()
                .setGuid(UUID.randomUUID().toString())
                .setPattern("This is my pattern")
                .setPositiveMatch("This is my pattern.")
                .setPositiveMatch("This is my pattern.")
                .setNegativeMatch("This doesn't work")
                .setRuleType("tokens") //set stage as well.
                .setStage(0)
                .build();


        RuleOuterClass.Rules rules = RuleOuterClass.Rules.newBuilder()
                .addRules(r1)
                .build();

        // 2. Create custom extractor that bypasses the need to read from File
        CoreMapExpressionExtractor<MatchedExpression> extractor = new CoreMapExpressionExtractor<MatchedExpression>(TokenSequencePattern.getNewEnv());
        List<SequenceMatchRules.Rule> rls = RulesHelpers.convertRulesToListofSequenceRules(rules, extractor);
        extractor.appendRules(rls);

        CoreDocument document = new CoreDocument("This is my pattern. This should not work.");
        mPipeline.annotate(document);

        // 4. Go through each sentence and match. Print out if there's a match.
        List<CoreMap> sentences = document.annotation().get(CoreAnnotations.SentencesAnnotation.class);

        List<MatchedExpression> matched = extractor.extractExpressions(document.annotation().get(CoreAnnotations.SentencesAnnotation.class).get(0));
        Assert.assertEquals(matched.size(), 1,"S:" + sentences.get(0).toShorterString()); //This is my pattern

        matched = extractor.extractExpressions(sentences.get(1));
        Assert.assertEquals(matched.size(),0, "S:" + sentences.get(1).toShorterString());
    }

    @Test
    public void TestSingleRuleV2() {

        // 1. Create Rule
        RuleOuterClass.Rule r1 = null;

        r1 = RuleOuterClass.Rule.newBuilder()
                .setGuid(UUID.randomUUID().toString())
                .setPattern(" ( /This/ /should/ /match/ ) ")
                .setPositiveMatch("This should match")
                .setNegativeMatch("No match")
                .setResult("This is the result")
                .setRuleType("tokens") //set stage as well.
                .setStage(0)
                .build();


        RuleOuterClass.Rules rules = RuleOuterClass.Rules.newBuilder()
                .addRules(r1)
                .build();

        // 2. Create custom extractor that bypasses the need to read from File
        mExtractor = new CoreMapExpressionExtractor<MatchedExpression>(TokenSequencePattern.getNewEnv());
        try {
            RulesHelpers.RuleToSequenceRulesParser(mExtractor, rules);
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

    private void testString(String text, boolean match) {
        int _b = match ? 1: 0;
        CoreDocument document = new CoreDocument(text);
        mPipeline.annotate(document);
        Assert.assertEquals(mExtractor.extractExpressions(document.annotation().get(CoreAnnotations.SentencesAnnotation.class).get(0)).size(), _b);
    }
}

package com.rulestest;

import com.rules.RulesHelpers;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.SentenceUtils;
import edu.stanford.nlp.ling.tokensregex.CoreMapExpressionExtractor;
import edu.stanford.nlp.ling.tokensregex.MatchedExpression;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.util.ArrayCoreMap;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Unlike the TestBasicRulesfromFile class, this class writes the annotations manually. If this breaks
 * then there is an issue with the rules file or a bug somewhere.
 *
 */
public class TestRulesFromFilesWithCustomAnnotation {

    CoreMapExpressionExtractor<MatchedExpression> mExtractor;

    @BeforeClass
    public void setUp() throws IOException{

        System.out.println("Setting up TestRulesFromFilesWithCustomAnnotation");

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("rules/test/test.txt").getFile());
        String filePath = file.getAbsolutePath();

        if(! new File(filePath).exists()){
            throw new IOException("Invalid path supplied in setup of test: " + filePath);
        }
        mExtractor = RulesHelpers.createExtractorFromFile(filePath);
    }


    /**
     * Manually create coremap and test match.
     */
    @Test
    public void TestMatchFromFile1(){
        Annotation ann = makeAnnotation("Which word should be matched");
        for(CoreMap s : ann.get(CoreAnnotations.SentencesAnnotation.class)){
            Assert.assertEquals(mExtractor.extractExpressions(s).size(), 1);
        }
       ///// Assert.assertEquals(mExtractor.extractExpressions(makeAnnotation("Show me the way")).size(), 0);
    }

    @Test
    public void TestRulesVerification(){


    }

    private static List<CoreLabel> makeSentence(String sentence) {
        String[] words = sentence.split(" ");
        return SentenceUtils.toCoreLabelList(words);

    }
    private static CoreMap makeSentenceCoreMap(String sentence) {
        List<CoreLabel> tokens = makeSentence(sentence);
        CoreMap map = new ArrayCoreMap(1);
        map.set(CoreAnnotations.TokensAnnotation.class, tokens);
        return map;
    }

    private static Annotation makeAnnotation(String... testText) {

        List<CoreMap> sentences = new ArrayList<>();
        for (String text : testText) {
            List<CoreLabel> labels = makeSentence(text);
            CoreMap sentence = new ArrayCoreMap();
            sentence.set(CoreAnnotations.TokensAnnotation.class, labels);
            sentences.add(sentence);
        }
        Annotation annotation = new Annotation(StringUtils.join(testText));
        annotation.set(CoreAnnotations.SentencesAnnotation.class, sentences);

        return annotation;
    }

    /**
         * Helper method: check that the CoreLabels in the give sentence
         * have the expected tags
         */
    private static void checkLabels(List<CoreLabel> sentence,
                                    String... tags) {
        Assert.assertEquals(tags.length, sentence.size());
        for (int i = 0; i < tags.length; ++i) {
            Assert.assertEquals(tags[i], sentence.get(i).get(CoreAnnotations.PartOfSpeechAnnotation.class));
        }
    }

    private static void checkLabels(CoreMap sentence, String... tags){
        checkLabels(sentence.get(CoreAnnotations.TokensAnnotation.class), tags);
    }
}

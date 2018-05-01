package com.benchmark;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.proto.gen.RuleOuterClass;
import com.rules.RulesHelpers;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.tokensregex.CoreMapExpressionExtractor;
import edu.stanford.nlp.ling.tokensregex.MatchedExpression;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;
import edu.stanford.nlp.ling.tokensregex.parser.ParseException;
import edu.stanford.nlp.ling.tokensregex.parser.TokenSequenceParseException;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.testng.annotations.BeforeTest;

import java.util.Properties;
import java.util.UUID;

public class MatchBenchmark {

    StanfordCoreNLP mPipeline;

    @BeforeTest
    public void setup() {
        //Setup Stanford's NLP Library with
        // set up pipeline properties
        Properties props = new Properties();
        // set the list of annotators to run
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");
        // set a property for an annotator, in this case the coref annotator is being set to use the neural algorithm

        mPipeline = new StanfordCoreNLP(props);
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
    //@Test
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
}

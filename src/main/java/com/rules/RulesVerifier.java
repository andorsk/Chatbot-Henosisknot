package com.rules;

import com.proto.gen.RuleOuterClass;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.tokensregex.CoreMapExpressionExtractor;
import edu.stanford.nlp.ling.tokensregex.MatchedExpression;
import edu.stanford.nlp.ling.tokensregex.parser.ParseException;
import edu.stanford.nlp.ling.tokensregex.parser.TokenSequenceParseException;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.agrona.Verify;

import java.io.InvalidObjectException;

public class RulesVerifier {
    CoreMapExpressionExtractor <MatchedExpression> mExtractor;
    StanfordCoreNLP mPipeline;
    RuleOuterClass.Rules mRules;

    public RulesVerifier(CoreMapExpressionExtractor <MatchedExpression> extractor, StanfordCoreNLP pipeline){
            mExtractor = extractor;
            mPipeline = pipeline;
    }

    public RulesVerifier(CoreMapExpressionExtractor <MatchedExpression> extractor, StanfordCoreNLP pipeline, RuleOuterClass.Rules rules){
        this(extractor, pipeline);
        mRules = rules;
    }
    /**
     * For each rule, they should have an expected match pair and an expected fail pair. This will check the
     * rules upon load and make sure they are working as expected.
     * @return
     */
    public boolean verifyRules(RuleOuterClass.Rules rules) {

        boolean allValidRules = true;
        for(RuleOuterClass.Rule rule : rules.getRulesList()){
            try{
                verifyRule(rule);
            }catch(VerifyError e){
                allValidRules = false;
            }
        }
        return allValidRules;
    }

    public boolean verifyRules(){
        return verifyRules(mRules);
    }


    /**
     * Verification of rules. Based on supplied
     * @param rule
     * @return
     */
    public void verifyRule(RuleOuterClass.Rule rule) throws VerifyError{

            if(!rule.getIsExtraction()){
                return;
            }
            if(!testString(rule.getPositiveMatch(), true)) {
                System.out.println("Positive match failed for rule " + rule.getGuid());
                throw new VerifyError("Positive match failed for rule " + rule.getGuid());
            };

            if(!testString(rule.getNegativeMatch(), false)){
                System.out.println("Negative match failed for rule " + rule.getGuid() + ":" + rule.getPattern());
                throw new VerifyError("Negative match failed for rule " + rule.getGuid() );
            };
    }

    private boolean testString(String text, boolean match) throws VerifyError {
        if(text == null || text.isEmpty()){
            System.out.println("Warning. No verification pattern detected in one of your rules.");
        }
        int _b = match ? 1: 0;
        CoreDocument document = new CoreDocument(text);
        mPipeline.annotate(document);
        if(mExtractor.extractExpressions(document.annotation().get(CoreAnnotations.SentencesAnnotation.class).get(0)).size() != _b){
           return false;
        };
        return true;
    }
}

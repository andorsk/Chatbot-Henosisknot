package com.decisionengine;

import com.proto.gen.RuleOuterClass.*;
import edu.stanford.nlp.pipeline.TokensRegexAnnotator;
import edu.stanford.nlp.naturalli.OpenIE;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.ie.util.RelationTriple;

/**
 * Stanford.nlp.pipeline.TokenRegexAnnotator from Angel X. Chang and Christopher D. Manning. 2014. TokensRegex: Defining cascaded regular expressions over tokens. Stanford University Technical Report, 2014. [bib]
 * Ideally, states not in the rules definitions will get processed, annotated, and that annotation will be feed into
 * a rules_2.txt which will have some statistical implications.
 */

public class RulesDE extends DecisionEngineType {
    protected Rules rules;
    private final String ruleType = "tokens";
    //Input -> Set of Rules
    //Matches set of rules to statment on process command
    //Output -> Statement
    public RulesDE(Rules rules){
        this.rules = rules;
    }

    public String process(CoreDocument doc){
        String response;

        RulesFeatureCreation.generateFeatures(doc);

        return "";
    }

    private static class RulesFeatureCreation {

        public static void generateFeatures(CoreDocument input){
            OpenIE ie = new OpenIE();
            Document doc = new Document(input.text());
            RulesFeatureCreation.generateIE(doc);
            RulesFeatureCreation.generatePattern(doc);
        }

        static void generatePattern(Document doc){

            }

        static void generateIE(Document doc){
            for(Sentence sent: doc.sentences()){
                for(RelationTriple triple : sent.openieTriples()){
                    System.out.println(triple.confidence + "\t" +
                            triple.subjectLemmaGloss() + "\t" +
                            triple.relationLemmaGloss() + "\t" +
                            triple.objectLemmaGloss());
                }
            }
        }
    }

}

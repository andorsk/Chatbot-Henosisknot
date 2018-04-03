package com.nlp;

import edu.stanford.nlp.pipeline.*;
import java.util.*;

public class IntroductionParser extends ParseEngineType {

    @Override
    public CoreDocument parse(String text){
        //Sentence sent = new Sentence(text);
        Properties props = new Properties();
        // set the list of annotators to run
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,depparse,coref,kbp,quote");
        // set a property for an annotator, in this case the coref annotator is being set to use the neural algorithm
        props.setProperty("coref.algorithm", "neural");
        // build pipeline
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        CoreDocument document = new CoreDocument(text);
        pipeline.annotate(document);
        return document;
    }
}

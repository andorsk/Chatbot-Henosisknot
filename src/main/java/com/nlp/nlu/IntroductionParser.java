package com.nlp.nlu;

import edu.stanford.nlp.pipeline.*;
import java.util.*;

public class IntroductionParser extends ParseEngineType {

    private StanfordCoreNLP mPipeline;

    public IntroductionParser(String s){
        System.out.println("Intializing pipeline in Introduction Parser");
        initPipeline(s);
    }

    public IntroductionParser(){
        System.out.println("Intializing pipeline in Introduction Parser");
        initPipeline();
    }

    private void initPipeline() {
        initPipeline("tokenize,ssplit,pos,lemma,ner,parse,depparse,coref,kbp,quote");
    }

    private void initPipeline(String annotator_settings){

        //Sentence sent = new Sentence(text);
        Properties props = new Properties();
        // set the list of annotators to run

        props.setProperty("annotators", annotator_settings); //working with a limited annotator for testing and speed.

        // set a property for an annotator, in this case the coref annotator is being set to use the neural algorithm
        props.setProperty("coref.algorithm", "neural");

        // build pipeline
        mPipeline = new StanfordCoreNLP(props);
    }

    @Override
    public CoreDocument parse(String text){
        CoreDocument document = new CoreDocument(text);
        mPipeline.annotate(document);
        return document;
    }
}

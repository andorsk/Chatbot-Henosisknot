package com.nlp.nlu;

import com.config.PrimaryConfig;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.logging.RedwoodConfiguration;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;
import org.omg.CORBA.DynAnyPackage.InvalidValue;

import java.util.*;

/**
 * The Introduction Parser focuses on getting information such as name and relevant context information
 * on the purpose of the conversation. It is extremely goal oriented as supposed to conversation oriented.
 */
public class IntroductionParser extends ParseEngineType {

    private StanfordCoreNLP mPipeline;
    private volatile boolean isReady = false;

    public StanfordCoreNLP getCurrentPipeline(){
        return mPipeline;
    }

    public IntroductionParser(String s){
        System.out.println("Intializing pipeline in Introduction Parser");
        initPipeline(s);
    }

    public boolean isReady(){
        return this.isReady;
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
        props.put("regexner.mapping", PrimaryConfig.PREDEFINED_STATEMENTS_LOC);
        RedwoodConfiguration.current().clear().apply();

        // build pipeline
        mPipeline = new StanfordCoreNLP(props);
        isReady = true;
    }

    @Override
    public CoreDocument parse(String text) throws ValueException {
        if(text == "" || text == null){
            throw new ValueException("Please provide valid string");
        }

        CoreDocument document = new CoreDocument(text);
        mPipeline.annotate(document);
        return document;
    }
}

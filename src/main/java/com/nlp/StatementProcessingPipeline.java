package com.nlp;

import com.config.StateTracker;
import com.nlp.nlu.IntroductionParser;
import com.nlp.nlu.ParseEngineType;
import com.nlp.nlu.ParsingEngine;
import com.proto.gen.MessageOuterClass;
import com.utilities.StringPreprocessors;
import edu.stanford.nlp.pipeline.CoreDocument;

import java.util.HashMap;


public class StatementProcessingPipeline {
    ParsingEngine mParsingEngine;
    HashMap<String, ParseEngineType> engines = new HashMap<String, ParseEngineType>();

    public StatementProcessingPipeline(){

    }

    public ParsingEngine getParsingEngine(){
        return this.mParsingEngine;
    }

    //Initalize all the engines. Set the introduction parser initially.
    public void start(){
        System.out.println("Starting up parsers");
        this.mParsingEngine = new ParsingEngine();
        Thread initThread = new Thread(new Initalizer(this));
        initThread.start();
    }

    private void initEngines(){
        engines.put("introduction", new IntroductionParser("tokenize,ssplit,pos,lemma,ner"));//tokenize,ssplit,pos,lemma,ner,regexner
    }

    public String process(MessageOuterClass.Message message){
        String text = message.getText();
        text = StringPreprocessors.cleanWhiteSpaces(text);
        CoreDocument doc = parse(text);
        match(doc);
        return generateResponse();
    }

    /**
     * Parses a string input into a document and also annotates. Currently, handles as the controller for different
     * parsing mechanisms dependent on context. To be revisited at a later point of time.
     * @param input
     * @return Core document of input with annotations
     */
    public CoreDocument parse(String input){

        StateTracker.conversationPhaseEnum state = StateTracker.currentState;

        ParsingEngine pe = this.mParsingEngine;

        switch(state) {
            case INTRODUCTION:
                System.out.println("Introduction");
                pe.setParsingType(engines.get("introduction"));
                break;
            case DIALOG:
                System.out.println("Dialog");
                break;
            case CONCLUSION:
                System.out.println("Conclusion");
                break;
            default:
                System.out.println("Default");
        }

        return pe.parse(input);
    }

    /**
     * @param doc
     * @return
     */
    public CoreDocument match(CoreDocument doc){
        return null;
    }

    public String generateResponse(){
        return "This is a response";
    }

    /**
     * Sets up the engines in the background because it takes a while.
     */
    private class Initalizer implements Runnable {
        StatementProcessingPipeline mSPP;


        public Initalizer(StatementProcessingPipeline sp){
         mSPP = sp;
        }

        @Override
        public void run() {
            mSPP.initEngines();
            mSPP.mParsingEngine.setParsingType(engines.get("introduction"));
        }
    }
}


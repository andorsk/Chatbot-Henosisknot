package com.nlp;

import com.config.StateTracker;
import com.nlp.nlu.IntroductionParser;
import com.nlp.nlu.ParsingEngine;
import com.proto.gen.MessageOuterClass;
import com.utilities.StringPreprocessors;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;


public class StatementProcessingPipeline {

    public StatementProcessingPipeline(){

    }

    private void init(){

    }

    public void process(MessageOuterClass.Message message){
        String text = message.getText();
        text = StringPreprocessors.cleanWhiteSpaces(text);
        CoreDocument doc = parse(text);
        match(doc);
    }

    /**
     * Parses a string input into a document and also annotates. Currently, handles as the controller for different
     * parsing mechanisms dependent on context. To be revisited at a later point of time.
     * @param input
     * @return Core document of input with annotations
     */
    public CoreDocument parse(String input){

        StateTracker.conversationPhaseEnum state = StateTracker.currentState;
        ParsingEngine pe = new ParsingEngine();

        switch(state) {
            case INTRODUCTION:
                System.out.println("Introduction");
                pe.setParsingType(new IntroductionParser("tokenize,ssplit,pos,lemma,ner,regexner"));
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
        return "";
    }
}

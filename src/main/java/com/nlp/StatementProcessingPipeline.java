package com.nlp;

import com.config.StateTracker;
import com.nlp.nlu.IntroductionParser;
import com.nlp.nlu.ParsingEngine;
import com.proto.gen.MessageOuterClass;
import com.utilities.StringPreprocessors;
import edu.stanford.nlp.simple.Sentence;

public class StatementProcessingPipeline {

    public StatementProcessingPipeline(){

    }

    private void init(){

    }

    public void process(MessageOuterClass.Message message){
        String text = message.getText();
        text = StringPreprocessors.cleanWhiteSpaces(text);
        parse(text);
    }

    public void parse(String input){

        StateTracker.conversationPhaseEnum state = StateTracker.currentState;
        ParsingEngine pe = new ParsingEngine();

        switch(state) {
            case INTRODUCTION:
                System.out.println("Introduction");
                pe.setParsingType(new IntroductionParser());
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

        pe.parse(input);
    }

    public String generateResponse(){
        return "";
    }
}

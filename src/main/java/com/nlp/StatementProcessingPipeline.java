package com.nlp;

import com.config.StateTracker;
import com.proto.gen.MessageOuterClass;
import com.utilities.StringPreprocessors;
import edu.stanford.nlp.simple.Sentence;

import java.util.List;

public class StatementProcessingPipeline {

    public static void process(MessageOuterClass.Message message){
        String text = message.getText();
        text = StringPreprocessors.cleanWhiteSpaces(text);
        Sentence sent = new Sentence(text);
    }

    public static void parse(String input){
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

        pe.runParse(input);
    }
}

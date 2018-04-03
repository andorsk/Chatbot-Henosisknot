package com.nlp.nlu;

import edu.stanford.nlp.pipeline.*;

public class ParsingEngine {

    ParseEngineType mParser;
    /**
     * Parsing engine separates phase of conversation. In the introduction,
     * Pre-Built Introduction Sentences are added and Pronouns are given more weight
     * to sentence composition;
     */
    public ParsingEngine(ParseEngineType parser){
        this.mParser = parser;
    }

    public CoreDocument parse(String text){
        return this.mParser.parse(text);
    }

    public ParsingEngine(){
    }

    public void setParsingType(ParseEngineType parser){
        this.mParser = parser;
    }

}

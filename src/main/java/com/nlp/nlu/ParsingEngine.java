package com.nlp.nlu;


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

    public void runParse(String text){
        this.mParser.parse(text);
    }

    public ParsingEngine(){
    }

    public void setParsingType(ParseEngineType parser){
        this.mParser = parser;
    }

}

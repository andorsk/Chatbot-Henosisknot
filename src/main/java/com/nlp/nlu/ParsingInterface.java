package com.nlp.nlu;


import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.simple.Document;

public interface ParsingInterface {

    public abstract CoreDocument parse(String text);

}

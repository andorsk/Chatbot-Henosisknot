package com.nlp.nlu;


import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.simple.Document;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

public interface ParsingInterface {

    public boolean isReady();
    public abstract CoreDocument parse(String text) throws ValueException;

}

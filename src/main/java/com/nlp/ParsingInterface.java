package com.nlp;


import edu.stanford.nlp.pipeline.CoreDocument;

public interface ParsingInterface {

    public abstract CoreDocument parse(String text);

}

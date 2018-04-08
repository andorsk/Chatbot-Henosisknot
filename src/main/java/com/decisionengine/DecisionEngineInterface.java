package com.decisionengine;

import edu.stanford.nlp.pipeline.CoreDocument;

public interface DecisionEngineInterface {
    public String process(CoreDocument doc);
}

package com.annotation;

import edu.stanford.nlp.ling.CoreAnnotation;

public class ExtendedCoreAnnotation {

    private ExtendedCoreAnnotation() {
    }

    public static class EmbeddedRule implements CoreAnnotation<String> {
        public EmbeddedRule() {
        }

        public Class<String> getType() {
            return String.class;
        }
    }

}

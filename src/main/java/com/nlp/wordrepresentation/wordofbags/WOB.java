package com.nlp.wordrepresentation.wordofbags;

import java.util.HashMap;
import java.util.Collection;
/**
 * Standard WOB word NLP representation model.
 * Builds a union projection matrix based on the histogram the words.
 */

public class WOB extends HashMap<String, Integer> {

    public void WOB(Collection<String> tokenized_words){
        for (String tokenized_word : tokenized_words) {
            int count = this.containsKey(tokenized_word) ? this.get(tokenized_word) + 1 : 1;
            this.put(tokenized_word, count);
        }
    }

    /**
     * Method adds a WOB map to existing set and creates a union directly. DOES NOT RETURN A COPY and
     * directly impacts the initial set.
     * @param wob
     */
    public void UnionOfWOB(WOB wob) {
        for (String token : wob.keySet()) {
            int count = this.containsKey(token) ? this.get(token) + 1 : 1;
            this.put(token, count);
        }
    }


}


package com.nlp.wordrepresentation.word2vec;


import java.util.Collection;

import org.deeplearning4j.models.sequencevectors.interfaces.SequenceIterator;
import org.deeplearning4j.models.sequencevectors.sequence.Sequence;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentencePreProcessor;

/**
 * Wrapper for the Word2VecWrapper implementation using Deep4J.
 *
 *  Word2vec is a two-layer neural net that processes text.
 *  Its input is a text corpus and its output is a set of vectors
 *
 *  It vectorizes the word and allows a Deep Learning model to efficiently understand it.
 *
 *  The purpose and usefulness of Word2vec is to group the vectors of similar words together in vectorspace. That is, it detects similarities mathematically. Word2vec creates vectors that are distributed numerical representations of word features, features such as the context of individual words.
 *
 */
public class Word2VecWrapper {

    public void create(Collection<String> tokenized_words){


        Word2Vec vec = new Word2Vec.Builder()
                .minWordFrequency(5)
                .iterations(1)
                .layerSize(100)
                .seed(42)
                .windowSize(5)
                .toke
                .build();

        vec.fit();
    }

    private fromFile(String filePath){
        SentenceIterator iter = new LineSentenceIterator(new File(filePath));
        iter.setPreProcessor(new SentencePreProcessor() {
            @Override
            public String preProcess(String sentence) {
                return sentence.toLowerCase();
            }
        });
    }
}


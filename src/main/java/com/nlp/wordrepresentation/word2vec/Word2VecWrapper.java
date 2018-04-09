package com.nlp.wordrepresentation.word2vec;


import java.io.File;
import java.io.IOException;
import java.util.Collection;

import com.config.PrimaryConfig;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.LineSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentencePreProcessor;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;


import static com.config.PrimaryConfig.WORD2VECTORTESTWRITEFILE;

/*
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

    public static void createWordToVectorFile(){

        System.out.println("Load & Vectorize Sentences....");

        SentenceIterator iter = fromFile(PrimaryConfig.WORD2VECTORTESTFILE);

        TokenizerFactory t = new DefaultTokenizerFactory(); //this needs to be swapped with Stanfords NLP libary
        t.setTokenPreProcessor(new CommonPreprocessor());

        System.out.println("Building model....");
        Word2Vec vec = new Word2Vec.Builder()
                .minWordFrequency(5)
                .iterations(1)
                .layerSize(100)
                .seed(42)
                .windowSize(5)
                .iterate(iter)
                .tokenizerFactory(t)
                .build();

        System.out.println("Fitting Word2Vec model...");
        vec.fit();

        WordVectorSerializer.writeWord2VecModel(vec, WORD2VECTORTESTWRITEFILE);
        System.out.println("Writing word vectors to text file....");
        Collection<String> lst = vec.wordsNearestSum("day", 10);
        System.out.println("10 Words closest to 'day': " + lst);


    }


    private static SentenceIterator fromFile(String filePath){
        SentenceIterator iter = new LineSentenceIterator(new File(filePath));
        iter.setPreProcessor(new SentencePreProcessor() {
            @Override
            public String preProcess(String sentence) {
                return sentence.toLowerCase();
            }
        });
        return iter;
    }
}


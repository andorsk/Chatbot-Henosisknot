package com.visualization;

import com.config.PrimaryConfig;
import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.wordstore.VocabCache;
import org.deeplearning4j.plot.BarnesHutTsne;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.primitives.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.api.buffer.DataBuffer;

/*
Heavily borrowed and slightly modified from org.deeplearning4j.examples.nlp.tsne byy agibsonccc on 9/20/14
*/

public class tSNEVisualizer {
    private static Logger log = LoggerFactory.getLogger(tSNEVisualizer.class);
    private HashMap<String, Object> mParam;

    public tSNEVisualizer(HashMap<String, Object> parammap) {
        this.mParam = parammap;
    }

    protected void visualize() {
        int iter = this.mParam.containsKey("iterations") ? Integer.valueOf(this.mParam.get("iterations").toString()) : 100;

        Nd4j.setDataType(DataBuffer.Type.DOUBLE);
        List<String> cacheList = new ArrayList<>(); //cacheList is a dynamic array of strings used to hold all words

        //STEP 2: Turn text input into a list of words
        log.info("Load & Vectorize data....");

        //Get the data of all unique word vectors
        Pair<InMemoryLookupTable, VocabCache> vectors = null;
        VocabCache cache = vectors.getSecond();
        INDArray weights = vectors.getFirst().getSyn0();    //seperate weights of unique words into their own list

        try {
            vectors = WordVectorSerializer.loadTxt(new File(PrimaryConfig.WORD2VECTORTESTWRITEFILE));
            for (int i = 0; i < cache.numWords(); i++)   //seperate strings of words into their own list
                cacheList.add(cache.wordAtIndex(i));

            //STEP 3: build a dual-tree tsne to use later
            log.info("Build model....");
            BarnesHutTsne tsne = new BarnesHutTsne.Builder()
                    .setMaxIter(iter).theta(0.5)
                    .normalize(false)
                    .learningRate(500)
                    .useAdaGrad(false)
//                .usePca(false)
                    .build();

            //STEP 4: establish the tsne values and save them to a file
            log.info("Store TSNE Coordinates for Plotting....");
            String outputFile = "target/archive-tmp/tsne-standard-coords.csv";
            (new File(outputFile)).getParentFile().mkdirs();

            tsne.fit(weights);
            if(new File(PrimaryConfig.tSNEFILE).exists()){
                log.info("Deleted existing tSNEFILE file at " + PrimaryConfig.tSNEFILE);
                new File(PrimaryConfig.tSNEFILE).delete();
            }

            tsne.saveAsFile(cacheList, PrimaryConfig.tSNEFILE);
            //This tsne will use the weights of the vectors as its matrix, have two dimensions, use the words strings as
            //labels, and be written to the outputFile created on the previous line
            // Plot Data with gnuplot
            //    set datafile separator ","
            //    plot 'tsne-standard-coords.csv' using 1:2:3 with labels font "Times,8"
            //!!! Possible error: plot was recently deprecated. Might need to re-do the last line
            //
            // If you use nDims=3 in the call to tsne.plot above, you can use the following gnuplot commands to
            // generate a 3d visualization of the word vectors:
            //    set datafile separator ","
            //    splot 'tsne-standard-coords.csv' using 1:2:3:4 with labels font "Times,8"
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



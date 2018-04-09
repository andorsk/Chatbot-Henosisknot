package com.config;

/**
 * Basic Static retriever to provide base configuration and constants. Will move to a protobuf serialization later with
 * non-embedded config.json.
 */
public class PrimaryConfig {

    public static String DEFAULT_LANGUAGE = "EN_US";
    public static int STATEMENT_TEXT_MAX_LENGTH = 400;
    public static String APP_NAME = "Henosisknot Chatbot";
    public static String PREDEFINED_STATEMENTS_LOC = "/users/andor/workspace/Chatbot-Henosisknot/resources/responses.json";
    public static String INTRODUCTION_SENTENCE = "Hi! I am Andor's Personal Assistant to help you when he's traveling the world. I'm not perfect, but I want to be. Help me get better. \nWhat's your name?";

    //Vector Files are Hardcoded right now and will overwrite eachother.
    public final static String WORD2VECTORTESTFILE = "/users/andor/workspace/Chatbot-Henosisknot/data/sample_raw.txt";
    public final static String WORD2VECTORTESTWRITEFILE = "/users/andor/workspace/Chatbot-Henosisknot/model/wordrepresentation/sample_vec_model.txt";
    public final static String tSNEFILE = "/users/andor/workspace/Chatbot-Henosisknot/data/results_tsne.txt";
}

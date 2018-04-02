package com.config;

/**
 * Basic Static retriever to provide base configuration and constants. Will move to a protobuf serialization later with
 * non-embedded config.json.
 */
public class PrimaryConfig {

    public static String DEFAULT_LANGUAGE = "EN_US";
    public static int STATEMENT_TEXT_MAX_LENGTH = 400;
    public static String APP_NAME = "Henosisknot Chatbot";

}

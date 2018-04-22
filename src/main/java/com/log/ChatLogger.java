package com.log;


import java.io.IOException;
import java.util.logging.*;

public class ChatLogger {

        private final static Logger LOGGER = Logger.getLogger(ChatLogger.class.getName());
        static private FileHandler fileTxt;
        static private SimpleFormatter formatterTxt;
        private static ChatLogger single_instance = null;

        private ChatLogger(){
               start();
        }

        public void start(){

                LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME).setLevel(Level.FINE);
                LOGGER.setLevel(Level.INFO);
                try {
                        fileTxt = new FileHandler("ChatbotLog.txt");

                        // create a TXT formatter
                        formatterTxt = new SimpleFormatter();
                        fileTxt.setFormatter(formatterTxt);
                        LOGGER.addHandler(fileTxt);


                } catch (IOException e) {
                        e.printStackTrace();
                }

        }

        // static method to create instance of Singleton class
        public static ChatLogger getInstance()
        {
                if (single_instance == null)
                        single_instance = new ChatLogger();

                return single_instance;
        }

}

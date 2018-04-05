package com.training;

import java.util.LinkedList;
import java.util.Scanner;

import com.config.PrimaryConfig;
import com.io.IOUtil;
import com.message.MessageHelpers;
import com.proto.gen.MessageOuterClass;
import javafx.util.Pair;
import org.json.simple.JSONObject;

/**
 * Manual Trainer You Program Q/A Responses.
 */
public class ManualTrainer extends TrainerType {

    @Override
    public void train(){
        boolean terminate = false;
        boolean chatturn = false;

        Scanner reader = new Scanner(System.in);  // Reading from System.in
        LinkedList<QAPair> QAPairs = new LinkedList<QAPair>();

        System.out.println("Q:");
        String question = reader.next();
        long questiontime = System.currentTimeMillis();


        while(!terminate){
            System.out.println("A:");
            String answer = reader.next();
            long answertime = System.currentTimeMillis();

            if(answer.equals("terminate")){
                terminate = true;
                break;
            }

            int turn = (chatturn) ? 1 : 0; //keep track of which turn.
            int opp_turn = (!chatturn) ? 1 : 0;

            QAPair qaPair = new QAPair(MessageHelpers.buildMessage(questiontime,  question, turn), MessageHelpers.buildMessage(answertime,  answer, opp_turn));
            QAPairs.add(qaPair);
            question = answer;
            questiontime = answertime;
            chatturn = !chatturn;

        }
        reader.close();

        JSONObject js = new JSONObject();
        js.put("QA", QAPairs);
        IOUtil.appendJSONToFile(js, PrimaryConfig.PREDEFINED_STATEMENTS_LOC);
    }


    private class QAPair extends Pair<MessageOuterClass.Message, MessageOuterClass.Message>{

        private MessageOuterClass.Message mQuestion;
        private MessageOuterClass.Message mAnswer;
        /**
         * Creates a new pair
         *
         * @param key   The key for this pair
         * @param value The value to use for this pair
         */
        public QAPair(MessageOuterClass.Message key, MessageOuterClass.Message value) {
            super(key, value);
            this.mQuestion = key;
            this.mAnswer = value;
        }

        public MessageOuterClass.Message getQuestion(){
            return this.mQuestion;
        }

        public MessageOuterClass.Message getAnswer(){
            return this.mAnswer;
        }

    }

}



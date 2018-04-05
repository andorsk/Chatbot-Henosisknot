package com.training;

import java.util.ArrayList;
import java.util.HashMap;
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
    private HashMap<String, ArrayList<String>> mQAmap = new HashMap<String, ArrayList<String>>();

    @Override
    public void train(){
        boolean terminate = false;
        boolean chatturn = false;

        Scanner reader = new Scanner(System.in);  // Reading from System.in

        System.out.println("Q:");
        String question = reader.nextLine();


        while(!terminate){
            System.out.println("A:");
            String answer = reader.nextLine();

            if(answer.equals("terminate")){
                terminate = true;
                break;
            }

            int turn = (chatturn) ? 1 : 0; //keep track of which turn.
            int opp_turn = (!chatturn) ? 1 : 0;

            ArrayList<String> responses = new ArrayList<String>();
            if(mQAmap.containsKey(question)) {
                responses = mQAmap.get(question);
            }
            responses.add(answer);

            mQAmap.put(question, responses);

            question = answer;
            chatturn = !chatturn;
        }

        reader.close();
        JSONObject js = new JSONObject();
        js.put("Strict", mQAmap);

        IOUtil.appendJSONToFile(js, PrimaryConfig.PREDEFINED_STATEMENTS_LOC);
    }


}



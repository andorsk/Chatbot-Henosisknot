package com.cli;

import com.config.PrimaryConfig;
import com.nlp.StatementProcessingPipeline;
import java.util.Scanner;
import com.message.MessageHelpers;
import com.proto.gen.MessageOuterClass;

public class CLI_Instance {

    protected StatementProcessingPipeline mPipeline;
    private static void main(String args[]){
    }

    public void start(){
        System.out.println("Running CLI version.");
        System.out.println(PrimaryConfig.INTRODUCTION_SENTENCE);
        initPipeline();
        begin();
    }

    void initPipeline(){
        mPipeline = new StatementProcessingPipeline();
    }

    void begin(){
        boolean terminate = false;
        Scanner reader = new Scanner(System.in);

        while(!terminate){
            String line = reader.nextLine();
            if(line.equals("exit")){
                terminate = true;
            }
            MessageOuterClass.Message message = MessageHelpers.buildMessage(System.currentTimeMillis(), line, 1);
            mPipeline.process(message);
        }

        reader.close();
    }
}

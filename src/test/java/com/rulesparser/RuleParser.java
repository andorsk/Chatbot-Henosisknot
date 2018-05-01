package com.rulesparser;

import com.proto.gen.RuleOuterClass.*;
import com.utilities.FileHelpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 *
 */
public class RuleParser {

    Rules mRules;
    Rules.Builder mBuilder;
    File mLoc;


    public RuleParser(String fPath){
        this(new File(fPath));
    }

    public RuleParser(File fPath){
        mBuilder = Rules.newBuilder();
        mLoc = fPath;
        if(fPath.isDirectory()){
            readFromDirectory(fPath);
        }

        if(fPath.isFile()){
            readFromFile(fPath);
        }
    }


    public Rules parseFiles(){
        return mBuilder.build();
    }

    public void updateLoc(File location){
        mLoc = location;
    }


    public void readFromDirectory(File directory){
        List<File> files = FileHelpers.listRecusivelyGetFilesWithExtension(directory,".txt");
        for(File f: files){
            readFromFile(f);
        }
    }

    public void readFromFile(File file){
        try {
            Rules rules = Rules.parseFrom(new FileInputStream(file));
            for(Rule rule : rules.getRulesList()){
                if( rule.getGuid() == null || rule.getGuid() == "" ){
                    rule.toBuilder().setGuid(UUID.randomUUID().toString()).build();
                }
                mBuilder.addRules(rule);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

package com.rules;

import com.proto.gen.RuleOuterClass;
import edu.stanford.nlp.ling.tokensregex.CoreMapExpressionExtractor;
import edu.stanford.nlp.ling.tokensregex.MatchedExpression;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;

import java.io.*;
import java.util.UUID;

public class RulesHelpers {
    /**
     * wrapper function for extractor creation
     */
    public static CoreMapExpressionExtractor<MatchedExpression> createExtractorFromFile(String ruleFile) throws RuntimeException {
        return CoreMapExpressionExtractor.createExtractorFromFiles(TokenSequencePattern.getNewEnv(), ruleFile);
    }


    public static void printRulesToFileInJSON(RuleOuterClass.Rules rules, String filePath){

        try {

            File file = new File(filePath);
            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fs = new FileOutputStream(file);
            String jsonView = com.google.protobuf.util.JsonFormat.printer().print(rules.toBuilder());
            fs.write(jsonView.getBytes());
            fs.flush();
            fs.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public static RuleOuterClass.Rules createSampleRulesFile(int num){

        RuleOuterClass.Rules.Builder rules = RuleOuterClass.Rules.newBuilder();

        for(int i = 0; i < num; i++){
            RuleOuterClass.Rule r = createSampleRule();
            rules.addRules(r);
        }

        return rules.build();
    }

    private static RuleOuterClass.Rule createSampleRule(){
        return RuleOuterClass.Rule.newBuilder()
            .setGuid(UUID.randomUUID().toString())
            .setPattern("/Pattern/ /This/")
            .setPositiveMatch("Pattern This")
            .setNegativeMatch("This doesn't work")
            .setType("token")
            .build();
    }
}
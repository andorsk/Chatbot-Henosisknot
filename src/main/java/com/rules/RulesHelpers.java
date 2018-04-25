package com.rules;

import com.google.protobuf.util.JsonFormat;
import com.io.IOUtil;
import com.proto.gen.RuleOuterClass;
import edu.stanford.nlp.ling.tokensregex.CoreMapExpressionExtractor;
import edu.stanford.nlp.ling.tokensregex.MatchedExpression;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.charset.Charset;
import java.util.UUID;

public class RulesHelpers {
    /**
     * wrapper function for extractor creation
     */
    public static CoreMapExpressionExtractor<MatchedExpression> createExtractorFromFile(String ruleFile) throws RuntimeException {
        return CoreMapExpressionExtractor.createExtractorFromFiles(TokenSequencePattern.getNewEnv(), ruleFile);
    }


    public static RuleOuterClass.Rules readRulesFromJSON(String file){
        RuleOuterClass.Rules rules = null;
        File f = new File(file);
        try {

            RuleOuterClass.Rules.Builder rb = RuleOuterClass.Rules.newBuilder();
            JsonFormat.parser().merge(IOUtil.readFile(file, Charset.defaultCharset()), rb);
            return rb.build();

        } catch (IOException e) {
            System.err.println("Error finding json rules file at " + f.getAbsolutePath());
            e.printStackTrace();
        }

        return rules;
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
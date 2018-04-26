package com.rules;

import com.google.gson.Gson;
import com.google.protobuf.Descriptors;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.io.IOUtil;
import com.proto.gen.RuleOuterClass.Rule;
import com.proto.gen.RuleOuterClass.Rules;
import edu.stanford.nlp.ling.tokensregex.CoreMapExpressionExtractor;
import edu.stanford.nlp.ling.tokensregex.MatchedExpression;
import edu.stanford.nlp.ling.tokensregex.SequenceMatchRules;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;
import edu.stanford.nlp.ling.tokensregex.parser.ParseException;
import edu.stanford.nlp.ling.tokensregex.parser.TokenSequenceParseException;
import edu.stanford.nlp.ling.tokensregex.parser.TokenSequenceParser;
import edu.stanford.nlp.ling.tokensregex.types.Expression;
import edu.stanford.nlp.ling.tokensregex.types.Expressions;
import edu.stanford.nlp.ling.tokensregex.types.Value;
import edu.stanford.nlp.util.ArrayMap;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.simple.JSONObject;
import sun.jvm.hotspot.utilities.ObjectReader;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class RulesHelpers {
    /**
     * wrapper function for extractor creation
     */
    public static CoreMapExpressionExtractor<MatchedExpression> createExtractorFromFile(String ruleFile) throws RuntimeException {
        return CoreMapExpressionExtractor.createExtractorFromFiles(TokenSequencePattern.getNewEnv(), ruleFile);
    }


    public static Rules readRulesFromJSON(String file) {
        Rules rules = null;
        File f = new File(file);
        try {

            Rules.Builder rb = Rules.newBuilder();
            JsonFormat.parser().merge(IOUtil.readFile(file, Charset.defaultCharset()), rb);
            return rb.build();

        } catch (IOException e) {
            System.err.println("Error finding json rules file at " + f.getAbsolutePath());
            e.printStackTrace();
        }

        return rules;
    }


    public static void printRulesToFileInJSON(Rules rules, String filePath) {

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

    public static Rules createSampleRulesFile(int num) {

        Rules.Builder rules = Rules.newBuilder();

        for (int i = 0; i < num; i++) {
            Rule r = createSampleRule();
            rules.addRules(r);
        }

        return rules.build();
    }

    private static Rule createSampleRule() {
        return Rule.newBuilder()
                .setGuid(UUID.randomUUID().toString())
               // .setPattern("/Pattern/ /This/")
                .setPositiveMatch("Pattern This")
                .setNegativeMatch("This doesn't work")
                .setRuleType("token")
                .build();
    }


    /**
     * Converts a rule set into a list of sequence match rules, so the extractor from
     * Stanford's nlp library can take it.
     *
     * Because format of normal object is individual rules, cycle through rules individually
     *
     * TODO: Create a wrapping function that parses rules list directly.
     * @param rules
     * @return
     */
    public static List<SequenceMatchRules.Rule> convertRulesToListofSequenceRules(Rules rules, CoreMapExpressionExtractor extractor) {
        List<SequenceMatchRules.Rule> ls = new ArrayList<SequenceMatchRules.Rule>();
        for (Rule r : rules.getRulesList()) {
            ls.add(convertRuleToSequenceMatchRule(r, extractor));
        }
        return ls;
    }


    public static void RuleToSequenceRulesParser(CoreMapExpressionExtractor extractor, Rules rules){
        TokenSequenceParser sp = new TokenSequenceParser();
        StringBuffer sb = new StringBuffer();
        try {
            for(Rule r: rules.getRulesList()){
                sb.append(createRulesString(r));
            }
            StringReader sr = new StringReader(sb.toString());
            sp.updateExpressionExtractor(extractor,sr);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (TokenSequenceParseException e) {
            e.printStackTrace();
        }
    }

    private static String createRulesString(Rule rule){
        Map<String, Object> map = new HashMap<String, Object>();

        for(Descriptors.FieldDescriptor d : rule.getAllFields().keySet()){
            Object v = rule.getField(d);

            if(d.equals("pattern")){
                v = TokenSequencePattern.compile((String) v);
            }
            map.put(d.getName(), v);
        }

        Gson gson = new Gson();
        String json = gson.toJson(map);
        System.out.println("Converted to " + json);
        return json;
    }
    /**
     * Take a rule and converts in into a sequence match rule.
     * Sequence match rules can be looked up here:
     * https://nlp.stanford.edu/nlp/javadoc/javanlp/edu/stanford/nlp/ling/tokensregex/SequenceMatchRules.html
     * It would have been ideal to directly recreate the rule but it seems that there are a
     * lot of parsing handlers prebuilt. Therefore, instead the function will format the rule as
     *
     * Alternatively also should update the extaction source code from Stanford NLP to handle direct
     * creation.
     *
     * The benefit of this is that it uses the native stanford nlp parser so updates in the parser and
     * handlers are also handled.
     * @param extractor Extractor needs to be passed to retrieve the extraction enviornment. Seeing if there's a way to bypass this.
     * @param rule      Rule type is a custom defined type in the proto definition.
     * @return
     *
     * Note: This method will no longer be used. This ws an attempt to directly write to expression rules.
     * The issue is that the TokenSequenceParser is capable of handling many cases which to bypass would mean
     * that rewriting a lot of the parser class.
     *
     * Token Sequence parser requires a reader, so I'll be tossing it a string as a reader instead.
     */

    @Deprecated
    public static SequenceMatchRules.Rule convertRuleToSequenceMatchRule(Rule rule, CoreMapExpressionExtractor extractor) {

            Map<String, Object> map = new HashMap<String, Object>();
            Map<String, Expression> attributes = new ArrayMap<String, Expression>();

        TokenSequenceParser tsp = new TokenSequenceParser();
            for(Descriptors.FieldDescriptor d : rule.getAllFields().keySet()){
                Object v = rule.getField(d);
                Expression expr = null;

                if(d.getName().equals("pattern")){
                    v = TokenSequencePattern.compile(v.toString());
                }


                attributes.put(d.getName(), tsp.Expression(ex
                ));
                map.put(d.getName(), v);
            }
            SequenceMatchRules.AnnotationExtractRuleCreator aer = new  SequenceMatchRules.AnnotationExtractRuleCreator();
            try {

                Expressions.CompositeValue cv = new Expressions.CompositeValue(attributes, false, new String[0]);
             //   return SequenceMatchRules.createRule(extractor.getEnv(), cv);
            } catch(NullPointerException e){
                System.out.println("E is ...");
                e.printStackTrace();
            }
            return null;
    }
}
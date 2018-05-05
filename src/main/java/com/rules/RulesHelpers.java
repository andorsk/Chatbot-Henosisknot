package com.rules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.protobuf.Descriptors;
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
import edu.stanford.nlp.util.ArrayMap;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * <p>
     * Because format of normal object is individual rules, cycle through rules individually
     * <p>
     * TODO: Create a wrapping function that parses rules list directly.
     *
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


    public static void updateExtractorRules(CoreMapExpressionExtractor extractor, Rules rules) throws TokenSequenceParseException, ParseException {
        TokenSequenceParser sp = new TokenSequenceParser();
        StringBuffer br = new StringBuffer();
        for (Rule rule : rules.getRulesList()) {
            try {
                br.append(createRuleString(rule));
            } catch (RulesException.RulesTypeMissingException e) {
                e.printStackTrace();
            }
        }

        System.out.println("final string is " + br.toString());
        sp.updateExpressionExtractor(extractor, new StringReader(br.toString()));

    }

    public static void clearAndUpdateExtractorRule(CoreMapExpressionExtractor extractor, Rule rule) throws TokenSequenceParseException, ParseException {
        TokenSequenceParser sp = new TokenSequenceParser();
        StringReader sr = null;
        try {
            sr = new StringReader(createRuleString(rule));
        } catch (RulesException.RulesTypeMissingException e) {
            e.printStackTrace();
        }
        sp.updateExpressionExtractor(extractor, sr);
    }


    private static String createAssignmentString(Rule rule) {
        if (!rule.getIsAssignment()) {
            return null;
        }
        String line = String.format("\n %s = { type: \"%s\" , value: \"%s\" \n}", "rule", rule.getRuleType(), rule.getValue());
        return line;
    }

    private static String createRuleString(Rule rule) throws RulesException.RulesTypeMissingException {
        String ret = null;

        if (rule.getIsAssignment()) {
            ret = createAssignmentString(rule);
        } else if (rule.getIsExtraction()) {
            ret = createExtractionRuleString(rule);
        } else {
            throw new RulesException.RulesTypeMissingException("Missing Rule Type: Extraction " + rule.getIsExtraction() + " Assignment " + rule.getIsAssignment());
        }
        return ret;
    }

    /**
     * TODO: This must be revisited at some point! RegEx's assume the guid will always occur after pattern, which is not always true!
     * Best method would be direct.
     * <p>
     * Action field gets encoded with annotation of the object that can be pulled later. This is done for
     * easy access later.
     * <p>
     * Alternative is to store guid as a guid tag and lookup rules after.
     *
     * @param rule
     * @return
     */
    private static String createExtractionRuleString(Rule rule) {
        if (!rule.getIsExtraction()) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuffer action_encoding = new StringBuffer();
        int count = 0;
        action_encoding.append("( ");

        for (Descriptors.FieldDescriptor d : rule.getAllFields().keySet()) {
            count++;
            Object v = rule.getField(d);

            if (d.equals("pattern")) {
                v = (TokenSequencePattern) TokenSequencePattern.compile((String) v);
            }

            String action_string = " Annotate($$0, " + d.getName() + ", \"" + v.toString() + "\")";
            if (count != rule.getAllFields().keySet().size()) {
                action_string = action_string + ",";
            }

            JsonFormat.Printer printer = com.google.protobuf.util.JsonFormat.printer();
            action_encoding.append(" Annotate($0, " + "rule" + ", " + "quotemark " + "Thisisit" + " quotemark " + ")");


            map.put("action", action_string);
        }
            Gson gson = new GsonBuilder().create();
            String json = gson.toJson(map);


            //need a better way to do this. This is ugly.
            Pattern p = Pattern.compile("(?<=\\\"pattern\":)(\")(.*?)");
//        Pattern p = Pattern.compile("(?<=pattern\":).*?([\"])");
            Matcher m = p.matcher(json);
            String s = m.replaceAll("");
            p = Pattern.compile(".(?=\\,\\\"guid\\\":)");//should be forward lookup. need some work on regex.
            m = p.matcher(s);
            String t = m.replaceAll("");

            //need a better way to do this. This is ugly.
            p = Pattern.compile("(?<=\\\"action\":)(\")(.*?)");
            p = Pattern.compile("(?<=\"action\":).*?([\"])");
            m = p.matcher(t);
            s = m.replaceAll("");
            p = Pattern.compile(".(?=\\,\\\"positive_match\\\":)");//should be forward lookup. need some work on regex.
            m = p.matcher(s);
            t = m.replaceAll("");

            t = t.replaceAll("quotemark", "\"");

            System.out.println(t);
            return t;

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
        public static SequenceMatchRules.Rule convertRuleToSequenceMatchRule (Rule rule, CoreMapExpressionExtractor
        extractor){

            Map<String, Object> map = new HashMap<String, Object>();
            Map<String, Expression> attributes = new ArrayMap<String, Expression>();

            TokenSequenceParser tsp = new TokenSequenceParser();
            for (Descriptors.FieldDescriptor d : rule.getAllFields().keySet()) {
                Object v = rule.getField(d);
                Expression expr = null;

                if (d.getName().equals("pattern")) {
                    v = TokenSequencePattern.compile(v.toString());
                }


                // attributes.put(d.getName(), null);
                map.put(d.getName(), v);
            }
            SequenceMatchRules.AnnotationExtractRuleCreator aer = new SequenceMatchRules.AnnotationExtractRuleCreator();
            try {

                Expressions.CompositeValue cv = new Expressions.CompositeValue(attributes, false, new String[0]);
                //   return SequenceMatchRules.createRule(extractor.getEnv(), cv);
            } catch (NullPointerException e) {
                System.out.println("E is ...");
                e.printStackTrace();
            }
            return null;
        }
    }
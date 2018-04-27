package com.rulestest;

import com.proto.gen.RuleOuterClass;
import com.rules.RulesHelpers;
import junit.framework.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.util.UUID;

public class TestReadRulesJSON {

    private String rulesPath;

    @BeforeTest
    private void setUp(){
        ClassLoader classLoader = getClass().getClassLoader();
        rulesPath = "/users/andor/workspace/Chatbot-Henosisknot/src/test/resources/rulesjson/test/test.txt";
    }

    public RuleOuterClass.Rules writeRules(String path){

        RuleOuterClass.Rule r1 = RuleOuterClass.Rule.newBuilder()
                .setGuid(UUID.randomUUID().toString())
              //  .setPattern("/Pattern/ /This/")
                .setPositiveMatch("Pattern This")
                .setNegativeMatch("This doesn't work")
                .setRuleType("token")
                .build();
        RuleOuterClass.Rule r2 = RuleOuterClass.Rule.newBuilder()
                .setGuid(UUID.randomUUID().toString())
                //
                // .setPattern("/Pattern/ /This/")
                .setPositiveMatch("Pattern This")
                .setNegativeMatch("This doesn't work")
                .setRuleType("token")
                .build();

        RuleOuterClass.Rules.Builder rb = RuleOuterClass.Rules.newBuilder();
        rb.addRules(r1);
        rb.addRules(r2);
        RuleOuterClass.Rules rules = rb.build();
        RulesHelpers.printRulesToFileInJSON(rules, path);
        return rules;
    }

    @Test
    public void ReadRulesTest(){
        RuleOuterClass.Rules wrrls = writeRules(rulesPath);
        RuleOuterClass.Rules rules = RulesHelpers.readRulesFromJSON(rulesPath);
        Assert.assertEquals(wrrls, rules);
        Assert.assertEquals(rules.getRulesList().size(), 2);
    }

    @AfterClass
    private void deleteFile(){
        new File(rulesPath).deleteOnExit();
    }
}

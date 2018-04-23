package com.nlptests;

import com.intializersfortest.SetupRulesEngine;
import com.nlp.nlu.IntroductionParser;
import com.nlp.nlu.ParsingEngine;
import edu.stanford.nlp.pipeline.*;
import org.testng.Assert;
import org.testng.annotations.Test;


public class SemanticParsingTest extends SetupRulesEngine {

    @Test
    public void testSentenceNER() {
        System.out.println("Running Semantic Parsing Test");
        CoreDocument doc = this.mParser.parse("My name is Bob Marley. How are you Henosisknot.com?");
        Assert.assertEquals("PERSON", doc.sentences().get(0).nerTags().get(3));
        Assert.assertEquals("PERSON", doc.sentences().get(0).nerTags().get(4));
        Assert.assertEquals("O", doc.sentences().get(0).nerTags().get(1));
        Assert.assertEquals("URL", doc.sentences().get(1).nerTags().get(3));
    }

    final String GILLARTTESTSTRING = "Julia Eileen Gillard (born 29 September 1961) is an Australian politician who was the Prime Minister of Australia and Leader of the Labor Party from 2010 to 2013. She was the first woman to hold either position.\n" +
            "Gillard was born in Barry, Wales, and migrated with her family to Adelaide, South Australia, in 1966, attending Mitcham Demonstration School and Unley High School. In 1982, she moved to Melbourne, Victoria. She graduated from the University of Melbourne with a Bachelor of Arts and a Bachelor of Laws in 1986. In 1987, Gillard joined the law firm Slater & Gordon, specialising in industrial law, before entering politics.\n" +
            "Gillard was first elected to the House of Representatives at the 1998 federal election for the seat of Lalor, Victoria. Following the 2001 federal election, she was elected to the Shadow Cabinet and was given the portfolio of Population and Immigration. In 2003, she took on responsibility for both Reconciliation and Indigenous Affairs and Health. In December 2006, when Kevin Rudd was elected as Labor Leader and became Leader of the Opposition, Gillard was elected unopposed as his deputy.\n" +
            "Gillard became the first female Deputy Prime Minister of Australia upon Labor's victory in the 2007 federal election, also serving as Minister for Education, Minister for Employment and Workplace Relations and Minister for Social Inclusion. On 24 June 2010, after Rudd lost the support of his party and resigned, Gillard was elected unopposed as the Leader of the Labor Party, thus becoming the 27th Prime Minister of Australia. The subsequent 2010 federal election saw the first hung parliament since the 1940 federal election. Gillard was able to form a minority government with the support of a Green MP and three independent MPs. On 26 June 2013, after a leadership spill, Gillard lost the leadership of the Labor Party to Kevin Rudd. Her resignation as Prime Minister took effect the following day.";

}
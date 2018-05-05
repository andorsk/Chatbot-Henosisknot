package com.annotation;

import com.proto.gen.RuleOuterClass;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.Annotator;
import edu.stanford.nlp.util.ArraySet;

import java.util.*;

public class CustomRulesAnnotator implements Annotator {

    HashMap<TokenSequencePattern, RuleOuterClass.Rule> regexToRule = new HashMap<TokenSequencePattern, RuleOuterClass.Rule>();


    public CustomRulesAnnotator(String name, Properties props){
        String rulesFile = props.getProperty("custom.lemma.lemmaFile");
        List<String> rulesEntries = IOUtils.linesFromFile(rulesFile);
        for (String rules : rulesEntries) {
            //insert rule
        }

    }
    @Override
    public void annotate(Annotation annotation) {
        for (CoreLabel token : annotation.get(CoreAnnotations.TokensAnnotation.class)) {
           // String lemma = regexToRule.getOrDefault();
            token.set(CoreAnnotations.TokensAnnotation.class, null);
        }
    }

    @Override
    public Set<Class<? extends CoreAnnotation>> requirementsSatisfied() {
        return Collections.singleton(CoreAnnotations.TokensAnnotation.class);
    }

    @Override
    public Set<Class<? extends CoreAnnotation>> requires() {
        return Collections.unmodifiableSet(new ArraySet<>(Arrays.asList(
                CoreAnnotations.TextAnnotation.class,
                CoreAnnotations.TokensAnnotation.class,
                CoreAnnotations.SentencesAnnotation.class,
                CoreAnnotations.PartOfSpeechAnnotation.class,
                CoreAnnotations.RoleAnnotation.class
        )));
    }
}

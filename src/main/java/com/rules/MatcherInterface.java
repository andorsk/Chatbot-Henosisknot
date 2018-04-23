package com.rules;

import com.proto.gen.MessageOuterClass;

import java.util.List;

public interface MatcherInterface {
    public List<MessageOuterClass.UtterenceMatch> match(String input);
}

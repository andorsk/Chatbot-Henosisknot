package com.config;


//A naive state tracker. Multiple failure points needs to be addressed including single state tracking.
public class StateTracker {
    public static conversationPhaseEnum currentState = conversationPhaseEnum.INTRODUCTION;


    //In the introduction phase, more emphasis is placed into key nouns.
    public enum conversationPhaseEnum {
        INTRODUCTION,
        DIALOG,
        CONCLUSION
    }
}


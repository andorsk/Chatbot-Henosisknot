package com.rules;

public class RulesException extends Exception {
    public static class RequiredFieldMissingException extends Exception {

    }

    public static class RulesTypeMissingException extends Exception {
        RulesTypeMissingException(){
                super();
        }

        RulesTypeMissingException(String message){
            super(message);
        }

    }

}

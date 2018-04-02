package com.utilities;

public class StringPreprocessors {

    public static String trimWhitespaces(String str){
       return str.trim();
    }

    //Remove consecutive whitespaces
    public static String cleanWhiteSpaces(String str){
        str = StringPreprocessors.cleanWhiteSpaces(str);
        return str.replace("  ", " ");
    }
}


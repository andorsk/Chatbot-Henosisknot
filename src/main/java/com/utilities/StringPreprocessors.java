package com.utilities;

public class StringPreprocessors {

    public static String trimWhitespaces(String str){
       return str.trim();
    }

    //Remove consecutive whitespaces
    public static String cleanWhiteSpaces(String str){
        str = StringPreprocessors.trimWhitespaces(str);
        str = str.replace("  ", " ");
        return str;
    }
}


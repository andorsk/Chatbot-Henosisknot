package com.io;

import java.io.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class IOUtil {

    public static JSONArray readJSONFile(String file){
        JSONParser parser = new JSONParser();
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            if (br.readLine() == null) {
                return null;
            }
            Object obj = parser.parse(fr);
            JSONArray jsonObject = (JSONArray) obj;
            return jsonObject;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void appendJSONToFile(JSONObject obj, String fileloc){
        JSONArray arr = readJSONFile(fileloc);
        if(arr == null){
            arr = new JSONArray();
        }
        arr.add(obj);

        try (FileWriter file = new FileWriter(fileloc)) {
            file.write(arr.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

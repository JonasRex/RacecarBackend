/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import dtos.RenameMeDTO;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import com.google.gson.*;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utility {
    private static Gson gson = new GsonBuilder().create();

    public static void printAllProperties() {
        Properties prop = System.getProperties();
        Set<Object> keySet = prop.keySet();
        for (Object obj : keySet) {
            System.out.println("System Property: {"
                    + obj.toString() + ","
                    + System.getProperty(obj.toString()) + "}");
        }
    }

    public static String fetchData(String _url) throws MalformedURLException, IOException {
        URL url = new URL(_url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        //con.setRequestProperty("Accept", "application/json;charset=UTF-8");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("User-Agent", "server");

        Scanner scan = new Scanner(con.getInputStream());
        String jsonStr = null;
        if (scan.hasNext()) {
            jsonStr = scan.nextLine();
        }
        scan.close();
        return jsonStr;
    }

    public static RenameMeDTO json2DTO(String json) throws UnsupportedEncodingException{
        return gson.fromJson(new String(json.getBytes("UTF8")), RenameMeDTO.class);
    }

    public static String DTO2json(RenameMeDTO rmDTO){
        return gson.toJson(rmDTO, RenameMeDTO.class);
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
//        printAllProperties();

        //Test json2DTO and back again
        String str2 = "{'id':1, 'str1':'Dette er den første tekst', 'str2':'Her er den ANDEN'}";
        RenameMeDTO rmDTO = json2DTO(str2);
        System.out.println(rmDTO);

        String backAgain = DTO2json(rmDTO);
        System.out.println(backAgain);
    }
}

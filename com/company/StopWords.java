package com.company;

/**
 * Created by lynn on 10/5/17.
 */
import java.util.*;
import java.io.*;

public class StopWords {
    public static ArrayList<String> StopWords(ArrayList<String> TokenWord) throws IOException {


        //Store Stopword into an arraylist
        ArrayList<String> stopWord = new ArrayList<String>();
        //modify pathname if path is changed
        Scanner sc = new Scanner(new File("/Users/lynn/Desktop/hw1_PA/StopWordList"));
        while (sc.hasNext()) {
            stopWord.add(sc.next());
        }
        sc.close();


        //delete stopword in words list
        ArrayList<String> StopRemWord = new ArrayList<String>();
        for (String word : TokenWord) {
            if (!stopWord.contains(word.toLowerCase())) {
                StopRemWord.add(word);
            }
        }
        return StopRemWord;
    }
}

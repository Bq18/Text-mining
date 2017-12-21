package Token;

/**
 * Created by lynn on 10/5/17.
 */

import java.io.*;
import java.util.*;
import java.lang.String;

public class Tokenizer {
    public ArrayList<String> Tokenizer(File f) {

        //Read input text and store words into array
        BufferedReader reader = null;
        ArrayList<String> textToken = new ArrayList<String>();
        String[] words = null;
        try {
            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new FileReader(f));
            String line = null;
            while ((line = reader.readLine()) != null) {
                line = line.replaceAll("\\p{Punct}+", " ");
                line = line.toLowerCase();
                sb.append(line).append(" ");
            }
            String text = sb.toString();
            words = text.split(" +");
            textToken = new ArrayList<String>(Arrays.asList(words));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return textToken;
    }

    public ArrayList<String> TitleTokenizer(File f) {

        //Read the first line of input file and store words into array
        BufferedReader reader = null;
        ArrayList<String> LinetextToken = new ArrayList<String>();
        String[] words = null;
        try {
            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new FileReader(f));
            String line = null;
                line = reader.readLine();
                line = line.replaceAll("\\p{Punct}+", " ");
                line = line.toLowerCase();
                sb.append(line).append(" ");
            String text = sb.toString();
            words = text.split(" +");
            LinetextToken = new ArrayList<String>(Arrays.asList(words));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return LinetextToken;
    }
}



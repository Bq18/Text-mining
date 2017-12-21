package com.company;

/**
 * Created by lynn on 10/5/17.
 */
import java.util.*;
public class N_Grams {

    ArrayList<String> Nword = new ArrayList<String>();


    public HashMap<String,Integer> N_Grams(ArrayList<String> word, int n) {

        for (int num = 1; num <= n; num++) {
            NTerm(word, num);
            // System.out.println("")
        }
/*
        for(int i = 0;i<Nword.size();i++){
           System.out.println(Nword.get(i));
        }
*/
       //count Freq of each term
       HashMap<String, Integer> termFreq = new HashMap<String, Integer>();
        for (int i = 0; i < Nword.size(); i++) {
                String key = Nword.get(i);
                if(!termFreq.containsKey(key)) {
                    termFreq.put(key, 1);
                } else{
                    termFreq.put(key,termFreq.get(key) + 1);
                }
        }

        Iterator it = termFreq.entrySet().iterator();
       //System.out.print(termFreq);
        return termFreq;
    }

    public void NTerm(ArrayList<String> array, int n) {
        String word = "";
        for (int i = 0; i <= array.size() - n; i++) {
            for (int j = 0; j < n; j++) {
                word = word + " " + array.get(i + j);
            }
            word = word.trim();
            Nword.add(word);
            word = "";
        }
    }
/*
    public static void main(String[] args) {
        ArrayList<String> x = new ArrayList<String>(Arrays.asList("I", "have", "a", "have", "a", "have", "a"));
        ArrayList<String> map = new ArrayList<String>();
        N_Grams nG = new N_Grams();
        map = nG.N_Grams(x, 3);

        /*
        Iterator it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                System.out.println(pair.getKey() + " = " + pair.getValue());
                it.remove(); // avoids a ConcurrentModificationException
            }
    }

        System.out.print(map);
    }
*/
}

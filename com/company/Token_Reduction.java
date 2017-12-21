package com.company;

import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.HashMap;

/**
 * Created by lynn on 10/16/17.
 */
public class Token_Reduction {


    public  ArrayList<String> Token_Reduction(HashMap<String,Integer> termFreq) {
        int value = 0;
        Iterator it = termFreq.entrySet().iterator();
        ArrayList<String> termList = new ArrayList<String>();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            value = value + Integer.parseInt(pair.getValue().toString());
            //it.remove(); // avoids a ConcurrentModificationException
        }
        double Avg = value / (double) termFreq.size();


        //filter by Freq
        Iterator its = termFreq.entrySet().iterator();
        while (its.hasNext()) {
            Map.Entry pair = (Map.Entry) its.next();
            if (Integer.parseInt(pair.getValue().toString()) < Avg+3) {
                its.remove();
            }
        }

        //convert hashmap to arraylist
        Iterator itFreq = termFreq.entrySet().iterator();
        while (itFreq.hasNext()) {
            Map.Entry pair = (Map.Entry) itFreq.next();
            termList.add(pair.getKey().toString());
        }

        return termList;
    }
}

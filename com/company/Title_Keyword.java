package com.company;

/**
 * Created by lynn on 10/12/17.
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Title_Keyword {
    //get keyword of the tile that has 80% possiblity to occur among all the files in folder
    public String Title_Keyword(ArrayList<ArrayList<String>> titleText){
        String keyword = "";
        Map<String,Integer> wordFreq = new LinkedHashMap<String,Integer>();
        ArrayList<String> text = new ArrayList<String>();
        int size = titleText.size();

        for (int i = 0; i < size; i++){
            text = titleText.get(i);
            for (int j = 0; j < text.size();j++){
                if(!wordFreq.containsKey(text.get(j))){
                    wordFreq.put(text.get(j), 1);
                }else{
                    wordFreq.put(text.get(j),wordFreq.get(text.get(j))+1);
                }
            }
        }

        Iterator it = wordFreq.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String,Integer> pair = (Map.Entry)it.next();
            if (pair.getValue()/(double)size >= 0.8) {
                keyword = keyword + " " + pair.getKey().toString();
            }
        }
        if (keyword == ""){
            Iterator it2 = wordFreq.entrySet().iterator();
            while (it2.hasNext()) {
                Map.Entry<String,Integer> pair = (Map.Entry)it2.next();
                if (pair.getValue()/(double)size >= 0.6) {
                    keyword = keyword + " " + pair.getKey().toString();
                }
            }
        }
        return keyword;
    }
}

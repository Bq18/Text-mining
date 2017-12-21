package Token;

/**
 * Created by lynn on 10/6/17.
 */
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Math.*;
import java.util.*;



public class TF_IDF {
    ArrayList termDocs = new ArrayList<HashMap<String, Double>>();
    int termNum;
    int docSize;

    public String[][] TF_IDF(ArrayList<ArrayList<String>> docs,ArrayList<HashMap<String,Integer>> totalTerm) throws IOException {
        HashMap<String, Double> termMap = new LinkedHashMap<String, Double>();
        String [][] TFIDF_Matrix= new String[termNum][docSize+1];
        //store all the terms of the documents into a Hashtable
        docSize = docs.size();
        for (int i = 0; i < docSize; i++) {
            ArrayList<String> words = docs.get(i);
            for (String word : words) {
                if (!termMap.containsKey(word)) {
                    termMap.put(word, 0.0);
                }
            }
        }
        termNum = termMap.size();

        TF(termMap,totalTerm);
        HashMap<String, Double> IDFmap = idf(termMap,totalTerm);
        print_idf(IDFmap);
        TFIDF_Cal(IDFmap);

        TFIDF_Matrix = transMatrix();

        //Insert terms into the first row of the matrix
        int row  = 0;
        Iterator it = termMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Double> pair = (Map.Entry) it.next();
            TFIDF_Matrix[row][0] = pair.getKey();
            row++;
        }
        return TFIDF_Matrix;
    }

    public void TFIDF_Cal(HashMap<String, Double> IDFmap) {
        for (Object Map : termDocs) {
            HashMap<String, Double> termMap = (HashMap<String, Double>) Map;

            Iterator it = termMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Double> pair = (Map.Entry) it.next();
                double idfVal = IDFmap.get(pair.getKey());
                termMap.put(pair.getKey(), pair.getValue() * idfVal);
            }
        }
    }

    public void print_idf(HashMap<String, Double> IDFmap)throws IOException{
        PrintWriter pw = new PrintWriter(new File("output/IDF.csv"));
        Iterator it = IDFmap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Double> pair = (Map.Entry) it.next();
            pw.append(pair.getKey());
            pw.append(',');
            pw.append(pair.getValue().toString());
            pw.append('\n');
        }
        pw.close();
    }

    public void TF(HashMap<String,Double> termMap,ArrayList<HashMap<String,Integer>> totalTerm) {
        for (int j = 0; j < docSize; j++) {
            HashMap<String, Double> termFreq = new LinkedHashMap<String, Double>();
            termFreq.putAll(termMap);
            int totalFreq = 0;
            HashMap<String,Integer> singtotalTerm = totalTerm.get(j);;
            //count the raw frequence of each term in one document
            Iterator it = singtotalTerm.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String,Integer> pair = (Map.Entry)it.next();
                if (termFreq.containsKey(pair.getKey())) {
                    termFreq.put(pair.getKey(), pair.getValue()+0.0);

                }
                //System.out.println("file" + j + " " + pair.getKey() + " " + pair.getValue());
                totalFreq = totalFreq +pair.getValue();
            }
            //count the relative term frequence in one document
            Iterator it2 = termFreq.entrySet().iterator();
            while (it2.hasNext()) {
                Map.Entry<String, Double> pair = (Map.Entry) it2.next();
                termFreq.put(pair.getKey(), pair.getValue()/totalFreq);
            }

            termDocs.add(j, termFreq);
        }
    }

    public HashMap<String,Double> idf (HashMap<String,Double>IDF,ArrayList<HashMap<String,Integer>> totalTerm){
        int N = totalTerm.size();
        for(Object Map : totalTerm){
            HashMap<String,Integer> termMap = (HashMap<String,Integer>)Map;

            Iterator it = termMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String,Integer> pair = (Map.Entry)it.next();
                if(IDF.containsKey(pair.getKey())){
                    IDF.put(pair.getKey(),IDF.get(pair.getKey())+1.0);
                }
            }
        }
        Iterator it = IDF.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String,Double> pair = (Map.Entry)it.next();
            IDF.put(pair.getKey(),Math.log(N/pair.getValue()+1));
        }

        return IDF;
    }

    public String[][] transMatrix(){
        String [][] TFIDF_M= new String[termNum][docSize+1];
        for (int col = 0; col < docSize;col++){
            int row = 0;
            // System.out.print("Document: " + m);
            HashMap<String,Double> tempTFIDF = new LinkedHashMap<String,Double>();
            tempTFIDF = (LinkedHashMap)termDocs.get(col);

            Iterator it = tempTFIDF.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String,Double> pair = (Map.Entry)it.next();
                TFIDF_M[row][col+1] = pair.getValue().toString();
                row++;
            }
        }
     return TFIDF_M;

    }
}

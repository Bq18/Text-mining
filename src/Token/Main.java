package Token;
import java.io.*;
import java.util.*;
import java.lang.*;

/*
public class Main{


    public static void main(String []args) throws IOException{
        //String filename = "New TF_IDF_Matrix" + ".txt";
        //String filename2 = "New TF_IDF_Matrix_2" + ".txt";

        //PrintWriter writer = new PrintWriter(filename, "UTF-8");
        //PrintWriter writer2 = new PrintWriter(filename2, "UTF-8");

        ArrayList<ArrayList<String>> finText = new ArrayList<ArrayList<String>>();
        ArrayList<HashMap<String,Integer>> totalTerm = new ArrayList<HashMap<String,Integer>>();
        //ArrayList<ArrayList<String>> titleText = new ArrayList<ArrayList<String>>();
        ArrayList<Integer> counterSize = new ArrayList<Integer>();

        for (int i =1 ; i <=15; i++) {

            //String titleKeyword;

            //modify "target_dir" if path is changed
            String target_dir = "input/DataSet/C";
            String str2 = Integer.toString(i);
            target_dir = target_dir + str2;
            File dir = new File(target_dir);
            File[] files = dir.listFiles();

            ArrayList<String> nGram = null;
            //ArrayList<String> lemmaTitle = null;
            HashMap<String,Integer> termFreq = new LinkedHashMap<String,Integer>();
            int counter = 0;
            if (files != null) {
                for (File f : files) {
                    counter++;
                    //Token
                    if (f.isFile()) {
                        Tokenizer tokenText = new Tokenizer();
                        ArrayList<String> tokenWord = new ArrayList<String>();
                        tokenWord = tokenText.Tokenizer(f);


                        //stopWord
                        StopWords stopText = new StopWords();
                        ArrayList<String> stopWord = new ArrayList<String>();
                        stopWord = stopText.StopWords(tokenWord);



                        //Lemma & NER
                        StringBuilder sb = new StringBuilder();
                        for (String s : stopWord) {
                            sb.append(s);
                            sb.append(" ");
                        }
                        String stopT = sb.toString();
                        StanfordLemma_NER slem = new StanfordLemma_NER();
                        ArrayList<String> lemmaWord = new ArrayList<String>();
                        lemmaWord = slem.lemma_ner(stopT);

                        //stopWord
                        ArrayList<String> stopWord2 = new ArrayList<String>();
                        StopWords stopTexT2 = new StopWords();
                        stopWord2 = stopTexT2.StopWords(lemmaWord);

                        //Apply Sliding Window Algorithm
                        N_Grams n_gramsT = new N_Grams();
                        termFreq = n_gramsT.N_Grams(stopWord2, 5);

                        //ApplyReduction Algorithm
                        Token_Reduction tokenR = new Token_Reduction();
                        nGram = new ArrayList<String>();
                        HashMap<String,Integer> ngramFreq = new LinkedHashMap<String,Integer>();
                        ngramFreq.putAll(termFreq);
                        nGram = tokenR.Token_Reduction(ngramFreq);

                        finText.add(nGram);
                        //titleText.add(lemmaTitle);
                        totalTerm.add(termFreq);
                    }
                }

            }
           // writer.println("\n\n\n");
            counterSize.add(counter);
        }
        //TF-IDF print matrix
        TF_IDF tfidf = new TF_IDF();
        String[][] TFIDF_M = null;
        TFIDF_M = tfidf.TF_IDF(finText,totalTerm);
        //writer.println( "This is the Matrix in Folder " + i );
        //writer.println("\n");

      //  writer.println("First TF-IDF Scheme");
        //print title theme
        //int value = 20 * finText.size();
       // String str = "%" + value + "s";
        //writer.printf(str, "  Folder " + i + " Title Keyword : " + titleKeyword);

      //  writer.println();


        //print document line
       // writer.printf("%20s", " ");
       // for (int i = 0; i < counterSize.size();i++) {
            //for (int p = 1; p <= counterSize.get(i); p++) {
               // writer.printf("%20s", "document" + p);
        //    }
       // }
       // writer.println();

        //print tf-idf matrix
        /*for (int row = 0; row < TFIDF_M.length; row++) {
            for (int col = 0; col < TFIDF_M[row].length; col++) {
                writer.printf("%20s", TFIDF_M[row][col] + " ");
            }
            writer.println();
        }


        //TF-IDF print matrix

        TF_IDF2 tfidf2 = new TF_IDF2();
        String[][] TFIDF_M2 = null;
        TFIDF_M2 = tfidf2.TF_IDF2(finText,totalTerm);
        //writer.println( "This is the Matrix in Folder " + i );
        //writer.println("\n");
       /*
        writer2.println("Second TF-IDF Scheme");
        //print title theme
        //int value = 20 * finText.size();
        // String str = "%" + value + "s";
        //writer.printf(str, "  Folder " + i + " Title Keyword : " + titleKeyword);

        writer2.println();


        //print document line
        writer2.printf("%20s", " ");
        for (int m = 0; m < counterSize.size();m++) {
            for (int p = 1; p <= counterSize.get(m); p++) {
                writer2.printf("%20s", "document" + p);
            }
        }
        writer2.println();

        //print tf-idf matrix
        for (int row = 0; row < TFIDF_M2.length; row++) {
            for (int col = 0; col < TFIDF_M2[row].length; col++) {
                writer2.printf("%20s", TFIDF_M2[row][col] + " ");
            }
            writer2.println();
        }

        writer.close();
        writer2.close();

        PrintWriter pw = new PrintWriter(new File("output/LargeMatrix1.data"));
        StringBuilder sb = new StringBuilder();


        //print tf-idf matrix

        System.out.println("terms" + TFIDF_M.length);
        System.out.println("counter" + counterSize);

        int index = 0;
            int iter = 0;

            PrintWriter pw3 = new PrintWriter(new File("output/termlist.csv"));
            for(int row = 0; row <TFIDF_M.length;row++){
                pw3.append(TFIDF_M[row][0]);
                pw3.append('\n');
            }
            pw3.close();

            for (int col = 1; col < TFIDF_M[0].length; col++) {
                index++;
                if( index <= counterSize.get(iter)){
                    String docN = "C" + (iter + 1)+"_" + index;
                    sb.append(docN);
                    sb.append(',');
                }else{
                    iter++;
                    index = 1;
                    String docN = "C" + (iter + 1)+"_" + index;
                    sb.append(docN);
                    sb.append(',');
                }

                for (int row = 0; row < TFIDF_M.length; row++) {
                    sb.append(TFIDF_M[row][col]);
                    if(row != TFIDF_M.length-1) {
                        sb.append(',');
                    }
            }
            sb.append('\n');
        }
        pw.write(sb.toString());
        pw.close();

        PrintWriter pw2 = new PrintWriter(new File("output/CounterSize.csv"));

        for(int i = 0; i < counterSize.size();i++){
           pw2.append(counterSize.get(i).toString());
            if( i != counterSize.size()-1) {
                pw2.append(',');
            }
        }
        pw2.close();
    }
}*/

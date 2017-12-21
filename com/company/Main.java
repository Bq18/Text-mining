package com.company;
import java.io.*;
import java.util.*;
import java.lang.*;

public class Main{


    public static void main(String []args) throws IOException{
        String filename = "TF_IDF_Matrix" + ".txt";
        PrintWriter writer = new PrintWriter(filename, "UTF-8");

        for (int i =1 ; i <=15; i++) {

            ArrayList<ArrayList<String>> finText = new ArrayList<ArrayList<String>>();
            ArrayList<HashMap<String,Integer>> totalTerm = new ArrayList<HashMap<String,Integer>>();
            ArrayList<ArrayList<String>> titleText = new ArrayList<ArrayList<String>>();
            String titleKeyword;

            //modify "target_dir" if path is changed
            String target_dir = "/Users/lynn/Desktop/hw1_PA/Dataset/c";
            String str2 = Integer.toString(i);
            target_dir = target_dir + str2;
            File dir = new File(target_dir);
            File[] files = dir.listFiles();

            ArrayList<String> nGram = null;
            ArrayList<String> lemmaTitle = null;
            HashMap<String,Integer> termFreq = new LinkedHashMap<String,Integer>();

            if (files != null) {
                for (File f : files) {
                    //Token
                    if (f.isFile()) {
                        Tokenizer tokenText = new Tokenizer();
                        ArrayList<String> tokenWord = new ArrayList<String>();
                        tokenWord = tokenText.Tokenizer(f);

                        //title tokenizer
                        ArrayList<String> titleLine = new ArrayList<String>();
                        titleLine = tokenText.TitleTokenizer(f);

                        //stopWord
                        StopWords stopText = new StopWords();
                        ArrayList<String> stopWord = new ArrayList<String>();
                        stopWord = stopText.StopWords(tokenWord);


                        //title stopword
                        ArrayList<String> titleStop = new ArrayList<String>();
                        titleStop = stopText.StopWords(titleLine);


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


                        //title Lemma & NER
                        StringBuilder sb2 = new StringBuilder();
                        for (String s2 : titleStop) {
                            sb2.append(s2);
                            sb2.append(" ");
                        }

                        String stopTitle = sb2.toString();
                        StanfordLemma_NER slem2 = new StanfordLemma_NER();

                        lemmaTitle = new ArrayList<String>();
                        lemmaTitle = slem2.lemma_ner(stopTitle);

                        //remove StopWord after Lemmatization and NER
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
                        titleText.add(lemmaTitle);
                        totalTerm.add(termFreq);
                    }
                }

                //Title keyword Extraction
                Title_Keyword titleKey = new Title_Keyword();
                titleKeyword = titleKey.Title_Keyword(titleText);


                //TF-IDF print matrix
                TF_IDF tfidf = new TF_IDF();
                TF_IDF2 tfidf2 = new TF_IDF2();

                String[][] TFIDF_M = null;
                String[][] TFIDF_M2 = null;

                TFIDF_M = tfidf.TF_IDF(finText,totalTerm);


                PrintWriter writer2 = new PrintWriter("Terms_In_Folder " + i, "UTF-8");
                writer2.println(titleKeyword);
                for (int mn = 0; mn < TFIDF_M.length; mn++) {
                    writer2.println(TFIDF_M[mn][0]);
                }
                writer2.close();

                writer.println( "This is the Matrix in Folder " + i );
                writer.println("\n");
                writer.println("First TF-IDF Scheme");

                //print title theme
                    int value = 20 * finText.size();
                    String str = "%" + value + "s";
                    writer.printf(str, "  Folder " + i + " Title Keyword : " + titleKeyword);

                    writer.println();


                //print document line
                writer.printf("%20s", " ");
                for(int p = 1; p <= finText.size();p++){
                    writer.printf("%20s","document" + p);
                }
                writer.println();

                //print tf-idf matrix
                for (int row = 0; row < TFIDF_M.length; row++) {
                    for (int col = 0; col < TFIDF_M[row].length; col++) {
                        writer.printf("%20s", TFIDF_M[row][col] + " ");
                    }
                    writer.println();
                }




                writer.println("\n");


                TFIDF_M2 = tfidf2.TF_IDF2(finText,totalTerm);
                writer.println("Second TF-IDF Scheme");

                  //print title theme
                    int value2 = 20 * finText.size();
                    String strm = "%" + value + "s";
                    writer.printf(strm, "  Folder " + i + " Title Keyword : " + titleKeyword);
                    writer.println();


                //print document line
                writer.printf("%20s", " ");
                for(int p = 1; p <= finText.size();p++){
                    writer.printf("%20s","document" + p);
                }
                writer.println();


                //print tf-idf
                for (int row = 0; row < TFIDF_M2.length; row++) {
                    for (int col = 0; col < TFIDF_M2[row].length; col++) {
                        writer.printf("%20s", TFIDF_M2[row][col] + " ");
                    }
                    writer.println();
                }
            }
            writer.println("\n\n\n");
        }
        writer.close();
    }
}
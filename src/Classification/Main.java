package Classification;

/**
 * Created by lynn on 11/24/17.
 */
import Token.*;

import java.util.*;
import java.io.*;
public class Main {
    static String[][] wtfTrain;
    static String[][] wtfTest;
    static int [] trainNum;
    static int [] testNum;

    static HashMap<String,String> label = new HashMap<>();
    static ArrayList<ArrayList<String>> finText = new ArrayList<ArrayList<String>>();
    static ArrayList<HashMap<String,Double>> TF_total = new ArrayList<>();
    static ArrayList<HashMap<String,Double>> TFIDF_total = new ArrayList<>();

    public static void main(String []args) throws Exception {

        Main mainClass = new Main();

        //read csv file generated from hw1 and store into arraylist
        String csvFile = "input/LargeMatrix1.csv";
        ArrayList<String> lineArray = new ArrayList<String>();
        Scanner scanner = new Scanner(new File(csvFile));
        String line = "";
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            lineArray.add(line);
        }

        //convert arraylist to string matrix
        String[][] multD = new String[lineArray.size()][];
        for (int i = 0; i < multD.length; i++) {
            multD[i] = lineArray.get(i).split(",");
        }

        //Read Term_IDF into Hashmap
        String IDF = "input/IDF.csv";
        HashMap<String, Double> Term_IDF = new LinkedHashMap<>();
        Scanner scanner2 = new Scanner(new File(IDF));
        String line2 = "";
        while (scanner2.hasNextLine()) {
            line2 = scanner2.nextLine();
            String[] split1 = line2.split(",");
            Term_IDF.put(split1[0], Double.parseDouble(split1[1]));
        }


        //get label for each folder
        mainClass.labelFolder();


        //10 folds cross validation to pick mminimal k
        Cross_Val validation = new Cross_Val();
        HashMap<Integer, Double> k_err = new HashMap<>();
        int minK = 10;
        double min = 10;
        for (int i = 1; i <= 10; i++) {
            double value = validation.Cross_Val(multD, 10, i, label);
            k_err.put(i, value);
            if (min > value) {
                min = value;
                minK = i;
            }
        }
        System.out.println("k-error: " + k_err);
        System.out.println("min_k :" + minK);
        System.out.println("min_value: " + min);

        //write k-err result to csv file
        PrintWriter pw = new PrintWriter(new File("output/K_ERROR.csv"));
        Iterator it = k_err.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Double> pair = (Map.Entry) it.next();
            pw.append(String.valueOf(pair.getKey()));
            pw.append(',');
            pw.append(pair.getValue().toString());
            pw.append('\n');
        }
        pw.close();

        //Test label of unseen dataset
        mainClass.unseen_test(multD,Term_IDF,minK);



        //library
        HashMap<Integer, Double> k_err_lib = new HashMap<>();
        for(int k = 1; k <=10; k++) {
            KNN_Lib klib = new KNN_Lib();
            double err = klib.knn_lib("input/matrix_label.data", k);
            k_err_lib.put(k,err);
        }

        PrintWriter pw3 = new PrintWriter(new File("output/K_ERROR_lib.csv"));
        Iterator it2 = k_err_lib.entrySet().iterator();
        while (it2.hasNext()) {
            Map.Entry<String, Double> pair = (Map.Entry) it2.next();
            pw3.append(String.valueOf(pair.getKey()));
            pw3.append(',');
            pw3.append(pair.getValue().toString());
            pw3.append('\n');
        }
        pw3.close();

    }

    /*  Purpose: Predict labels for unseen dataset. For a new test document, call text preprocessing function from hw1
    *            which is lied in token package to parse text into token. Then calcuate term frequency of the new
    *            document based the termlist from traing dataset. If the term in the termlist doesn't appear in the
    *            new document, the term frequecy in the test document is 0. IDF for each term is given which has been
    *            calculated in hw1. TFIDF = TF* IDF. Through this way, we can convert a set of new documents into the
    *            TF_IDF vectors. Then call KNN to predict label of each document.
    *   Arguments: original dataset matrix, IDF value of each terms in the training dataset, the number of nearest
    *               with minimal cross validation error.
    *   Return: None. (Result is writen to ouput/unseen_predict.csv file)
    * */
    public void unseen_test(String[][] multD,HashMap<String, Double> Term_IDF,int minK)throws IOException{
        // Test filename
        ArrayList<String> filename = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            for (int j = 1; j <= 10; j++) {
                String name = "";
                if (i == 1) {
                    name = i + "_b_" + j;
                } else {
                    name = i + "_a_" + j;
                }
                filename.add(name);
            }
        }

        //Actual Label for test file
        HashMap<String, String> testLabel = new HashMap<>();
        testLabel.put("2", "Predictive Analytics");
        testLabel.put("1", "Harvey & Irmas");


        //Train Dataset
        ArrayList<String[]> train = new ArrayList<>();
        for (int i = 0; i < multD.length; i++) {
            train.add(multD[i]);
        }

        // preprocess testDataset => tokenizer/Remove StopWords/Lemma
        preprocess();

        //calculate TF
        TF(Term_IDF);

        //calculate TF_IDF
        cal_TFIDF(Term_IDF);

        //convert TF_IDF ArrayList<HashMap<>> to ArrayList<String[]>
        ArrayList<String[]> test_TFIDF = new ArrayList<>();
        for (int i = 0; i < TFIDF_total.size(); i++) {
            String[] myArray = new String[Term_IDF.size() + 1];

            myArray[0] = filename.get(i);
            int m = 1;
            Iterator it3 = TFIDF_total.get(i).entrySet().iterator();
            while (it3.hasNext()) {
                Map.Entry pair = (Map.Entry) it3.next();
                myArray[m++] = pair.getValue().toString();
            }
            test_TFIDF.add(myArray);
        }


        //predict label on test data
        System.out.println("Predict Label on unlabelled dataset");
        for (int i = 0; i < filename.size(); i++) {
            String name = filename.get(i);
            KNN knn = new KNN();
            String labeltest = knn.KNN(name, train, test_TFIDF, label, minK);
            System.out.println("FileName: " + name);
            //System.out.println("Actual Label is " + testLabel.get(name.substring(0,1)));
            System.out.println("Predict Label is " + labeltest);
            System.out.println("");
        }
        System.out.println("");
    }

    //calculate TF_IDF
    public void cal_TFIDF(HashMap<String, Double> IDF)throws IOException{
        for(int i = 0; i <TF_total.size();i++){
            HashMap<String, Double> TF_IDF = new LinkedHashMap<>();
            Iterator it = TF_total .get(i).entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                double idf_value = IDF.get(pair.getKey());
                double value = idf_value * Double.valueOf(pair.getValue().toString());
                TF_IDF.put(pair.getKey().toString(),value);
            }
            TFIDF_total.add(TF_IDF);
        }
        //write one document tfidf to csv file for future use
        PrintWriter pw = new PrintWriter(new File("output/Single_TFIDF.csv"));
        Iterator it2 = TFIDF_total.get(1).entrySet().iterator();
        while (it2.hasNext()) {
            Map.Entry<String, Double> pair = (Map.Entry) it2.next();
            pw.append(String.valueOf(pair.getKey()));
            pw.append(',');
            pw.append(pair.getValue().toString());
            pw.append('\n');
        }
        pw.close();

    }


    public void TF(HashMap<String, Double> Term) throws IOException {

        for (int i = 0; i < finText.size(); i++) {
            HashMap<String, Double> TF = new LinkedHashMap<>();
            Iterator it = Term.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                TF.put(pair.getKey().toString(), 0.0);
            }
            //count freq
            for(int m = 0; m < finText.get(i).size();m++){
                if(TF.containsKey(finText.get(i).get(m))){
                    TF.put(finText.get(i).get(m),TF.get(finText.get(i).get(m))+1.0);
                }
            }
            TF_total.add(TF);

        }

    }


    public void preprocess() throws IOException{
        for (int i =1 ; i <= 2; i++) {
            String target_dir = "input/TestDataUnlabeled/";
            String str2 = Integer.toString(i);
            target_dir = target_dir + str2;
            File dir = new File(target_dir);
            File[] files = dir.listFiles();
            if (files != null) {
                for (File f : files) {
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

                        finText.add(lemmaWord);
                    }

                }

            }

        }
    }

    public void labelFolder(){
        for(int i = 0; i < 15; i++){
            label.put("C1","Airline Safety");
            label.put("C2","Amphertamine");
            label.put("C3","China and Spy Plan and Captives");
            label.put("C4","Hoof and Mouth Desease");
            label.put("C5","Iran Nuclear");
            label.put("C6","Korea and Nuclear Capability");
            label.put("C7","Mortrage Rates");
            label.put("C8","Ocean and Pollution");
            label.put("C9","Satanic Cult");
            label.put("C10","Store Irene");
            label.put("C11","Volcano");
            label.put("C12","Saddam Hussein");
            label.put("C13","Kim Jong-un");
            label.put("C14","Predictive Analytics");
            label.put("C15","Irma & Harvey");
        }
    }
}

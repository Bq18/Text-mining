package cluster; /**
 * Created by lynn on 11/3/17.
 */

import java.io.*;
import java.util.*;
import java.util.Comparator;
import java.lang.*;
public class Main {


    ArrayList<ArrayList<String>> finText = new ArrayList<ArrayList<String>>();
    ArrayList<HashMap<String,Integer>> totalTerm = new ArrayList<HashMap<String,Integer>>();
    HashMap<String,Integer> termFreq = null;
    public static void main(String[] args)throws IOException {

        Main mainClass = new Main();

        //read csv file generated from hw1 and store into arraylist
        String csvFile = "LargeMatrix1.csv";
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

        //read countersize.csv and store into array;
        Scanner scanner2 = new Scanner(new File("CounterSize.csv"));
        int[] numbers = new int[15];
        while (scanner2.hasNextLine()) {
            line = scanner2.nextLine();
            String[] counter = line.split(",");
            for (int i = 0; i < counter.length; i++) {
                numbers[i] = Integer.parseInt(counter[i]);
            }
        }

        //Choose documents in (C1,C2,C8) folders,
        // relative columns from full matrix multD[i] are selected directly
        int num = numbers[0] + numbers[1] + numbers[7];
        String[][] multD3 = new String[num][];
        //add index
        for (int i = 1; i < numbers.length; i++) {
            numbers[i] = numbers[i] + numbers[i - 1];
        }
        //c1
        int index = 0;
        for (int i = 0; i < numbers[0]; i++) {
            multD3[index] = multD[i];
            index++;
        }
        //C2
        for (int i = numbers[0]; i < numbers[1]; i++) {
            multD3[index] = multD[i];
            index++;
        }
        //C8
        for (int i = numbers[6]; i < numbers[7]; i++) {
            multD3[index] = multD[i];
            index++;
        }




        // pick 3 document of my choice
        ArrayList<Double> doc1 = new ArrayList<>();
        String label1 = multD[8][0];
        for(int i = 1; i < multD[8].length;i++){
            doc1.add(Double.parseDouble(multD[8][i]));
        }
        ArrayList<Double> doc2 = new ArrayList<>();
        String label2 = multD[20][0];
        for(int i = 1; i < multD[5].length;i++){
            doc2.add(Double.parseDouble(multD[20][i]));
        }

        ArrayList<Double> doc3 = new ArrayList<>();
        String label3 = multD[35][0];
        for(int i = 1; i < multD[35].length;i++){
            doc3.add(Double.parseDouble(multD[35][i]));
        }





        ///Apply SVD to large matrix
        SVD svd = new SVD();
        svd.SVD(multD,15);

        ///Apply SVD to small matrix
        svd.SVD(multD3,3);

        K_Similarity sim = new K_Similarity();
        //find k_similarity document, Similarity = Eucilidian_distance, k = 6
        sim.K_Similarity(6,multD,doc1,label1,"Eucilidian_distance");
        sim.K_Similarity(6,multD,doc2,label2,"Eucilidian_distance");
        sim.K_Similarity(6,multD,doc3,label3,"Eucilidian_distance");
        //find k_similarity document, Similarity = Eucilidian_distance, k = 6
        sim.K_Similarity(6,multD,doc1,label1,"Cosine_similarity");
        sim.K_Similarity(6,multD,doc2,label2,"Cosine_similarity");
        sim.K_Similarity(6,multD,doc3,label3,"Cosine_similarity");



        // Apply Kmeans / improved Kmeans on full Matrix, k = 15, Similarity = Eucilidian_distance
        Kmeans cluster = new Kmeans();
        cluster.K_means_improved(15, multD, "Eucilidian_distance","");
        cluster.K_means_improved(15, multD, "Eucilidian_distance","_Imp");




        // Apply Kmeans/improved Kmeans on full Matrix, k = 15, Similarity = Cosine_similarity
        cluster.K_means(15, multD, "Cosine_similarity","");
        cluster.K_means_improved(15, multD, "Cosine_similarity","_Imp");




        // Apply Kmeans on small Matrix, k = 3, Similarity = Eucilidian_distance
        cluster.K_means(3, multD3, "Eucilidian_distance","");
        cluster.K_means_improved(3, multD3, "Eucilidian_distance","_Imp");


        // Apply Kmeans on small Matrix, k = 3, Similarity = Cosine_similarity
        cluster.K_means(3, multD3, "Cosine_similarity","");
        cluster.K_means_improved(3, multD3, "Cosine_similarity","_Imp");



        //measure model performance of k = 15, Similarity = Eucilidian_distance
        Model_Perform perf1 = new Model_Perform();
        perf1.Model_Perform(15,"15_cluster_Euci.csv");
        perf1.eval_Sil("15_cluster_Euci.csv");
        //Improved K_Means
        perf1.Model_Perform(15,"15_cluster_Euci_Imp.csv");
        perf1.eval_Sil("15_cluster_Euci_Imp.csv");


        //measure model performance of k = 15, Similarity = Cosine_similarity
        Model_Perform perf2 = new Model_Perform();
        perf2.Model_Perform(15,"15_cluster_Cosi.csv");
        perf2.eval_Sil("15_cluster_Cosi.csv");
        //Improved K_means
        perf2.Model_Perform(15,"15_cluster_Cosi_Imp.csv");
        perf2.eval_Sil("15_cluster_Cosi_Imp.csv");

        //measure model performance of k = 3, Similarity = Eucilidian_distance
        Model_Perform perf3 = new Model_Perform();
        perf3.Model_Perform(3,"3_cluster_Euci.csv");
        perf3.eval_Sil("3_cluster_Euci.csv");
        //Improved k_means
        perf3.Model_Perform(3,"3_cluster_Euci_Imp.csv");
        perf3.eval_Sil("3_cluster_Euci_Imp.csv");



        //measure model performance of k = 3, Similarity = Cosine_similarity
        Model_Perform perf4 = new Model_Perform();
        perf4.Model_Perform(3,"3_cluster_Cosi.csv");
        perf4.eval_Sil("3_cluster_Cosi.csv");
        //Improved k_means
        perf4.Model_Perform(3,"3_cluster_Cosi_Imp.csv");
        perf4.eval_Sil("3_cluster_Cosi_Imp.csv");



        //use kmeans library on largematrix, k = 15
        Kmeans_Lib klib = new Kmeans_Lib();
        klib.kmeans("15_SVD_Matrix_5d.data",15);



        //use kmeans library on 3_documentMatirx, k = 3
        klib.kmeans("3_SVD_Matrix_5d.data",3);

    }
}






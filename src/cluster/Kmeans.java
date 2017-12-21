package cluster;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
/**
 * Created by lynn on 11/5/17.
 */
public class Kmeans {
    final double threshold = 0.001;
    final int niter = 100;

    public void  K_means(int k, String [][]word_doc, String Similariy_method,String centroid_m)throws IOException{

        ArrayList<ArrayList<Double>> oldCentroid = new ArrayList<>(k);
        //convert matrix to Arraylist<Arraylist<Double>
        ArrayList<ArrayList<Double>> Dataset = new ArrayList<>();
        for (int i = 0; i < word_doc.length;i++){
            ArrayList<Double> array = new ArrayList<>();
            for (int j = 1; j < word_doc[0].length;j++) {
                array.add(Double.parseDouble(word_doc[i][j]));
            }
            Dataset.add(array);
        }
        //centroid initialization
        ArrayList<ArrayList<Double>> centroid = initialCentroid(k,Dataset);
        //calculate distance of the data to centroid
        ArrayList<ArrayList<Double>> disMatrix = Distance(Dataset,centroid,k,Similariy_method);
        //get index of shortest distance from arraylist
        ArrayList<Integer> pointIndex = getNearstPointIndex(disMatrix,k,Similariy_method);
        int round = 0;
        while(true){
                oldCentroid = centroid;
                centroid = newCentroid(Dataset,pointIndex,k);
                disMatrix = Distance(Dataset,centroid,k,Similariy_method);
                pointIndex = getNearstPointIndex(disMatrix,k,Similariy_method);
                round++;
                if( round >= niter || converge(oldCentroid,centroid,threshold )){
                    break;
                }
        }
        //System.out.println(" pointIndexFinal" + pointIndex);
        writeToCSV (k,word_doc,pointIndex,Similariy_method,centroid_m);
    }

    public void  K_means_improved(int k, String [][]word_doc, String Similariy_method,String centroid_m)throws IOException{

        ArrayList<ArrayList<Double>> oldCentroid = new ArrayList<>(k);
        //convert matrix to Arraylist<Arraylist<Double>
        ArrayList<ArrayList<Double>> Dataset = new ArrayList<>();
        for (int i = 0; i < word_doc.length;i++){
            ArrayList<Double> array = new ArrayList<>();
            for (int j = 1; j < word_doc[0].length;j++) {
                array.add(Double.parseDouble(word_doc[i][j]));
            }
            Dataset.add(array);
        }
        //centroid initialization
        ArrayList<ArrayList<Double>> centroid = initialCentroid_improved(k,Dataset);
        //calculate distance of the data to centroid
        ArrayList<ArrayList<Double>> disMatrix = Distance(Dataset,centroid,k,Similariy_method);
        //get index of shortest distance from arraylist
        ArrayList<Integer> pointIndex = getNearstPointIndex(disMatrix,k,Similariy_method);
        int round = 0;
        while(true){
            oldCentroid = centroid;
            centroid = newCentroid(Dataset,pointIndex,k);
            disMatrix = Distance(Dataset,centroid,k,Similariy_method);
            pointIndex = getNearstPointIndex(disMatrix,k,Similariy_method);
            round++;
            if( round >= niter || converge(oldCentroid,centroid,threshold )){
                break;
            }
        }
        //System.out.println(" pointIndexFinal" + pointIndex);
        writeToCSV (k,word_doc,pointIndex,Similariy_method,centroid_m);
    }

    public void writeToCSV (int k,String [][]multD,ArrayList<Integer> pointIndex ,String Similariy_method, String centroid_m) throws FileNotFoundException {

        //print result into csv file
        String filename_1 = k + "_cluster_" + Similariy_method.substring(0,4) + centroid_m+ ".csv";
        String filename_2 = k + "_clusterSize_" + Similariy_method.substring(0,4)+centroid_m+ ".csv";

        ArrayList<ArrayList<String>> cluster_1 = new ArrayList<ArrayList<String>>();
        for (int loop = 0; loop < k; loop++) {
            cluster_1.add(new ArrayList<String>());
        }


        for (int j = 0; j < k; j++) {
            ArrayList<String> temp = new ArrayList<>();

            for (int i = 0; i < pointIndex.size(); i++) {

                String str = "";
                if (pointIndex.get(i).equals(j)) {

                    for (int m = 0; m < multD[i].length; m++) {

                        str = str + multD[i][m];
                        if (m != multD[i].length - 1) {
                            str = str + ",";
                        }
                    }
                    temp.add(str);
                }
            }
            cluster_1.set(j, temp);
        }


        //write to CSV
        PrintWriter pw = new PrintWriter(new File(filename_1));
        PrintWriter pw2 = new PrintWriter(new File(filename_2));
        for (int i = 0; i < cluster_1.size(); i++) {
            for (int j = 0; j < cluster_1.get(i).size(); j++){
                pw.append("cluster_" + i +",");
                pw.append(cluster_1.get(i).get(j));
                pw.append(System.getProperty("line.separator"));
            }
            //pw.append(System.getProperty("line.separator"));
            pw2.append(Integer.toString(cluster_1.get(i).size()));
            if(i != cluster_1.size()-1){
                pw2.append(",");
            }
        }
        pw.close();
        pw2.close();
    }

    public boolean converge(ArrayList<ArrayList<Double>>C1, ArrayList<ArrayList<Double>>C2,double threshold){
        double maxV =  0.0;
        for(int i = 0; i < C1.size();i++){
            double d =  Eucil_Dis(C1.get(i), C2.get(i));
            if(maxV < d){
                maxV = d;
            }
        }
        if(maxV < threshold){
            return true;
        }else {
            return false;
        }
    }

    public ArrayList<ArrayList<Double>> initialCentroid(int k,ArrayList<ArrayList<Double>> Dataset){
        ArrayList<ArrayList<Double>> centroids = new ArrayList<>(k);
        for (int i = 0; i < k; i++){
            Random rn = new Random();
            int index = rn.nextInt(Dataset.size());
            centroids.add(Dataset.get(index));
        }
        return centroids;
    }

    //improved algorithm reference: https://link.springer.com/article/10.1007/s40031-014-0106-z
    public ArrayList<ArrayList<Double>> initialCentroid_improved(int k,ArrayList<ArrayList<Double>> Dataset){
        ArrayList<ArrayList<Double>> centroids = new ArrayList<>(k);
        //Initial Orig with value 0 in n dimensions
        ArrayList<Double> Orig = new ArrayList<>(k);
        for(int i =0; i < Dataset.get(0).size();i++){
            Orig.add(0.0);
        }
        //calcuate distance of each point in dataset to Orig
        ArrayList<Element> dist = new ArrayList<>(k);
        for (int i = 0; i < Dataset.size();i++) {
            dist.add(new Element(i,Eucil_Dis(Dataset.get(i),Orig)));
        }


        //sort distance increasingly
        dist.sort(new InComprator());
        int index = 0;
        int partition = Dataset.size()/k;
        while (index < partition*k){
            int j = 0;
            ArrayList<Double> temp = new ArrayList<>();
            for(int m = 0; m < Dataset.get(0).size();m++) {
                double sum = 0;
                for (j = 0; j < partition; j++) {

                    int id = dist.get(index+j).index;
                    sum += Dataset.get(id).get(m);
                }
                temp.add(sum/partition);
            }
            centroids.add(temp);
            index += j;
        }
        return centroids;
    }

    public ArrayList<ArrayList<Double>> newCentroid(ArrayList<ArrayList<Double>> Dataset,ArrayList<Integer> pointIndex,int k){
        ArrayList<ArrayList<Double>> centroids = new ArrayList<>(k);
        for(int i = 0; i < k;i++) {
            centroids.add(new ArrayList<Double>());
        }
        ArrayList<ArrayList<Integer>> cluster = new ArrayList<>(k);
        for(int i = 0; i < k;i++) {
            cluster.add(new ArrayList<Integer>());
        }
        //System.out.println(pointIndex);
        for (int i = 0; i < cluster.size();i++){
            for(int j = 0; j <pointIndex.size();j++){
                if (pointIndex.get(j).equals(i)){
                    cluster.get(i).add(j);
                }
            }
        }

        for (int i = 0; i < cluster.size();i++){

            if(cluster.get(i).size() == 0){
                Random rn = new Random();
                int index = rn.nextInt(Dataset.size());
                centroids.set(i,Dataset.get(index));
            } else {
                ArrayList<Double> listValue = new ArrayList<Double>();
                for (int m = 0; m < Dataset.get(0).size(); m++) {
                    double sum = 0.0;
                    for (int j = 0; j < cluster.get(i).size(); j++) {
                        //System.out.println("i " + i + "   j  " +j + "   m "   + m );
                        sum += Dataset.get(cluster.get(i).get(j)).get(m);
                    }
                    listValue.add(sum / (cluster.get(i).size()));
                }
                centroids.set(i, listValue);
            }
        }
      return centroids;
    }


    public ArrayList<Integer> getNearstPointIndex(ArrayList<ArrayList<Double>> DisMatrix, int k,String Similariy_method){
        ArrayList<Integer> pointIndex = new ArrayList<Integer>();
        for (int i = 0; i < DisMatrix.size();i++){
            int index = -1;
            if(Similariy_method == "Eucilidian_distance") {
                double min = Double.MAX_VALUE;
                for (int j = 0; j < DisMatrix.get(i).size(); j++) {
                    if (DisMatrix.get(i).get(j) < min) {
                        min = DisMatrix.get(i).get(j);
                        index = j;
                    }
                }
            } else if(Similariy_method == "Cosine_similarity"){
                double max = Double.MIN_VALUE;
                for (int j = 0; j < DisMatrix.get(i).size(); j++) {
                    if (DisMatrix.get(i).get(j) > max) {
                        max = DisMatrix.get(i).get(j);
                        index = j;
                    }
                }
            }
                pointIndex.add(index);
        }
        return pointIndex;
    }

    public ArrayList<ArrayList<Double>> Distance(ArrayList<ArrayList<Double>> Dataset, ArrayList<ArrayList<Double>> centroids, int k,String Similariy_method){
        ArrayList<ArrayList<Double>> DisMatrix = new ArrayList<>();
        for(int i = 0; i < Dataset.size();i++) {
            DisMatrix.add(new ArrayList<Double>());
        }


        for (int i = 0; i < Dataset.size();i++){
            for (int j = 0; j < centroids.size();j++){
                    if (Similariy_method == "Eucilidian_distance") {
                        //System.out.println("i " + i);
                        //System.out.println("j " + j);
                        //System.out.print("Dataset" + Dataset.get(i));
                        //System.out.println("Centroid" + centroids.get(j));
                        double distance = Eucil_Dis(Dataset.get(i), centroids.get(j));
                        //System.out.println(distance);
                        DisMatrix.get(i).add(distance);

                    } else if (Similariy_method == "Cosine_similarity") {
                        DisMatrix.get(i).add(cosineSimilary(Dataset.get(i), centroids.get(j)));
                    }
            }
        }
        return DisMatrix;
    }

    public double cosineSimilary(ArrayList<Double> arrayA, ArrayList<Double> arrayB) {
        double dotproduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < arrayA.size(); i++){
            dotproduct = dotproduct + arrayA.get(i)* arrayB.get(i);
            normA += Math.pow(arrayA.get(i),2);
            normB += Math.pow(arrayB.get(i),2);
        }
        return dotproduct/(Math.sqrt(normA)*Math.sqrt(normB));
    }

    public double Eucil_Dis(ArrayList<Double> arrayA, ArrayList<Double> arrayB ){
        double sum = 0.0;
        for (int i = 0 ; i < arrayA.size();i++){
            sum = sum + Math.pow(arrayA.get(i)- arrayB.get(i),2);
        }
        return Math.sqrt(sum);
    }
}

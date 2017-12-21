package cluster;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Created by lynn on 11/7/17.
 */
public class Model_Perform {

    public void Model_Perform(int k, String filename) throws IOException {
        //read csv file generated from hw1 and store into arraylist
        ArrayList<ArrayList<String>> lineArray = new ArrayList<>();
        String csvFile = filename;

        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(csvFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = "";
        while (scanner.hasNextLine()) {
            ArrayList<String> temp = new ArrayList<>();
            line = scanner.nextLine();
            String[] array = line.split(",");
            temp.add(array[0]);
            String[] split = array[1].split("_");

            temp.add(split[0]);
            lineArray.add(temp);
        }

        String[][] ConfusionMatrix = new String[k+1][k+1];
        ConfusionMatrix[0][0] = filename.substring(0,filename.length()-4);
        for(int i = 1; i < ConfusionMatrix.length;i++){
            ConfusionMatrix[i][0] = "cluster_" + (i-1);
        }
        //Initialize Confusion Matrix
        for(int j = 1; j <ConfusionMatrix[0].length;j++){
            if(k == 3 && j == 3){
                //since the folder we choose is C1,C2,C8
                ConfusionMatrix[0][3] = "C" + 8;
            }else {
                ConfusionMatrix[0][j] = "C" + j;
            }
        }
        for(int i = 1; i< ConfusionMatrix.length;i++){
            for(int j = 1; j <ConfusionMatrix[i].length;j++){
                ConfusionMatrix[i][j] = String.valueOf(0);
            }
        }

        //Calcuate value
        for (int m = 0; m < ConfusionMatrix.length;m++){
            for(int n = 0; n < ConfusionMatrix[m].length;n++){
                for(int f = 0; f <lineArray.size();f++){
                    if(lineArray.get(f).get(0).equals(ConfusionMatrix[m][0]) && lineArray.get(f).get(1).equals(ConfusionMatrix[0][n])){
                        ConfusionMatrix[m][n] = Integer.toString((Integer.parseInt(ConfusionMatrix[m][n]) + 1));
                    }
                }
            }
        }
        System.out.println("Confusion_Matrix of "+  filename.substring(0,filename.length()-4));
        for(int i = 0; i < ConfusionMatrix.length;i++){
            for(int j = 0; j < ConfusionMatrix[i].length;j++){
                System.out.printf("%20s", ConfusionMatrix[i][j]);
            }
            System.out.println();
        }
    }

    //Evaluation with Silhouette
    public void eval_Sil(String filename){
        ArrayList<ArrayList<String>> cluster = new ArrayList<>();

        //read csvFile into cluster arraylist
        String csvFile = filename;
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(csvFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = "";
        while (scanner.hasNextLine()) {
            ArrayList<String> temp = new ArrayList<>();
            line = scanner.nextLine();
            String[] array = line.split(",");
            for (int i = 0; i < array.length; i++){
                if(i !=1) {
                    temp.add(array[i]);
                }else{
                    String[] split = array[1].split("_");
                    temp.add(split[0]);
                }
            }
            cluster.add(temp);
        }



        Map<String,Double> Sil_index = new HashMap<String,Double>();
        HashMap<String,Double> hm = new HashMap<String,Double>();
        HashMap<String,Integer> hmSize = new HashMap<String,Integer>();

        //calculate Silhouette index for each points
        for(int i = 0; i < cluster.size();i++){
            //avg_dis in the same cluster to i
            double a_i = Avg_ai(cluster,i);
            double b_i = Avg_bi(cluster,i);
            double s_i = (a_i - b_i)/Math.max(a_i,b_i);

            if(!hm.containsKey(cluster.get(i).get(0))){
                hm.put(cluster.get(i).get(0),s_i);
                hmSize.put(cluster.get(i).get(0),1);
            }else{
                hm.put(cluster.get(i).get(0),hm.get(cluster.get(i).get(0))+ s_i);
                hmSize.put(cluster.get(i).get(0),hmSize.get(cluster.get(i).get(0))+1);
            }
        }

        Iterator it = hm.entrySet().iterator();
        //String cluster_d = "";
        double value = 0.0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            int counter = hmSize.get(pair.getKey());
            if(counter != 0) {
                value = (Double) pair.getValue() / counter;
                    //cluster_d = pair.getKey().toString();
                Sil_index.put(pair.getKey().toString(),value);
            }
            it.remove(); // avoids a ConcurrentModificationException
        }

        ValueComparator vc =  new ValueComparator(Sil_index);
        TreeMap<String, Double> sortedMap = new TreeMap<String, Double>(vc);
        sortedMap.putAll(Sil_index);

        System.out.println();
        System.out.println(" Silhouette_Index_Evaluation:");
        System.out.println(sortedMap);
        System.out.println();
        System.out.println();
    }


    public double Avg_bi(ArrayList<ArrayList<String>> cluster, int i){
        HashMap<String,Double> hm = new HashMap<String,Double>();
        HashMap<String,Integer> hmSize = new HashMap<String,Integer>();
        ArrayList<Double> Datum = DouArray(cluster.get(i));
        for(int m = 0; m <cluster.size();m++){

            //Sum_dis in different cluster
            if (m != i && !cluster.get(m).get(0).equals(cluster.get(i).get(0))){
                ArrayList<Double> Datum2 = DouArray(cluster.get(m));
                double value = Eucil_Dis(Datum,Datum2);
                if(!hm.containsKey(cluster.get(m).get(0))){
                    hm.put(cluster.get(m).get(0),value);
                    hmSize.put(cluster.get(m).get(0),1);
                }else{
                    hm.put(cluster.get(m).get(0),hm.get(cluster.get(m).get(0))+ value);
                    hmSize.put(cluster.get(m).get(0),hmSize.get(cluster.get(m).get(0))+1);
                }
            }
        }

        //caluclate avg of each cluster
        Iterator it = hm.entrySet().iterator();
        double min = Double.MAX_VALUE;
        //String cluster_d = "";
        double value = 0.0;

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            int counter = hmSize.get(pair.getKey());
            if(counter != 0) {
                value = (Double) pair.getValue() / counter;
                if (value < min){
                    min = value;
                    //cluster_d = pair.getKey().toString();
                }
            }
            it.remove(); // avoids a ConcurrentModificationException
        }
    return min;
    }


    public double Avg_ai(ArrayList<ArrayList<String>> cluster, int m){
        ArrayList<Double> Datum = DouArray(cluster.get(m));
        double sum = 0.0;
        int index = 0;
        for(int n = 0; n <cluster.size();n++) {
            if (m != n && cluster.get(m).get(0).equals(cluster.get(n).get(0))) {
                ArrayList<Double> Datum2 = DouArray(cluster.get(n));
                sum += Eucil_Dis(Datum, Datum2);
                index++;
            }
        }
        if (index == 0){
            return -1;
        }
        return sum/index;
    }


    public ArrayList<Double> DouArray(ArrayList<String> StrArray){
        ArrayList<Double> DouArray = new ArrayList<Double>();
        for(int i = 2; i < StrArray.size();i++){
            DouArray.add(Double.parseDouble(StrArray.get(i)));
        }
        return DouArray;
    }

    public double Eucil_Dis(ArrayList<Double> arrayA, ArrayList<Double> arrayB ){
        double sum = 0.0;
        for (int i = 0 ; i < arrayA.size();i++){
            sum = sum + Math.pow(arrayA.get(i)- arrayB.get(i),2);
        }
        return Math.sqrt(sum);
    }

}

class ValueComparator implements Comparator<String> {

    Map<String, Double> map;

    public ValueComparator(Map<String, Double> base) {
        this.map = base;
    }

    @Override
    public int compare(String a, String b) {
        // TODO Auto-generated method stub
        if (map.get(a) >= map.get(b)) {
            return -1;
        } else {
            return 1;
        }
    }
}


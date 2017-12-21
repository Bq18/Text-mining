package Classification;

/**
 * Created by lynn on 12/4/17.
 */
import java.util.*;
import java.io.*;

//measure model performance
public class Performance {
    /* Purpose: To measure the performance of KNN model. 15 * 15 confusion matrix was drawed based on the results from
    *           KNN modeling on the whole dataset. The accuracy, average precision/recall of 15 classes were calculated.
    *  Arguments: the gathering labels and corresponding orginal labels for all dataset, label(topics) of the
    *             dataset, the number of nearest neighbour.
    *   return: None. (write result into ouput file)
    * */
    public void Performance(ArrayList<HashMap<String,String>> total_label,
                            HashMap<String,String>label,int K) throws IOException {

        HashMap<String,String> pred_label = new HashMap<>();
        //Transform ArrayList<HashMap> to HashMap
        for (int i = 0; i < total_label.size(); i++){
            HashMap<String,String> temp = new HashMap<>();
            temp = total_label.get(i);
            Iterator it = temp.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                pred_label.put(pair.getKey().toString(),pair.getValue().toString());
            }
        }

        int k = label.size();
        String[][] ConfusionMatrix = new String[k+1][k+1];
        ConfusionMatrix[0][0] = "k = " + K;
        //Initialize Confusion Matrix first row
        for(int i = 1; i < ConfusionMatrix.length;i++){
            String folderName = "C" + i;
            ConfusionMatrix[i][0] = "Pred_" + label.get(folderName);
        }
        //Initialize Confusion Matrix first column
        for(int j = 1; j <ConfusionMatrix[0].length;j++){
            String folderName = "C" + j;
            ConfusionMatrix[0][j] = label.get(folderName);
        }
        //Intialize inner value
        for(int i = 1; i< ConfusionMatrix.length;i++){
            for(int j = 1; j <ConfusionMatrix[i].length;j++){
                ConfusionMatrix[i][j] = String.valueOf(0);
            }
        }

        //Calcuate value
        for (int m = 1; m < ConfusionMatrix.length;m++){
            for(int n = 1; n < ConfusionMatrix[m].length;n++){
                Iterator it = pred_label.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    String line = pair.getKey().toString();
                    String[] split = line.split("_");
                    String key= label.get(split[0]);
                    String value = "Pred_" + pair.getValue().toString();
                    if(value.equals(ConfusionMatrix[m][0]) && key.equals(ConfusionMatrix[0][n])){
                        //System.out.println("innnnner" + ConfusionMatrix[m][0]);
                        ConfusionMatrix[m][n] = Integer.toString((Integer.parseInt(ConfusionMatrix[m][n]) + 1));
                    }
                    //it.remove(); // avoids a ConcurrentModificationException
                }
            }
        }
        double accuracy = cal_accuracy(ConfusionMatrix, pred_label);
        double avg_recall = cal_recall(ConfusionMatrix, pred_label);
        double avg_preci = cal_preci(ConfusionMatrix, pred_label);

        //write to csv
        String filename ="output/" + K + "_ConfusionMatrix.csv";
        PrintWriter pw = new PrintWriter(new File(filename));
        for(int i = 0; i < ConfusionMatrix.length;i++) {
            for (int j = 0; j < ConfusionMatrix[i].length; j++) {
                pw.append(ConfusionMatrix[i][j]);
                pw.append(',');
            }
            pw.append('\n');
        }

        pw.append('\n');
        pw.append("Accuracy For Confusion Matrix:");
        pw.append(String.valueOf(accuracy));


        pw.append('\n');
        pw.append("Average Recall For Confusion Matrix:");
        pw.append(String.valueOf(avg_recall));


        pw.append('\n');
        pw.append("Average Precision For Confusion Matrix:");
        pw.append(String.valueOf(avg_preci));
        pw.close();


        //print
        /*System.out.println("Confusion_Matrix");
        for(int i = 0; i < ConfusionMatrix.length;i++) {
            for (int j = 0; j < ConfusionMatrix[i].length; j++) {
                System.out.printf("%38s", ConfusionMatrix[i][j]);
            }
            System.out.println();
        }*/
    }

    public double cal_accuracy(String[][] ConfusionMatrix,HashMap<String,String> pred_label){
        int sum = 0;
        for(int i = 1; i < ConfusionMatrix.length;i++){
            for(int j = 1; j <ConfusionMatrix[0].length;j++){
                if(i == j){
                    sum = sum + Integer.parseInt(ConfusionMatrix[i][j]);
                }
            }
        }

        return (double) sum/pred_label.size();
    }

    public double cal_recall(String[][] ConfusionMatrix,HashMap<String,String> pred_label){
        int tp = 0;
        double recall = 0;
        for(int j = 1; j < ConfusionMatrix[0].length; j++){
            int sum = 0;
            for(int i = 1; i < ConfusionMatrix.length;i++){
                if(i == j) {
                    tp = Integer.parseInt(ConfusionMatrix[i][j]);
                }
                sum = sum + Integer.parseInt(ConfusionMatrix[i][j]);
            }
            recall = recall + (double)tp/sum;
        }
        return (double)recall/(ConfusionMatrix[0].length);
    }

    public double cal_preci(String[][] ConfusionMatrix,HashMap<String,String> pred_label){
        double preci = 0;
        int tp = 0;
        for(int i = 1; i < ConfusionMatrix.length; i++) {
            int sum = 0;
            for (int j = 1; j < ConfusionMatrix[i].length; j++) {
                if (i == j) {
                    tp = Integer.parseInt(ConfusionMatrix[i][j]);
                }
                sum = sum + Integer.parseInt(ConfusionMatrix[i][j]);

            }
            if(sum!= 0) {
                preci = preci + (double) tp / sum;
            }
        }
        //System.out.println("preci" + preci);
        return (double)preci/(ConfusionMatrix.length);
    }
}

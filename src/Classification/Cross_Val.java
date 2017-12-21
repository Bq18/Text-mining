package Classification;

/**
 * Created by lynn on 12/3/17.
 */


import java.util.*;
import java.io.*;

//Ten folds Cross Validation class
public class Cross_Val {

    /* Purpose: To do ten folds cross validation. The original dataset is randomly splitted into 10 nearly equal
    *           size sub samples. Each time we will choose one unchoosen sample as the test, the rest nine
    *           samples as train, then call KNN method and calcuate CV error. The process will repeat for 10 times
    *           and calculate the average CV error. After 10 iterations, call performance method to calculate overall
    *           accuracy and draw confusion matrix based on predict labels in the whole dataset.
    * Arguments: the original dataset, the number of folds cross validation, the nearest neighbours, label of each file
    * Return: return average cross validation error.
    * */
    public double Cross_Val(String[][] mult, int folds,int k,HashMap<String,String> label) throws IOException{

        ArrayList<ArrayList<String[]>> cross_val = new ArrayList<>();
        int fold_size = mult.length / folds;

        int Min = 0;
        int Max = mult.length;
        int index;

        //String COPY
        String[][] copy = new String[Max][mult[0].length];
        for(int i = 0; i < mult.length;i++){
            for(int j = 0; j < mult[i].length;j++){
                copy[i][j] = mult[i][j];
            }
        }

        //split data into n_fold
        for (int i = 0; i < folds; i++) {
            ArrayList<String[]> temp = new ArrayList();
            while (temp.size() < fold_size) {
                boolean loop = true;
                while (loop) {
                    index = Min + (int) (Math.random() * ((Max - Min)));

                    String str = copy[index][0];
                    if (str != "null") {
                        temp.add(mult[index]);
                        loop = false;
                        copy[index][0] = "null";
                    }
                }
            }
            cross_val.add(temp);
        }
        //deal with remaining docs
        int remain_doc = mult.length - fold_size * folds;
        for (int i = 0; i < remain_doc; i++) {
            boolean loop = true;
            while (loop) {
                index = Min + (int) (Math.random() * ((Max - Min)));
                String str2 = copy[index][0];
                if (str2 != "null") {
                    cross_val.get(i).add(mult[index]);
                    loop = false;
                    copy[index][0] = "null";
                }
            }
        }

        //test cross validate
        ArrayList<HashMap<String,String>> total_test = new ArrayList<>();
        for (int i = 0; i < cross_val.size();i++){
            ArrayList<String[]> train = new ArrayList<> ();
            ArrayList<String[]> test = cross_val.get(i);
            HashMap<String,String> testLabel= new HashMap<>();
            train = sep_train_test(cross_val,i);
            //System.out.println("testsize:" + test.size());
            for(int j = 0; j < test.size();j++ ) {
                String name = test.get(j)[0];
                KNN knn = new KNN();
                String labelname = knn.KNN(name,train,test,label,k);
                testLabel.put(name,labelname);
            }
            total_test.add(testLabel);
            //System.out.println("size: " + total_test.get(i).size() + "test:" + total_test);
        }
        Performance perf = new Performance();
        perf.Performance(total_test, label,k);
        return val_error(total_test,label);
    }

    public double val_error(ArrayList<HashMap<String,String>> testLabel,HashMap<String,String> label){
        double sum = 0;
        for (int i = 0; i < testLabel.size();i++){
            int count = 0;
            HashMap<String,String> compLabel = new HashMap<>();
            compLabel = testLabel.get(i);
            int size = compLabel.size();
            Iterator it = compLabel.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                String docName = pair.getKey().toString();
                String[] split = docName.split("_");
                if (label.get(split[0]) == pair.getValue()){
                    count++;
                }
               // it.remove(); // avoids a ConcurrentModificationException
            }
           // System.out.println("testlabel:" + compLabel);
            //System.out.println("count:" + count);
            //System.out.println("size:" + size);

            double error = 1- (double)count/size;
            //System.out.println("error:" + error);
            sum = sum + error;
        }
        double cross_error = sum/testLabel.size();
        return cross_error;
    }

    public ArrayList<String[]> sep_train_test(ArrayList<ArrayList<String[]>>dataset,int index){
        ArrayList<String[]> train = new ArrayList<>();
        for(int i = 0; i < dataset.size();i++){
            if(i!=index){
                for(int j = 0; j < dataset.get(i).size();j++) {
                    train.add(dataset.get(i).get(j));
                }
            }
        }
        return train;
    }
}
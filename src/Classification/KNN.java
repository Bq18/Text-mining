package Classification;
import java.util.*;
import java.io.*;
/**
 * Created by lynn on 11/24/17.
 */
    //KNN class
    public class KNN {

        ArrayList<Double> doc = new ArrayList<Double>();
        HashMap<String,String> label = new HashMap<>();
        String topic = "";

        /* Purpose: K-nearest neighber implementation. Using consine_distance as similarty measure, find k most similar
         *          documents from training dataset to the test document. Return the label of the test document by
         *          majority vote of k most similar documents'label.
         * Arguments: Test document filename, TF-IDF vectors of train set, TF-IDF vectors of test set, label of each
         *            file, number of neighbours considered.
         * Return: Label(topic) of the test document
         */
        public String KNN(String filename,ArrayList<String[]> train,ArrayList<String[]> test,
                          HashMap<String,String> label, int k){

            //path
            ConvertDtoV(test, filename);
            ArrayList<Double> Cosi = new ArrayList<Double>();
            for(int i = 0; i < train.size();i++){
                ArrayList<Double> lineArray = new ArrayList<Double>();
                for(int j = 1; j < train.get(i).length;j++) {
                    lineArray.add(Double.parseDouble(train.get(i)[j]));
                }
                Cosi.add(cosineSimilary(lineArray, doc));//larger better
            }
            ArrayList<Element> desIndex = new ArrayList<Element>();
            for (int i = 0; i < Cosi.size(); i++) {
                desIndex.add(new Element(i, Cosi.get(i)));
            }
            for (int i = 0; i < desIndex.size();i++){
                //System.out.println("des" + desIndex.get(i).value);
            }

            desIndex.sort(new DesComprator());

            getMajority(desIndex,train,label,k);

            return topic;
        }

        /* Purpose: get label of test doc by majority voting of k most similar nearest neighbours
        *  Arguments: the index list of the trainset ordered decreasingly by similarilty to the test doc,
        *             original trainset, label of each file, the number of nearest neighbour.
        *  Return: None
        * */
        public void getMajority(ArrayList<Element> desIndex, ArrayList<String[]> mult, HashMap<String,String>label,int k){
            //create Hashmap of label_count
            HashMap<String, Integer> count = new LinkedHashMap<>();
            for(int i = 1; i <= 15; i++){
                String key = "C" + i;
                count.put(key,0);
            }

            //count freq of each label
            for (int i = 0; i < k;i++){
                String str = mult.get(desIndex.get(i).index)[0];
                String[] split = str.split("_");
                count.put(split[0],count.get(split[0])+ 1);
            }

            //get key with max freq and transform key to label
            int value = Collections.max(count.values());
            for (Map.Entry<String, Integer> entry : count.entrySet()) {  // Iterate through hashmap
                if (entry.getValue()== value) {
                    topic = label.get(entry.getKey());
                    break;
                }
            }
        }


        public void ConvertDtoV(ArrayList<String[]>test, String name){
            for(int i = 0; i < test.size();i++){
                if(name == test.get(i)[0]){
                    for(int j = 1; j < test.get(i).length;j++) {
                        doc.add(Double.parseDouble(test.get(i)[j]));
                    }
                }
            }
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

    }


    class DesComprator implements Comparator<Element>{
        @Override
        public int compare(Element a, Element b){
            if (b.value - a.value < 0){
                return -1;
            } else if(b.value - a.value == 0){
                return 0;
            } else{
                return 1;
            }
        }
    }

    class Element{
        int index;
        double value;
        public Element(int index,double value){
            this.value = value;
            this.index = index;
        }
    }


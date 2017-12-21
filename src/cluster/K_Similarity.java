package cluster;
import java.util.*;

/**
 * Created by lynn on 11/13/17.
 */
public class K_Similarity {


    public void K_Similarity(int k,String mult[][],ArrayList <Double> doc,String label,String Similariy_method){
        ArrayList<Double> Eucli = new ArrayList<Double>();
        ArrayList<Double> Cosi = new ArrayList<Double>();
        for(int i = 0;i < mult.length;i++){
            ArrayList<Double> lineArray = new ArrayList<Double>();
            for(int j = 1; j < mult[i].length;j++){
                lineArray.add(Double.parseDouble(mult[i][j]));
            }
            if (Similariy_method == "Eucilidian_distance") {
                Eucli.add(Eucil_Dis(lineArray, doc));//smaller better
            } else if (Similariy_method == "Cosine_similarity"){
                Cosi.add(cosineSimilary(lineArray, doc));//larger better
            }
        }
        ArrayList<Element> incIndex = new ArrayList<Element>();
        if (Similariy_method == "Eucilidian_distance") {
            for (int i = 0; i < Eucli.size(); i++) {
                incIndex.add(new Element(i, Eucli.get(i)));
            }
            incIndex.sort(new InComprator());

            System.out.println("With Eucilidian_distance, K = " + k + " most similar doc to " + label + " is");
            for(int i = 0; i < k;i++) {
                //System.out.println(incIndex.get(i).index);
                 System.out.printf("%20s",mult[incIndex.get(i).index][0]);
            }
            System.out.println();
        }

        ArrayList<Element> desIndex = new ArrayList<Element>();
        if (Similariy_method == "Cosine_similarity") {
            for (int i = 0; i < Cosi.size(); i++) {
                desIndex.add(new Element(i, Cosi.get(i)));
            }
            desIndex.sort(new DesComprator());
            System.out.println("With Cosine_similarity,K = " + k+  " most similar doc to " + label + " is");
            for(int i = 0; i < k;i++) {
                System.out.printf("%20s",mult[desIndex.get(i).index][0]);
            }
            System.out.println();
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

    public double Eucil_Dis(ArrayList<Double> arrayA, ArrayList<Double> arrayB ){
        double sum = 0.0;
        for (int i = 0 ; i < arrayA.size();i++){
            sum = sum + Math.pow(arrayA.get(i)- arrayB.get(i),2);
        }
        return Math.sqrt(sum);
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

class InComprator implements Comparator<Element>{
    @Override
    public int compare(Element a, Element b){
        if (b.value - a.value > 0){
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

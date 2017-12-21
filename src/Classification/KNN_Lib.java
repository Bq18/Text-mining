package Classification;

/**
 * Created by lynn on 12/6/17.
 */

import java.io.IOException;
import java.util.Random;
import java.io.File;
import java.util.Map;

import net.sf.javaml.classification.Classifier;
import net.sf.javaml.classification.KNearestNeighbors;
import net.sf.javaml.classification.evaluation.CrossValidation;
import net.sf.javaml.classification.evaluation.PerformanceMeasure;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.tools.data.FileHandler;

public class KNN_Lib {
    /* Purpose: call cross validation KNN method from JavaML library and write code to print average cross
     *           validation error.
     * Arguments: the path of input dataset, the number of nearest neighbour
     * Return: Average Cross Validation Error
     */
    public double knn_lib(String path,int k) throws IOException {

        Dataset data = FileHandler.loadDataset(new File(path), 0, ",");
    /* Construct KNN classifier */
        Classifier knn = new KNearestNeighbors(k);
    /* Construct new cross validation instance with the KNN classifier, */
        CrossValidation cv = new CrossValidation(knn);
    /* 105-fold CV with fixed random generator */
        Map<Object, PerformanceMeasure> r = cv.crossValidation(data, 10, new Random(25));
        System.out.println("KNN_Library: K-Error");
        System.out.println("K =" + k);
        double sum = 0;
        for(int i = 1; i <=15;i++) {
            String name = "C" + i;
            sum = sum + (1- r.get(name).getAccuracy());
        }
        double err = sum/15;
        System.out.println("error " + err);
       // System.out.println(r);
        return err;

    }
}

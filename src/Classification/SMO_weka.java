package Classification;

/**
 * Created by lynn on 12/11/17.
 */

import java.io.File;
import java.util.Map;
import java.util.Random;
import weka.classifiers.functions.SMO;
import weka.core.*;
import java.io.BufferedReader;
import java.io.FileReader;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.OptionHandler;
import weka.core.Utils;
import weka.filters.Filter;
import weka.core.converters.CSVLoader;
import weka.core.converters.ArffSaver;


    /**
     * Tutorial how to use a Weka classifier in Java-ML.
     *
     * @author Thomas Abeel
     *
     */
    public class SMO_weka {

        /**
         * This method performs classification of unseen instance.
         * It starts by training a model using a selection of classifiers then classifiy new unlabled instances.
         */

        public static void predict() throws Exception {
            //start by providing the paths for your training and testing ARFF files make sure both files have the same structure and the exact classes in the header

            //initialise classifier
            Classifier classifier = null;

            System.out.println("read training data");
            CSVLoader loader = new CSVLoader();
            loader.setSource(new File("input/matrix_tfidf_header.csv"));
            Instances data = loader.getDataSet();
            // save ARFF
            ArffSaver saver = new ArffSaver();
            saver.setInstances(data);
            saver.setFile(new File("output/Matrix.arff"));
            saver.setDestination(new File("output/Matrix.arff"));
            saver.writeBatch();
            Instances train = new Instances(new BufferedReader(new FileReader("output/Matrix.arff")));
            train.setClassIndex(0);//in my case the class was the first attribute thus zero otherwise it's the number of attributes -1

            System.out.println("read testing data");
            CSVLoader loader2 = new CSVLoader();
            loader2.setSource(new File("output/Document_TFIDF.csv"));
            Instances unlabeled = loader2.getDataSet();
            ArffSaver saver2 = new ArffSaver();
            saver2.setInstances(unlabeled);
            saver2.setFile(new File("output/TF_IDF_s.arff"));
            saver2.setDestination(new File("output/TF_IDF_s.arff"));
            saver2.writeBatch();
            Instances test = new Instances(new BufferedReader(new FileReader("output/TF_IDF_s.arff")));
            test.setClassIndex(0);

            // training using a collection of classifiers (NaiveBayes, SMO (AKA SVM), KNN and Decision trees.)
            Classifier smo = new SMO();
            Evaluation eval = new Evaluation(train);
            //perform 10 fold cross validation
            eval.crossValidateModel(classifier, train, 10, new Random(1));
            eval.evaluateModel(smo, unlabeled);
            System.out.println(eval.toSummaryString("\nResults\n======\n", false));

            // label instances (use the trained classifier to classify new unseen instances)
            for (int i = 0; i < unlabeled.numInstances(); i++) {
                double clsLabel = classifier.classifyInstance(unlabeled.instance(i));
                train.instance(i).setClassValue(clsLabel);
                System.out.println(clsLabel + " -> " + unlabeled.classAttribute().value((int) clsLabel));
            }
            }
        }



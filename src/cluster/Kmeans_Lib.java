package cluster;

/**
 * Created by lynn on 11/6/17.
 */
import java.io.*;
import java.util.*;

import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.clustering.KMeans;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.tools.InstanceTools;
import net.sf.javaml.tools.data.FileHandler;

public class Kmeans_Lib {
    /* Load a dataset */

    public void kmeans(String path,int k) throws IOException{

    //Dataset data = FileHandler.loadSparseDataset(new File("TestCSV.csv"), 0, ",",":");
        Dataset data = FileHandler.loadDataset(new File(path), 0, ",");
    /*
     * Create a new instance of the KMeans algorithm, with no options
     * specified. By default this will generate 4 clusters.
     */
    Clusterer km = new KMeans(k);
    /*
     * Cluster the data, it will be returned as an array of data sets, with
     * each dataset representing a cluster
     */
    String filename = k + "_cluster_lib.txt";
    PrintWriter writer = new PrintWriter(filename, "UTF-8");
     Dataset[] clusters = km.cluster(data);
        for (int i = 0; i < clusters.length;i++){
            writer.println("cluster_size: " + clusters[i].size());
        }
        for (int i = 0; i < clusters.length;i++){
            writer.println(clusters[i]);
        }
        writer.close();
    }
}

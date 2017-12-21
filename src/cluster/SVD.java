package cluster;

/**
 * Created by lynn on 11/6/17.
 */

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import Jama.Matrix;
import Jama.SingularValueDecomposition;


public class SVD {
    public void SVD(String [][]matrix,int k)throws IOException {
        int M = matrix.length;
        int N = matrix[0].length;
        double[][] matrixSVD = new double[matrix.length][matrix[0].length-1];
        for(int i = 0; i < matrixSVD.length;i++){
            for(int j = 0;j < matrixSVD[i].length;j++){
                matrixSVD[i][j] = 0.0;
            }
        }
        for(int i = 0; i < matrix.length;i++){
            for (int j = 1; j < matrix[i].length;j++){
                matrixSVD[i][j-1] = Double.parseDouble(matrix[i][j]);
            }
        }


        Matrix A = new Matrix(matrixSVD);

        A = A.transpose();


        // compute the singular vallue decomposition
        //System.out.println("A = U S V^T");
        SingularValueDecomposition s = A.svd();
        Matrix U = s.getU();
        Matrix S = s.getS();
        Matrix V = s.getV();
        System.out.println("rank = " + s.rank());
        System.out.println("condition number = " + s.cond());
        System.out.println("2-norm = " + s.norm2());


        System.out.print("singular values = ");
        Matrix svalues = new Matrix(s.getSingularValues(), 1);
        svalues.print(9, 6);

        String filename = k + "_"+"SVD_Matrix_5d.data";
        PrintWriter pw = new PrintWriter(new File(k+ "_SVD_Matrix_2d.csv"));
        PrintWriter pw2 = new PrintWriter(new File(filename));
        Matrix C = U.times(S);
        //System.out.print("B = ");
        C= C.transpose();
        double[][] CMatrix = C.getArray();

        for(int i = 0; i < CMatrix.length;i++){
            pw.append(matrix[i][0] + ",");
            pw2.append(matrix[i][0] + ",");
            for(int j = 0; j < 2; j++){
                pw.append(Double.toString(CMatrix[i][j]));
                if(j != 1) {
                    pw.append(",");
                }
            }

            for(int j = 0; j < 5; j++){
                pw2.append(Double.toString(CMatrix[i][j]));
                if(j != 4) {
                    pw2.append(",");
                }
            }

            pw.append(System.getProperty("line.separator"));
            pw2.append(System.getProperty("line.separator"));
        }
        pw.close();
        pw2.close();
    }
}

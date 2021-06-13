package com.personal.experiment.matrix;

import com.personal.experiment.matrix.process.Handler;
import com.personal.experiment.matrix.util.MatrixFileReader;
import com.personal.experiment.matrix.util.MatrixFileWriter;
import com.personal.experiment.matrix.util.MatrixGenerator;
import java.util.Arrays;

public class MatrixMultiplicationExperiment {

  private static final int ROWS_AND_COLUMNS = 3;

  private static final int NUMBER_OF_MATRICES = 2;

  /* true = generate new matrices and save them in independent files.
   * false = assume the matrix files already exist, so it's not required to generate them.
   * If it's being run for the first time the value should be true, then it can be changed to false,
   * otherwise it would generate different matrices every time.*/
  private static final boolean GENERATE_AND_SAVE_MATRICES = true;

  private static final boolean PRINT_RESULT = true;

  public static void main(String[] args) {

    final Handler handler = new Handler();

    if (GENERATE_AND_SAVE_MATRICES) {
      System.out.println("Generating matrices...");
      MatrixFileWriter.getAndSaveMatrix(NUMBER_OF_MATRICES, ROWS_AND_COLUMNS, ROWS_AND_COLUMNS);
      System.out.println("Matrices were generated.");
    }

    final int matrix1[][] = MatrixFileReader
        .readMatrixFromFile(0, ROWS_AND_COLUMNS, ROWS_AND_COLUMNS, "matrix_%s.txt");
    final int matrix2[][] = MatrixFileReader
        .readMatrixFromFile(1, ROWS_AND_COLUMNS, ROWS_AND_COLUMNS, "matrix_%s.txt");
    final int result[][] = new int[matrix1.length][matrix2[0].length];

    Arrays.stream(result).forEach(r -> Arrays.fill(r, 0));

    System.out.println("About to do some maths...");
    handler.multiplyMatrices(matrix1, matrix2, result);

    if (PRINT_RESULT) {
      MatrixGenerator.printMatrix(result);
    }
    System.exit(0);
  }

}

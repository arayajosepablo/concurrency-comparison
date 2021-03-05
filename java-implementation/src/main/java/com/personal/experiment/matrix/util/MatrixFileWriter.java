package com.personal.experiment.matrix.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MatrixFileWriter {

  public static void getAndSaveMatrix(final int numberOfMatrices, final int rows,
      final int columns) {
    int matrix[][];
    try {

      for (int k = 0; k < numberOfMatrices; k++) {
        matrix = MatrixGenerator.generateMatrix(rows, columns);

        final BufferedWriter bw = new BufferedWriter(
            new FileWriter(String.format("matrix_%s.txt", k)));

        for (int i = 0; i < matrix.length; i++) {
          for (int j = 0; j < matrix[i].length; j++) {
            bw.write(matrix[i][j] + ((j == matrix[i].length - 1) ? "" : ","));
          }
          bw.newLine();
        }
        bw.flush();
        bw.close();
      }
    } catch (IOException e) {
      System.out.println("Exception: " + e.toString());
    }
  }

}

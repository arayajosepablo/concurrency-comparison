package com.personal.experiment.matrix.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class MatrixFileReader {

  public static int[][] readMatrixFromFile(final int numberOfMatrix, final int rows,
      final int columns) {
    int[][] matrix = new int[rows][columns];

    try {
      final Scanner sc = new Scanner(
          new BufferedReader(new FileReader(String.format("matrix_%s.txt", numberOfMatrix))));
      while (sc.hasNextLine()) {
        for (int i = 0; i < matrix.length; i++) {
          String[] line = sc.nextLine().trim().split(",");
          for (int j = 0; j < line.length; j++) {
            matrix[i][j] = Integer.parseInt(line[j]);
          }
        }
      }

    } catch (FileNotFoundException e) {
      System.out.println("Exception: " + e.toString());
    }

    return matrix;
  }

}

package com.personal.experiment.matrix.util;

import java.util.Random;

public class MatrixGenerator {

    public static int[][] generateMatrix(final int rows, final int columns) {

        final int[][] result = new int[rows][columns];

        final Random random = new Random();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result[i][j] = random.nextInt(1001);
            }
        }

        return result;
    }

    public static void printMatrix(final int[][] matrix) {
        final int rows = matrix.length;
        final int columns = matrix[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}

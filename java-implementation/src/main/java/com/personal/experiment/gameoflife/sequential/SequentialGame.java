package com.personal.experiment.gameoflife.sequential;

import com.personal.experiment.gameoflife.general.GameOfLife;
import com.personal.experiment.matrix.util.MatrixFileReader;
import java.util.Calendar;

public class SequentialGame {

  public static void main(String[] args) {
    final boolean printMatrix = true;

    final int n = 28;
    final int m = 20000;
    final int[][] world = MatrixFileReader.readMatrixFromFile(1, n, n, "/Users/pablo/Documents/Ideas_para_paper-Concurrencia/Repositorios/concurrency-comparison/resources/infinite_growth_%s.txt");
    final int[][] newWorld = new int[n][n];

    if (printMatrix) {
      System.out.println("World");
      GameOfLife.printMatrix(world, n);
    }

    final long start = Calendar.getInstance().getTimeInMillis();
    for (int i = 0; i < m; i++) {
      GameOfLife.applyRules(world, newWorld, n);
      GameOfLife.copyMatrix(world, newWorld, n);
//      if (printMatrix) {
//        System.out.println("Gen " + (i + 1));
//        GameOfLife.printMatrix(world, n);
//        System.out.println();
//      }
    }

    final long end = Calendar.getInstance().getTimeInMillis();
    final long totalTime = end - start;

    System.out.println("Last generation");
    GameOfLife.printMatrix(world, n);

    System.out
        .println(String
            .format("Sequential game of life experiment took %s milliseconds which is %s seconds",
                totalTime, (totalTime / 1000.0)));

    System.exit(0);

  }

}

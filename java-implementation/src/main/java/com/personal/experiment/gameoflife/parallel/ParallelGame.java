package com.personal.experiment.gameoflife.parallel;

import com.personal.experiment.gameoflife.general.GameOfLife;
import com.personal.experiment.matrix.util.MatrixFileReader;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParallelGame {

  private static final int NUMBER_OF_THREADS = 32;

  public void applyRulesInParallel(final int[][] world, final int[][] newWorld, final int n) {
    final ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    for (int i = 0; i < n; i++) {
      executorService.submit(new WorkerThread(newWorld, world, i, n));
    }

    executorService.shutdown();
    try {
      executorService.awaitTermination(60, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

  }

  public static void main(String[] args) {

    ParallelGame parallelGame = new ParallelGame();

    final boolean printMatrix = true;

    final int n = 28;
    final int m = 20000;
    final int[][] world = MatrixFileReader.readMatrixFromFile(1, n, n, "/Users/pablo/Documents/Ideas_para_paper-Concurrencia/Repositorios/concurrency-comparison/resources/infinite_growth_%s.txt");
    final int[][] newWorld = new int[n][n];

    if (printMatrix) {
      System.out.println("World");
      GameOfLife.printMatrix(world, n);
      System.out.println();
    }

    final long start = Calendar.getInstance().getTimeInMillis();
    for (int i = 0; i < m; i++) {
      parallelGame.applyRulesInParallel(world, newWorld, n);
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
            .format("Parallel game of life experiment took %s milliseconds which is %s seconds",
                totalTime, (totalTime / 1000.0)));

  }
}

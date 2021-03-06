package com.personal.experiment.matrix.process;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Handler {

  private static final int NUMBER_OF_THREADS = 8;

  public void multiplyMatrices(final int[][] matrix1, final int[][] matrix2, final int[][] result) {
    final ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    int rows = matrix1.length;

    final long start = Calendar.getInstance().getTimeInMillis();
    for (int i = 0; i < rows; i++) {
      executorService.submit(new WorkerThread(result, matrix1, matrix2, i));
    }

    executorService.shutdown();
    try {
      executorService.awaitTermination(60,
          TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    final long end = Calendar.getInstance().getTimeInMillis();
    final long totalTime = end - start;

    System.out
        .println(String
            .format("Matrix multiplication experiment took %s milliseconds which is %s seconds",
                totalTime, (totalTime / 1000.0)));
  }

}

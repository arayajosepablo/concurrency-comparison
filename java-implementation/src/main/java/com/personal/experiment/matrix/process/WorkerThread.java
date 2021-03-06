package com.personal.experiment.matrix.process;

public class WorkerThread implements Runnable {

  private final int result[][];
  private int matrix1[][];
  private int matrix2[][];
  private final int rowIndex;

  public WorkerThread(final int[][] result, final int[][] matrix1,
      final int[][] matrix2, final int rowIndex) {
    this.result = result;
    this.matrix1 = matrix1;
    this.matrix2 = matrix2;
    this.rowIndex = rowIndex;
  }

  @Override
  public void run() {
//    System.out.println(
//        "Multiplying row " + this.rowIndex + " Thread.currentThread().getId() " + Thread
//            .currentThread().getId());
//    System.out.println("" + Thread.currentThread().getId());
    for (int i = 0; i < this.matrix2[0].length; i++) {
      for (int j = 0; j < this.matrix1[this.rowIndex].length; j++) {
        this.result[this.rowIndex][i] += this.matrix1[this.rowIndex][j] * this.matrix2[j][i];
      }
    }
  }
}

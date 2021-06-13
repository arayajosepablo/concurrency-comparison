package com.personal.experiment.gameoflife.parallel;

import com.personal.experiment.gameoflife.general.GameOfLife;

public class WorkerThread implements Runnable {

  private final int[][] newWorld;
  private final int[][] world;
  private final int rowIndex;
  private final int n;

  public WorkerThread(int[][] newWorld, int[][] world, int rowIndex, int n) {
    this.newWorld = newWorld;
    this.world = world;
    this.rowIndex = rowIndex;
    this.n = n;
  }

  @Override
  public void run() {

    for (int j = 0; j < this.world[this.rowIndex].length; j++) {
      newWorld[this.rowIndex][j] = GameOfLife.rule(world, this.rowIndex, j, this.n);
    }

  }
}

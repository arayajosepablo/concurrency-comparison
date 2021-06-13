package com.personal.experiment.gameoflife.general;

public class GameOfLife {

  public static void copyMatrix(final int world[][], final int newWorld[][], final int n) {
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        world[i][j] = newWorld[i][j];
      }
    }
  }

  public static int getNeighbour(final int row, final int col, final int world[][], final int n) {
    // If a neighbour of a cell is outside the limits of the world it is considered dead.
    if (row >= n || row < 0 || col >= n || col < 0) {
      return 0;
    }

    return world[row][col];
  }

  // Get the count of neighbours for a given cell
  public static int getNeighboursCount(final int row, final int col, final int world[][], final int n) {
    int count = 0;

    count += getNeighbour(row-1, col-1, world, n);
    count += getNeighbour(row-1, col, world, n);
    count += getNeighbour(row-1, col+1, world, n);

    count += getNeighbour(row, col-1, world, n);
    count += getNeighbour(row, col+1, world, n);

    count += getNeighbour(row+1, col-1, world, n);
    count += getNeighbour(row+1, col, world, n);
    count += getNeighbour(row+1, col+1, world, n);

    return count;
  }

  public static int rule(final int world[][], final int row, final int col, final int n) {
    final int count = getNeighboursCount(row, col, world, n);
    final boolean birth = world[row][col] == 0 && count == 3;
    final boolean survive = world[row][col] == 1 && (count == 2 || count == 3);
    if (birth || survive) {
      return 1;
    }
    return 0;
  }

  public static void applyRules(final int world[][], final int newWorld[][], final int n) {
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        newWorld[i][j] = rule(world, i, j, n);
      }
    }
  }

  public static void printMatrix(final int matrix[][], final int n) {
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        System.out.printf("%3d", matrix[i][j]);
      }
      System.out.println();
    }

  }

}

package com.personal.experiment.quicksort.parallel;

import com.personal.experiment.quicksort.AbstractQuickSort;
import com.personal.experiment.quicksort.util.ArrayHandler;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

public class ParallelQuickSort extends AbstractQuickSort {

  public static final int ARRAY_SIZE = 10000000;

  public static final int MAX_VALUE = 10000000;

  private static final int NUMBER_OF_THREADS = 32;

  public static final boolean GENERATE_AND_SAVE_ARRAY = false;

  public static final boolean PRINT_RESULT = false;

  public void sort(int[] array, ForkJoinPool forkJoinPool) {
    forkJoinPool.invoke(new SortRun(array, 0, array.length - 1));
  }

  @Override
  public void sort(int[] array, int left, int right) {
    if (left < right) {
      int pivot = getPivot(array, left, right);
      SortRun task;
      if (pivot - left > right - pivot) {
        task = new SortRun(array, pivot + 1, right);
        task.fork();
        sort(array, left, pivot);
      } else {
        task = new SortRun(array, left, pivot);
        task.fork();
        sort(array, pivot + 1, right);
      }
      task.join();
    }
  }

  class SortRun extends RecursiveAction {

    private final int[] array;
    private final int left;
    private final int right;

    SortRun(int[] array, int left, int right) {
      this.array = array;
      this.left = left;
      this.right = right;
    }

    @Override
    protected void compute() {
      if (left < right) {
        int t = getPivot(array, left, right);
        sort(array, left, t);
        sort(array, t + 1, right);
      }
    }
  }

  private static void warmup() {
    final ParallelQuickSort parallelQuickSort = new ParallelQuickSort();
    final ForkJoinPool forkJoinPool = new ForkJoinPool(2);
    for (int i = 0; i < 10000; i++) {
      parallelQuickSort.sort(ArrayHandler.generateArrayToSort(100, 100), forkJoinPool);
    }
  }

  public static void main(String[] args) throws InterruptedException {

    if (GENERATE_AND_SAVE_ARRAY) {
      System.out.println("Generating and saving array...");
      ArrayHandler.writeArrayToFile(ARRAY_SIZE, MAX_VALUE);
      System.out.println("The array was saved.");
    }

    System.out.println("Loading array in memory...");
    final int[] array = ArrayHandler.readArrayFromFile();
    System.out.println("Array was loaded.");

    // Cooling down after loading the array on memory
    TimeUnit.SECONDS.sleep(3);

    warmup();
    final ParallelQuickSort parallelQuickSort = new ParallelQuickSort();
    final ForkJoinPool forkJoinPool = new ForkJoinPool(NUMBER_OF_THREADS);

    System.out.println("About to do some sorting...");
    long elapsedTime = System.currentTimeMillis();
    parallelQuickSort.sort(array, forkJoinPool);
    elapsedTime = System.currentTimeMillis() - elapsedTime;

    if (PRINT_RESULT) {
      ArrayHandler.printArray(array);
    }

    if (!parallelQuickSort.isSorted(array)) {
      System.out.println("The array is not sorted");
    }

    System.out.println(String
        .format("It took %s milliseconds to sort the array using %s threads.", elapsedTime,
            NUMBER_OF_THREADS));
  }

}

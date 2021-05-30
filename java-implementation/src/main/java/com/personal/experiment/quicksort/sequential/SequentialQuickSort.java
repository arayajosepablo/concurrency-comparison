package com.personal.experiment.quicksort.sequential;

import com.personal.experiment.quicksort.AbstractQuickSort;
import com.personal.experiment.quicksort.parallel.ParallelQuickSort;
import com.personal.experiment.quicksort.util.ArrayHandler;
import java.util.concurrent.TimeUnit;

public class SequentialQuickSort extends AbstractQuickSort {

  @Override
  public void sort(int[] array, int left, int right) {
    if (left < right) {
      int pivot = getPivot(array, left, right);
      sort(array, left, pivot);
      sort(array, pivot + 1, right);
    }
  }

  private static void warmup() {
    final SequentialQuickSort sequentialQuickSort = new SequentialQuickSort();
    for (int i = 0; i < 10000; i++) {
      sequentialQuickSort.sort(ArrayHandler.generateArrayToSort(100, 100));
    }
  }

  public static void main(String[] args) throws InterruptedException {

    if (ParallelQuickSort.GENERATE_AND_SAVE_ARRAY) {
      System.out.println("Generating and saving array...");
      ArrayHandler.writeArrayToFile(ParallelQuickSort.ARRAY_SIZE, ParallelQuickSort.MAX_VALUE);
      System.out.println("The array was saved.");
    }

    System.out.println("Loading array in memory...");
    final int[] array = ArrayHandler.readArrayFromFile();
    System.out.println("Array was loaded.");

    // Cooling down after loading the array on memory
    TimeUnit.SECONDS.sleep(3);

    warmup();
    final SequentialQuickSort seqSort = new SequentialQuickSort();
    long elapsedTime = System.currentTimeMillis();
    System.out.println("About to do some sorting...");
    seqSort.sort(array);
    elapsedTime = System.currentTimeMillis() - elapsedTime;

    if(ParallelQuickSort.PRINT_RESULT) {
      ArrayHandler.printArray(array);
    }

    System.out.println(String
        .format("It took %s milliseconds to sort the array in sequential mode.", elapsedTime));
  }

}

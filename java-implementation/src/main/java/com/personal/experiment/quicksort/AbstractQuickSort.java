package com.personal.experiment.quicksort;

import java.util.Random;
import java.util.stream.IntStream;

public abstract class AbstractQuickSort {

    protected int getPivot(int[] array, int left, int right) {
        int pivot = left;
        int valueInPivot = array[pivot];
        int tmp;
        for (int i = left + 1; i <= right; i++) {
            if(array[i] < valueInPivot) {
                pivot++;
                tmp = array[pivot];
                array[pivot] = array[i];
                array[i] = tmp;
            }
        }
        tmp = array[left];
        array[left] = array[pivot];
        array[pivot] = tmp;
        return pivot;
    }

    abstract public void sort(int[] array, int left, int right);

    public void sort(int[] array) {
        sort(array, 0, array.length - 1);
    }

    protected static int[] generateArray(int size, int maxValue) {
        Random random = new Random();
        return IntStream.generate(() -> Math.abs(random.nextInt() % maxValue)).limit(size).toArray();
    }
}

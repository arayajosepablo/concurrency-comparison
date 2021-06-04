package com.personal.experiment.quicksort;

// This implementation is based on https://github.com/cvbnnthc1/QuickSort

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

}

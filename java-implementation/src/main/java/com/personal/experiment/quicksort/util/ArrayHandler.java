package com.personal.experiment.quicksort.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ArrayHandler {

  private static final String FILE_NAME = "arrayToSort.txt";
  private static final int MAX_VALUE = 100;

  public static int[] generateArrayToSort(final int size, final int maxValue) {
    Random random = new Random();
    final int[] array = IntStream.generate(() -> Math.abs(random.nextInt() % maxValue)).limit(size)
        .toArray();

    return array;
  }

  public static void writeArrayToFile(final int sizeOfArray, final int maxValue) {
    try {
      BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME));
      final int[] array = ArrayHandler.generateArrayToSort(sizeOfArray, maxValue);
      for (int i = 0; i < sizeOfArray; i++) {
        bw.write(array[i] + ((i == sizeOfArray - 1) ? "" : ","));
      }
      bw.flush();
      bw.close();
    } catch (IOException e) {
      System.out.println("Exception writing the array file: " + e.toString());
    }
  }

  public static int[] readArrayFromFile() {
    int[] array = null;

    try {
      final Scanner sc = new Scanner(new BufferedReader(new FileReader(FILE_NAME)));
      array = Stream.of(sc.nextLine().trim().split(",")).mapToInt(Integer::parseInt).toArray();
    } catch (FileNotFoundException e) {
      System.out.println("Exception reading the array file: " + e.toString());
    }

    return array;
  }

  public static void printArray(final int[] array) {
    System.out.println(Arrays.toString(array));
  }

}

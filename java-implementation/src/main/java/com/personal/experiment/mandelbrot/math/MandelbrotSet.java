package com.personal.experiment.mandelbrot.math;

import java.awt.Color;

public class MandelbrotSet {

  // Took from http://introcs.cs.princeton.edu/java/32class/Mandelbrot.java.html
  private static int mandelbrot(final Complex z0, final int maxIterations) {
    Complex z = z0;
    for (int t = 0; t < maxIterations; t++) {
      if (z.abs() > 2.0) {
        return t;
      }
      z = z.times(z).plus(z0);
    }
    return maxIterations;
  }

  public static Color blackAndWhiteMandelbrot(final Complex z0, final int maxIterations) {
    final int colorValue = maxIterations - MandelbrotSet.mandelbrot(z0, maxIterations);
    return new Color(colorValue, colorValue, colorValue);
  }
}

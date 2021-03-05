package com.personal.experiment.mandelbrot;

import com.personal.experiment.mandelbrot.process.Handler;

public class MandelbrotSetExperiment {

  public static void main(String[] args) {
    final Handler handler = new Handler();
    handler.patchProcess();
  }
}

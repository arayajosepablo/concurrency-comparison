package com.personal.experiment.mandelbrot.process;

import com.personal.experiment.mandelbrot.math.Complex;
import com.personal.experiment.mandelbrot.math.MandelbrotSet;

import java.awt.Color;

public class WorkerThread implements Runnable {

  private final int startI;
  private final int endI;
  private final int startJ;
  private final int endJ;
  private final Parameters parameters;

  public WorkerThread(final int startI, final int endI, final int startJ, final int endJ,
      final Parameters parameters) {
    this.startI = startI;
    this.endI = endI;
    this.startJ = startJ;
    this.endJ = endJ;
    this.parameters = parameters;
  }

  @Override
  public void run() {
    for (int i = this.startI; i < this.endI; i++) {
      for (int j = this.startJ; j < this.endJ; j++) {
        final double x0 = j / this.parameters.getZoomX() + this.parameters.getX1();
        final double y0 = i / this.parameters.getZoomY() + this.parameters.getY1();

        final Complex z0 = new Complex(x0, y0);
        final Color color = MandelbrotSet
            .blackAndWhiteMandelbrot(z0, this.parameters.getMaxIterations());
        this.parameters.getImageChunks()[i * this.parameters.getWidth() + j] = color.getRGB();
      }
    }
  }
}

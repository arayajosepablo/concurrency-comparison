package com.personal.experiment.mandelbrot.process;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class Parameters {

  private double x1;
  private double x2;
  private double y1;
  private double y2;
  private int maxIterations;
  private int width;
  private int height;
  private final int imageChunks[];
  private final double zoomX;
  private final double zoomY;
}

package com.personal.experiment.mandelbrot.math;

import lombok.AllArgsConstructor;
import lombok.Getter;

// Took from http://introcs.cs.princeton.edu/java/32class/Complex.java.html
@AllArgsConstructor
@Getter
public class Complex {

  private final double real;
  private final double imaginary;

  @Override
  public String toString() {
    if (this.imaginary == 0) {
      return this.real + "";
    }
    if (this.real == 0) {
      return this.imaginary + "i";
    }
    if (this.imaginary < 0) {
      return real + " - " + (-this.imaginary) + "i";
    }
    return real + " + " + this.imaginary + "i";
  }

  public double abs() {
    return Math.sqrt(this.real * this.real + this.imaginary * this.imaginary);
  }

  public Complex plus(final Complex b) {
    final double real = this.real + b.real;
    final double imaginary = this.imaginary + b.imaginary;
    return new Complex(real, imaginary);
  }

  public Complex times(final Complex b) {
    final double real = this.real * b.real - this.imaginary * b.imaginary;
    final double imaginary = this.real * b.imaginary + this.imaginary * b.real;
    return new Complex(real, imaginary);
  }
}

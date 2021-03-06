package com.personal.experiment.mandelbrot.process;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Handler {

  private static final int NUMBER_OF_THREADS = 8;
  private static final int WIDTH = 800;
  private static final int HEIGHT = 800;
  private static final int PATCH_HEIGHT = 10;
  private static final int PATCH_WIDTH = 10;
  private static final int MAX_ITERATIONS = 255;
  private static final double X1 = -2.1;
  private static final double X2 = 0.6;
  private static final double Y1 = -1.2;
  private static final double Y2 = 1.2;

  public void patchProcess() {
    final ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    int endI;
    int endJ;

    final Parameters parameters = new Parameters(X1, X2, Y1, Y2,
        MAX_ITERATIONS, WIDTH, HEIGHT, new int[WIDTH * HEIGHT],
        WIDTH / (X2 - X1), HEIGHT / (Y2 - Y1));

    final long start = Calendar.getInstance().getTimeInMillis();
    for (int i = 0; i < parameters.getHeight(); i += PATCH_HEIGHT) {
      endI = Math.min(i + PATCH_HEIGHT, parameters.getHeight());

      for (int j = 0; j < parameters.getWidth(); j += PATCH_WIDTH) {
        endJ = Math.min(j + PATCH_WIDTH, parameters.getWidth());
        executorService.submit(new WorkerThread(i, endI, j, endJ, parameters));
      }
    }
    executorService.shutdown();
    try {
      executorService.awaitTermination(60,
          TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    final long end = Calendar.getInstance().getTimeInMillis();

    final long totalTime = end - start;

    this.saveImage(parameters);

    System.out
        .println(String.format("MandelbrotSet experiment took %s milliseconds which is %s seconds",
            totalTime, (totalTime / 1000.0)));
  }

  private void saveImage(final Parameters parameters) {
    final BufferedImage image = new BufferedImage(parameters.getWidth(), parameters.getHeight(),
        BufferedImage.TYPE_INT_RGB);
    image.setRGB(0, 0, image.getWidth(), image.getHeight(),
        parameters.getImageChunks(), 0, image.getWidth());
    final File file = new File("mandelbrotSet.jpg");

    try {
      ImageIO.write(image, "jpg", file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

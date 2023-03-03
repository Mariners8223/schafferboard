package edu.wpi.first.shuffleboard.api.util;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * A representation of an image of a grid. This is not itself an image, but can create a new image based on this
 * representation with {@link #getAsImage(Color) getAsImage()}.
 */
public final class GridImage {

  private final int width;
  private final int height;
  private final int borderThickness;
  private final int primaryLineCount;
  private final int primaryLineThickness;

  /**
   * Creates a new grid image representation.
   *
   * @param width                  the overall width of the image
   * @param height                 the overall height of the image
   * @param borderThickness        the thickness of the borders
   * @param secondaryLineCount     the number of secondary lines
   * @param secondaryLineThickness the thickness of the secondary lines
   */
  public GridImage(int width, int height, int borderThickness, int secondaryLineCount, int secondaryLineThickness) {
    checkArgument(width > 0, "Width must be positive (given: " + width + ")");
    checkArgument(height > 0, "Height must be positive (given: " + height + ")");
    checkArgument(borderThickness > 0, "Border thickness must be positive (given: " + borderThickness + ")");
    checkArgument(secondaryLineCount >= 0, "Secondary line count must be non-negative");
    checkArgument(secondaryLineThickness > 0, "Secondary line thickness must be positive");
    this.width = width;
    this.height = height;
    this.borderThickness = borderThickness;
    this.primaryLineCount = secondaryLineCount;
    this.primaryLineThickness = secondaryLineThickness;
  }

  /**
   * Creates an image of a grid based off this model.
   *
   * @param lineColor the color of the lines of the grid
   *
   * @return a new image of the grid
   */
  public Image getAsImage(Color lineColor, int offset) {
    WritableImage image = new WritableImage(width, height);

    // borders
    fillRect(image, offset, offset, borderThickness, height, lineColor); // left edge
    fillRect(image, width - borderThickness + offset, offset, borderThickness, height, lineColor); // right edge
    fillRect(image, offset, offset, width, borderThickness, lineColor); // top edge
    fillRect(image, offset, height - borderThickness + offset, width, borderThickness, lineColor); // bottom edge

    // primary lines
    if (primaryLineCount > 0 && primaryLineThickness > 0) {
      int[] primaryLineStops = new int[primaryLineCount];

      // vertical
      for (int i = 0; i < primaryLineCount; i++) {
        primaryLineStops[i] = (i + 1) * width / (primaryLineCount + 1);
      }
      for (int i = 0; i < primaryLineCount; i++) {
        fillRect(image, primaryLineStops[i] + offset, offset, primaryLineThickness, height, lineColor);
      }

      // horizontal
      for (int i = 0; i < primaryLineCount; i++) {
        primaryLineStops[i] = (i + 1) * height / (primaryLineCount + 1);
      }
      for (int i = 0; i < primaryLineCount; i++) {
        fillRect(image, offset, primaryLineStops[i] + offset, width, primaryLineThickness, lineColor);
      }
    }

    return image;
  }

  /**
   * Draws a filled rectangle with the given dimensions and color on an image.
   *
   * @param image the image to draw the rectangle on
   * @param x     the x-coordinate of the top-left corner of the rectangle, in pixels
   * @param y     the y-coordinate of the top-left corner of the rectangle, in pixels
   * @param w     the width of the rectangle, in pixels
   * @param h     the height of the rectangle, in pixels
   * @param color the color of the rectangle
   */
  private void fillRect(WritableImage image, int x, int y, int w, int h, Color color) {
    PixelWriter writer = image.getPixelWriter();
    for (int i = x; i < x + w; i++) {
      for (int j = y; j < y + h; j++) {
        writer.setColor(i < 0 ? width + i : i % width, j < 0 ? height + j : j % height, color);
      }
    }
  }

}

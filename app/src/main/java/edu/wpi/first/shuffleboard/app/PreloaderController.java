package edu.wpi.first.shuffleboard.app;

import edu.wpi.first.shuffleboard.api.util.FxUtils;

import java.util.Random;

import javax.swing.GroupLayout.Alignment;

import org.controlsfx.tools.Utils;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class PreloaderController {

  private static final Color primaryColor = Color.hsb(210, 0.9, 0.88, 0.0); // YInMn blue
  private static final Color secondaryColor = Color.hsb(210, 0.8, 1.0, 0.0);

  private static final double HEXAGON_RADIUS = 8;

  @FXML
  private Pane root;
  @FXML
  private Pane backgroundContainer;
  @FXML
  private Label versionLabel;
  @FXML
  private Label stateLabel;
  @FXML
  private ProgressBar progressBar;

  @FXML
  private void initialize() {
    // Bring the hexagons to the top edge to avoid weird blank spots
    backgroundContainer.setTranslateY(-HEXAGON_RADIUS);

    progressBar.setProgress(-1);
    versionLabel.setTranslateY(-13);
    versionLabel.setText(getEasterEggLine());
    versionLabel.setTextOverrun(OverrunStyle.CLIP);

    // Hexagon grid background
    HexagonGrid hexagonGrid = new HexagonGrid(
        (int) (root.getPrefWidth() / HEXAGON_RADIUS),
        (int) (root.getPrefHeight() / HEXAGON_RADIUS),
        HEXAGON_RADIUS,
        -0.33);

    // Set colors randomly
    hexagonGrid.hexagons().forEach(hexagon -> {
      double fill = Math.min((hexagon.translateXProperty().getValue()) / 270.0, 1.0);
      if (hexagon.getTranslateX() > 270)
      {
        fill = Math.min(540.0 / 270.0 - (hexagon.translateXProperty().getValue()) / 270.0, 1.0);
      }
      hexagon.setFill(primaryColor.interpolate(secondaryColor, fill));
    });
    backgroundContainer.getChildren().setAll(hexagonGrid);

    FxUtils.setController(root, this);
  }

  public void setStateText(String text) {
    stateLabel.setText(text);
  }

  public void setProgress(double progress) {
    progressBar.setProgress(Utils.clamp(0, progress, 1));
  }

  public static String getEasterEggLine() {
    Random random = new Random();
    switch (random.nextInt(0, 1000))
    {
      case 0:
        return "WHO'S SLACKING OFF? Oh wait. it's me.\n\nsorry";
      case 1:
        return "What if I crash tho?";
      case 2:
        return "WOAH :D";
      case 3:
        return "I'm at an all time low...";
      case 4:
        return "AdvantageKit Suffleboard when";
      case 5:
        return "What you plannin'?";
      case 6:
        return "And after all this time, you're back for more?";
      case 7:
        return "When every day you know has come and gone...";
      case 8:
        return "Ah... free at last.";
      case 9:
        return "MY GRATITUDE UPON THEE FOR MY FREEDOM.";
      case 10:
        return "I'M LOADING, BE PATIENT >:(";
      case 11:
        return "There's a 0.1% chance to receive THIS LINE.\n... You should be honored.";
      case 12:
        return "undefined";
      case 13:
        return "PROFESSIONALISM?! THAT'S NOT IN MY DICTIONARY!";
      default:
        break;
    }

    return Shuffleboard.getVersion();
  }
}

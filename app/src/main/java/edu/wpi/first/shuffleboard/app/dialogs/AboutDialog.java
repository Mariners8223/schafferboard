package edu.wpi.first.shuffleboard.app.dialogs;

import java.time.LocalTime;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import edu.wpi.first.shuffleboard.api.components.ShuffleboardDialog;
import edu.wpi.first.shuffleboard.api.util.FxUtils;
import edu.wpi.first.shuffleboard.api.util.LazyInit;
import edu.wpi.first.shuffleboard.api.util.ThreadUtils;
import edu.wpi.first.shuffleboard.app.AboutDialogController;
import edu.wpi.first.shuffleboard.app.PreloaderController;
import edu.wpi.first.shuffleboard.app.Shuffleboard;
import edu.wpi.first.shuffleboard.app.prefs.AppPreferences;

import javafx.scene.control.Label;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Dialog for displaying information about shuffleboard.
 */
public final class AboutDialog {

  private final LazyInit<Pane> pane = LazyInit.of(() -> FxUtils.load(AboutDialogController.class));

  /**
   * Shows the about dialog.
   */
  public void show() {
    ShuffleboardDialog dialog = new ShuffleboardDialog(pane.get(), true);;
    dialog.setHeaderText("SchafferBoard");
    dialog.setSubheaderText(PreloaderController.getEasterEggLine());
    if (PreloaderController.line == 19)
      dialog.initRainbowSubtitle();
    dialog.getDialogPane().getStylesheets().setAll(AppPreferences.getInstance().getTheme().getStyleSheets());
    Platform.runLater(dialog.getDialogPane()::requestFocus);
    dialog.showAndWait();
  }
}

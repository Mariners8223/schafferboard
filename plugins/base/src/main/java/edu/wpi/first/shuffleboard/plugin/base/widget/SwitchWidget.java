package edu.wpi.first.shuffleboard.plugin.base.widget;

import edu.wpi.first.shuffleboard.api.prefs.Group;
import edu.wpi.first.shuffleboard.api.prefs.Setting;
import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;

import com.google.common.collect.ImmutableList;

import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

@Description(name = "Switch", dataTypes = Boolean.class)
@ParametrizedController("Switch.fxml")
public class SwitchWidget extends SimpleAnnotatedWidget<Boolean> {

  @FXML
  private Pane root;
  @FXML
  private StackPane left;
  @FXML
  private StackPane right;
  @FXML
  private Label leftLabel;
  @FXML
  private Label rightLabel;

  private final Property<Color> trueLeftColor = new SimpleObjectProperty<>(this, "trueLeftColor", Color.web("#ffdd23"));
  private final Property<Color> trueRightColor = new SimpleObjectProperty<>(this, "trueRightColor", Color.PURPLE);
  private final Property<Color> falseColor = new SimpleObjectProperty<>(this, "rightColor", Color.GRAY);

  @FXML
  private void initialize() {
    left.backgroundProperty().bind(
        Bindings.createObjectBinding(
            () -> createSolidColorBackground(getLeftColor(), new CornerRadii(32, 0, 0, 32, false)),
            dataProperty(), trueLeftColor, falseColor));
    right.backgroundProperty().bind(
        Bindings.createObjectBinding(
            () -> createSolidColorBackground(getRightColor(), new CornerRadii(0, 32, 32, 0, false)),
            dataProperty(), trueRightColor, falseColor));
    dataOrDefault.addListener((__, oldVal, newVal) -> {
      left.getStyleClass().clear();
      right.getStyleClass().clear();
      if (newVal.booleanValue()) {
        System.out.println("LEFT");
        left.getStyleClass().add("on");
      } else {
        System.out.println("RIGHT");
        right.getStyleClass().add("on");
      }
    });
    left.getStyleClass().add("on");
  }

  @Override
  public List<Group> getSettings() {
    return ImmutableList.of(
        Group.of("Colors",
            Setting.of("Color when true (left)", "The color to use when the value is `true`",
                trueLeftColor, Color.class),
            Setting.of("Color when true (right)", "The color to use when the value is `true`",
                trueRightColor, Color.class),
            Setting.of("Color when false", "The color to use when the value is `false`",
                falseColor, Color.class)),
        Group.of("Labels",
            Setting.of("Left label", "The displayed text on the left", leftLabel.textProperty(), String.class),
            Setting.of("Right label", "The displayed text on the right", rightLabel.textProperty(), String.class)));
  }

  private Color getLeftColor() {
    final Boolean data = getData();
    if (data == null) {
      return falseColor.getValue();
    }

    if (data.booleanValue()) {
      return trueLeftColor.getValue();
    } else {
      return falseColor.getValue();
    }
  }

  private Color getRightColor() {
    final Boolean data = getData();
    if (data == null) {
      return falseColor.getValue();
    }

    if (data.booleanValue()) {
      return falseColor.getValue();
    } else {
      return trueRightColor.getValue();
    }
  }

  private Background createSolidColorBackground(Color color, CornerRadii radii) {
    Background bg = new Background(new BackgroundFill(color, radii, null));
    return bg;
  }

  public Color getTrueLeftColor() {
    return trueLeftColor.getValue();
  }

  public Property<Color> trueLeftColorProperty() {
    return trueRightColor;
  }

  public void setTrueLeftColor(Color trueColor) {
    this.trueLeftColor.setValue(trueColor);
  }

  public Color getTrueRightColor() {
    return trueRightColor.getValue();
  }

  public Property<Color> trueRightColorProperty() {
    return trueRightColor;
  }

  public void setTrueRightColor(Color trueColor) {
    this.trueRightColor.setValue(trueColor);
  }

  public Color getFalseColor() {
    return falseColor.getValue();
  }

  public Property<Color> falseColorProperty() {
    return falseColor;
  }

  public void setFalseColor(Color color) {
    falseColor.setValue(color);
  }

  @Override
  public Pane getView() {
    return root;
  }
}

package edu.wpi.first.shuffleboard.plugin.base.widget;

import edu.wpi.first.shuffleboard.api.prefs.Group;
import edu.wpi.first.shuffleboard.api.prefs.Setting;
import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;

import com.google.common.collect.ImmutableList;

import java.io.Console;
import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

@Description(
    name = "Switch",
    dataTypes = Boolean.class)
@ParametrizedController("Switch.fxml")
public class SwitchWidget extends SimpleAnnotatedWidget<Boolean> {

  @FXML
  private Pane root;
  @FXML
  private StackPane left;
  @FXML
  private StackPane right;

  private final Property<Color> trueColor
      = new SimpleObjectProperty<>(this, "leftColor", Color.YELLOW);
  private final Property<Color> falseColor
      = new SimpleObjectProperty<>(this, "rightColor", Color.PURPLE);

  @FXML
  private void initialize() {
    dataOrDefault.addListener((__, oldVal, newVal) -> {
      left.getStyleClass().clear();
      right.getStyleClass().clear();
      if (newVal.booleanValue())
      {
        System.out.println("LEFT");
        left.getStyleClass().add("on");
      }
      else
      {
        System.out.println("RIGHT");
        right.getStyleClass().add("on");
      }
    });
    left.getStyleClass().add("on");
    dataProperty().setValue(true);
  }

  /*@Override
  public List<Group> getSettings() {
    return ImmutableList.of(
        Group.of("Colors",
            Setting.of("Color when true", "The color to use when the value is `true`", trueColor, Color.class),
            Setting.of("Color when false", "The color to use when the value is `false`", falseColor, Color.class)
        )
    );
  }*/

  @Override
  public Pane getView() {
    return root;
  }
}

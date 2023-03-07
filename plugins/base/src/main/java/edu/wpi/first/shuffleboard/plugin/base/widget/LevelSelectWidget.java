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

@Description(name = "Level Select", dataTypes = Number.class)
@ParametrizedController("LevelSelect.fxml")
public class LevelSelectWidget extends SimpleAnnotatedWidget<Number> {

    @FXML
    private Pane root;
    @FXML
    private StackPane left;
    @FXML
    private StackPane right;
    @FXML
    private StackPane middle;
    @FXML
    private Label leftLabel;
    @FXML
    private Label rightLabel;
    @FXML
    private Label middleLabel;

    private final Property<Color> trueColor = new SimpleObjectProperty<>(this, "trueLeftColor", Color.GRAY);
    private final Property<Color> falseColor = new SimpleObjectProperty<>(this, "rightColor", Color.web("#333333"));

    @FXML
    private void initialize() {
        left.backgroundProperty().bind(
                Bindings.createObjectBinding(
                        () -> createSolidColorBackground(getColor(0), new CornerRadii(32, 32, 0, 0, false)),
                        dataProperty(), trueColor, falseColor));
        middle.backgroundProperty().bind(
                Bindings.createObjectBinding(
                        () -> createSolidColorBackground(getColor(1), new CornerRadii(0, 0, 0, 0, false)),
                        dataProperty(), trueColor, falseColor));
        right.backgroundProperty().bind(
                Bindings.createObjectBinding(
                        () -> createSolidColorBackground(getColor(2), new CornerRadii(0, 0, 32, 32, false)),
                        dataProperty(), trueColor, falseColor));
    }

    @Override
    public List<Group> getSettings() {
        return ImmutableList.of(
                Group.of("Colors",
                        Setting.of("Color when selected", "The color to use when this level is selected.",
                                trueColor, Color.class),
                        Setting.of("Default color", "The default color to use.",
                                falseColor, Color.class)),
                Group.of("Labels",
                        Setting.of("Top label", "The displayed text on the top", leftLabel.textProperty(),
                                String.class),
                        Setting.of("Middle label", "The displayed text on the middle", middleLabel.textProperty(),
                                String.class),
                        Setting.of("Bottom label", "The displayed text on the bottom", rightLabel.textProperty(),
                                String.class)));
    }

    private Color getColor(int level) {
        final Number data = getData();
        if (data == null) {
            return falseColor.getValue();
        }

        if (data.intValue() == level) {
            return trueColor.getValue();
        }
        return falseColor.getValue();
    }

    private Background createSolidColorBackground(Color color, CornerRadii radii) {
        Background bg = new Background(new BackgroundFill(color, radii, null));
        return bg;
    }

    public Color getTrueColor() {
        return trueColor.getValue();
    }

    public Property<Color> trueColorProperty() {
        return trueColor;
    }

    public void setTrueColors(Color trueColor) {
        this.trueColor.setValue(trueColor);
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

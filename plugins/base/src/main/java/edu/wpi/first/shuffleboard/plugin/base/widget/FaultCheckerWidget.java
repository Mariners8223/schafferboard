package edu.wpi.first.shuffleboard.plugin.base.widget;

import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

@Description(name = "Fault Checker", dataTypes = { double[].class })
@ParametrizedController("FaultChecker.fxml")
public class FaultCheckerWidget extends SimpleAnnotatedWidget<double[]> {
    
    final String[] statusStrings = { "offline", "online", "warn", "fatal" };
    final String[] statusTextStrings = { "?", "", "!", "x" };
    @FXML
    private Pane root;
    @FXML
    private HBox engines;
    @FXML
    private VBox list;
    @FXML
    private Label titleLabel;

    @FXML
    private void initialize()
    {
        titleLabel.textProperty().bindBidirectional(titleProperty());
        dataOrDefault.addListener((__, oldData, newData) -> {
            int i = 0;
            for (i = 0; i < Math.min(newData.length, 4); i++)
            {
                int status = (int)newData[i];
                
                StackPane bg = (StackPane)engines.getChildren().get(i);
                bg.setVisible(true);
                bg.getStyleClass().clear();d
                bg.getStyleClass().add(statusStrings[status]);
                Label text = (Label)bg.getChildren().get(0);
                text.setText(statusTextStrings[status]);
            }
            for (; i < 4; i++)
            {
                StackPane bg = (StackPane)engines.getChildren().get(i);
                bg.setVisible(false);
            }
        });
        
        dataProperty().setValue(new double[]{0, 0, 0, 0});
    }

    @Override
    public Pane getView() {
        return root;
    }

}

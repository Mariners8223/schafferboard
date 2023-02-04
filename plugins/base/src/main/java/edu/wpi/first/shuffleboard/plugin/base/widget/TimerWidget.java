package edu.wpi.first.shuffleboard.plugin.base.widget;

import java.time.LocalTime;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

import edu.wpi.first.shuffleboard.api.util.ThreadUtils;
import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;
import edu.wpi.first.shuffleboard.plugin.base.data.TimerData;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;

// mode
// maxTime
// time

@Description(name = "Timer", dataTypes = {TimerData.class})
@ParametrizedController("Timer.fxml")
public class TimerWidget extends SimpleAnnotatedWidget<TimerData>  {

    @FXML
    private Pane root;
    @FXML
    private Label text;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label titleLabel;
    
    private final BooleanProperty showText = new SimpleBooleanProperty(this, "showText", true);

    private final ScheduledExecutorService autoRunnerExecutor = ThreadUtils.newDaemonScheduledExecutorService();

    private Stopwatch stopwatch;


    @FXML
    private void initialize()
    {
        titleLabel.textProperty().bindBidirectional(titleProperty());
        text.setTextAlignment(TextAlignment.RIGHT);
        stopwatch = Stopwatch.createStarted();
        autoRunnerExecutor.submit(() -> updateText());
    }

    private void updateText()
    {
        Platform.runLater(new Runnable(){
            @Override
            public void run()
            {
                if (getSource().getData() == null)
                    return;
                text.setText(String.format("%d:%02d (%d)", stopwatch.elapsed(TimeUnit.MINUTES), stopwatch.elapsed(TimeUnit.SECONDS) % 60, ((TimerData)getSource().getData()).getMode()));
                text.setTextAlignment(TextAlignment.RIGHT);
                progressBar.setProgress((double)stopwatch.elapsed(TimeUnit.MILLISECONDS) / (((TimerData)getSource().getData()).getTimerDuration() * 1000.0) % 1.0);
            }
        });
        autoRunnerExecutor.schedule(() -> updateText(), 16, TimeUnit.MILLISECONDS);
    }

    @Override
    public Pane getView() {
        return root;
    }  
}

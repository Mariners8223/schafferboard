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
import edu.wpi.first.shuffleboard.plugin.base.data.types.TimerType;
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

    private double offset;


    @FXML
    private void initialize()
    {
        //titleLabel.textProperty().bindBidirectional(titleProperty());
        text.setTextAlignment(TextAlignment.RIGHT);
        stopwatch = Stopwatch.createStarted();
        autoRunnerExecutor.submit(() -> updateText());
        dataOrDefault.addListener((__, oldData, newData) -> {
            if (newData.getMode() != oldData.getMode() || newData.getTimerDuration() != oldData.getTimerDuration()) {
                stopwatch.reset();
                stopwatch.start();
            }
            offset = ((double)stopwatch.elapsed(TimeUnit.MICROSECONDS) / 1000000.0) - newData.getTimerOffset();
        });
    }

    @SuppressWarnings("unchecked")
    private void updateText()
    {
        Platform.runLater(new Runnable(){
            @Override
            public void run()
            {
                if (getSource().getData() == null)
                    getSource().setData(TimerType.Instance.getDefaultValue());
                
                double timeLeft = ((TimerData)getSource().getData()).getTimerDuration() - ((double)stopwatch.elapsed(TimeUnit.MILLISECONDS) / 1000.0) + offset;
                if (timeLeft < 0)
                    timeLeft = 0;
                if ((int)Math.ceil(timeLeft) >= 60)
                    text.setText(String.format("%d:%02d", (int)(Math.ceil(timeLeft)) / 60, (int)(Math.ceil(timeLeft)) % 60));
                else text.setText(String.format("%d", (int)(Math.ceil(timeLeft))));
                
                text.setTextAlignment(TextAlignment.RIGHT);

                switch ((int)((TimerData)getSource().getData()).getMode())
                {
                    default:
                    case 1:
                        titleLabel.setText("AUTO");
                        progressBar.getStyleClass().remove("teleop-timer");
                        progressBar.getStyleClass().add("auto-timer");
                        break;
                    case 0:
                        titleLabel.setText("TELE-OP");
                        progressBar.getStyleClass().remove("auto-timer");
                        progressBar.getStyleClass().add("teleop-timer");
                        break;
                }
                progressBar.setProgress(timeLeft / ((TimerData)getSource().getData()).getTimerDuration());
            }
        });
        autoRunnerExecutor.schedule(() -> updateText(), 16, TimeUnit.MILLISECONDS);
    }

    @Override
    public Pane getView() {
        return root;
    }  
}

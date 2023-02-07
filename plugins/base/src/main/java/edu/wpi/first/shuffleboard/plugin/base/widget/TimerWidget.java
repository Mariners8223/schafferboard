package edu.wpi.first.shuffleboard.plugin.base.widget;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

import edu.wpi.first.shuffleboard.api.util.ThreadUtils;
import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;
import edu.wpi.first.shuffleboard.plugin.base.data.TimerData;
import javafx.application.Platform;
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

    private final ScheduledExecutorService autoRunnerExecutor = ThreadUtils.newDaemonScheduledExecutorService();

    private Stopwatch stopwatch;


    @FXML
    private void initialize()
    {
        //titleLabel.textProperty().bindBidirectional(titleProperty());
        text.setTextAlignment(TextAlignment.RIGHT);
        stopwatch = Stopwatch.createStarted();
        autoRunnerExecutor.submit(() -> updateText());
        dataOrDefault.addListener((__, oldData, newData) -> {
            if (newData.getMode() != oldData.getMode() || newData.getDuration() != oldData.getDuration()) {
                stopwatch.reset();
                stopwatch.start();
            }
        });
    }

    private void updateText()
    {
        Platform.runLater(new Runnable(){
            @Override
            public void run()
            {
                //if (getSource().getData() == null)
                //    getSource().setData(TimerType.Instance.getDefaultValue());
                
                double timeLeft = dataOrDefault.get().getTimeLeft();
                if (timeLeft < 0)
                    timeLeft = 0;
                if ((int)Math.ceil(timeLeft) >= 60)
                    text.setText(String.format("%d:%02d", (int)(Math.ceil(timeLeft)) / 60, (int)(Math.ceil(timeLeft)) % 60));
                else text.setText(String.format("%d", (int)(Math.ceil(timeLeft))));
                
                text.setTextAlignment(TextAlignment.RIGHT);

                if (dataOrDefault.get().getMode() == 1L && !progressBar.getStyleClass().contains("auto-timer"))
                {
                    progressBar.getStyleClass().remove("teleop-timer");
                    progressBar.getStyleClass().add("auto-timer");
                }
                else if (dataOrDefault.get().getMode() != 1L && !progressBar.getStyleClass().contains("teleop-timer"))
                {
                    progressBar.getStyleClass().remove("auto-timer");
                    progressBar.getStyleClass().add("teleop-timer");
                }
                titleLabel.setText(dataOrDefault.get().getTitle());
                progressBar.setProgress(timeLeft / dataOrDefault.get().getDuration());
            }
        });
        autoRunnerExecutor.schedule(() -> updateText(), 16, TimeUnit.MILLISECONDS);
    }

    @Override
    public Pane getView() {
        return root;
    }  
}

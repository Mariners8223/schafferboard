package edu.wpi.first.shuffleboard.plugin.base.widget;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;

import edu.wpi.first.shuffleboard.api.prefs.Group;
import edu.wpi.first.shuffleboard.api.prefs.Setting;
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

    private BooleanProperty warnBelow30s = new SimpleBooleanProperty(this, "warnBelow30s", true);

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
        Platform.runLater(new Runnable()
        {
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

                switch ((int)dataOrDefault.get().getMode())
                {
                    case 1:
                        progressBar.getStyleClass().clear();
                        progressBar.getStyleClass().add("auto-timer");
                    default:
                    case 0:
                        progressBar.getStyleClass().clear();
                        if (timeLeft <= 30.0 && warnBelow30s.get())
                        {
                            progressBar.getStyleClass().add("teleop-timer-30s");
                        }
                        else
                        {
                            progressBar.getStyleClass().add("teleop-timer");
                        }
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
    @Override
    public List<Group> getSettings() {
        return ImmutableList.of(
            Group.of("Timer Settings",
                Setting.of("Warn below 30s", warnBelow30s, Boolean.class)
            )
        );
    }
}

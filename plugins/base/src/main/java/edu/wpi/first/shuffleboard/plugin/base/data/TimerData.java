package edu.wpi.first.shuffleboard.plugin.base.data;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.wpi.first.shuffleboard.api.data.ComplexData;

public class TimerData extends ComplexData<TimerData> {

    long mode;
    double timerDuration;

    public TimerData(int mode, double timerDuration)
    {
        this.mode = mode;
        this.timerDuration = timerDuration;
    }
    public TimerData(Map<String, Object> map)
    {
        Logger.getLogger(TimerData.class.getName()).log(Level.INFO, "MODE: " + map.get("mode"));
        Logger.getLogger(TimerData.class.getName()).log(Level.INFO, "TIMER DURATION: " + map.get("timerDuration"));
        this.mode = (long)(map.get("mode"));
        this.timerDuration = (double)(map.get("timerDuration"));
    }

    @Override
    public Map<String, Object> asMap() {
        return Map.of("mode", mode, "timerDuration", timerDuration);
    }
    
    public long getMode() { return mode; }
    public double getTimerDuration() { return timerDuration; }
}

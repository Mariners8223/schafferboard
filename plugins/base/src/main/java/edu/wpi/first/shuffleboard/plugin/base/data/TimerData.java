package edu.wpi.first.shuffleboard.plugin.base.data;

import java.time.LocalTime;
import java.util.Map;

import edu.wpi.first.shuffleboard.api.data.ComplexData;

public class TimerData extends ComplexData<TimerData> {

    long mode;
    double duration;
    long startTime;
    String displayTitle;

    public TimerData(int mode, double duration, String title)
    {
        this.mode = mode;
        this.duration = duration;
        this.startTime = LocalTime.now().toNanoOfDay();
        displayTitle = title;
    }
    public TimerData(Map<String, Object> map)
    {
        this.mode = (long)(map.get("mode"));
        this.duration = (double)(map.get("duration"));
        this.startTime = (long)(map.get("startTime"));
        this.displayTitle = (String)(map.get("displayTitle"));
    }

    @Override
    public Map<String, Object> asMap() {
        return Map.of("mode", mode, "duration", duration, "startTime", startTime);
    }
    
    public long getMode() { return mode; }
    public double getDuration() { return duration; }
    public long getTimerOffset() {
        return startTime;
    }
    public double getTimeLeft() {
        long nanoInDay = LocalTime.now().toNanoOfDay();
        // idk what you're doing testing robots/widgets at 12am but I ain't judging.
        if (nanoInDay < startTime)
            startTime -= 1000000000L * 86400L; // one billion times 86400 = how many nanoseconds in day
        
        return duration - ((nanoInDay - startTime) / 1000000000.0); // 1 billion nanoseconds in second
    }
    public String getTitle()
    {
        return displayTitle;
    }
}

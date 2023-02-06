package edu.wpi.first.shuffleboard.plugin.base.data.types;

import edu.wpi.first.shuffleboard.api.data.ComplexDataType;
import edu.wpi.first.shuffleboard.plugin.base.data.TimerData;

import java.util.Map;
import java.util.function.Function;

public final class TimerType extends ComplexDataType<TimerData> {
    public static final TimerType Instance = new TimerType();

    private TimerType() {
        super("Timer", TimerData.class);
    }

    @Override
    public Function<Map<String, Object>, TimerData> fromMap() {
        return TimerData::new;
    }

    @Override
    public TimerData getDefaultValue() {
        return new TimerData(-1, 75);
    }
}

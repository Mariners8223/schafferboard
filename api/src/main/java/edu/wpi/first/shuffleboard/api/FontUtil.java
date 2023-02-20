package edu.wpi.first.shuffleboard.api;

import javafx.scene.text.Font;

public class FontUtil {
    private static FontUtil instance;
    public Font spaceMono;
    public Font spaceMonoBold;
    
    private FontUtil()
    {
        spaceMono = Font.loadFont(FontUtil.class.getResourceAsStream("/edu/wpi/first/shuffleboard/api/SpaceMono-Regular.ttf"), 10);
        spaceMonoBold = Font.loadFont(FontUtil.class.getResourceAsStream("/edu/wpi/first/shuffleboard/api/SpaceMono-Bold.ttf"), 10);
    }
    public static FontUtil getInstance()
    {
        if (instance == null)
            instance = new FontUtil();
        
        return instance;
    }
}

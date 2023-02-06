package edu.wpi.first.shuffleboard.api;

import javafx.scene.text.Font;

public class FontUtil {
    private static FontUtil instance;
    public Font unbounded;
    
    private FontUtil()
    {
        unbounded = Font.loadFont(FontUtil.class.getResourceAsStream("/edu/wpi/first/shuffleboard/api/Unbounded-Regular.ttf"), 10);
        unbounded = Font.loadFont(FontUtil.class.getResourceAsStream("/edu/wpi/first/shuffleboard/api/Unbounded-Bold.ttf"), 10);
    }
    public static FontUtil getInstance()
    {
        if (instance == null)
            instance = new FontUtil();
        
        return instance;
    }
}

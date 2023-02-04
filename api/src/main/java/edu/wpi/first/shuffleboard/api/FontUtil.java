package edu.wpi.first.shuffleboard.api;

import javafx.scene.text.Font;

public class FontUtil {
    private static FontUtil instance;
    public Font unbounded;
    
    private FontUtil()
    {
        unbounded = Font.loadFont(FontUtil.class.getResourceAsStream("/edu/wpi/first/shuffleboard/api/Unbounded-Regular.ttf"), 10);
        System.out.println("UNBOUNDED LOADED: " + (unbounded == null));
    }
    public static FontUtil getInstance()
    {
        if (instance == null)
            instance = new FontUtil();
        
        return instance;
    }
}

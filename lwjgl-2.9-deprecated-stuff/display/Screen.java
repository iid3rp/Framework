package framework.display;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

import java.awt.Canvas;

public class Screen extends Canvas
{
    private int width;
    private int height;

    public Screen(int width, int height)
    {
        super();
        this.width = width;
        this.height = height;
        setVisible(true);
    }

    public void set()
    {
        DisplayManager.setSize(width, height);
        DisplayManager.createDisplay();
        try {
            Display.setParent(this);
        }
        catch(LWJGLException e) {
            throw new RuntimeException(e);
        }
    }
}

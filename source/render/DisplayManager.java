package render;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;

public class DisplayManager
{
    public static int width = 720;
    public static int height = 720;
    private static final int Frames = 60;
    public static void createDisplay()
    {
        ContextAttribs attributes = new ContextAttribs(3, 2);
        attributes = attributes.withForwardCompatible(true);
        attributes = attributes.withProfileCore(true);

        try {
            Display.setDisplayMode(new DisplayMode(width, height));
            Display.create(new PixelFormat(), attributes);
        }
        catch(LWJGLException e) {
            throw new RuntimeException(e);
        }

        GL11.glViewport(0, 0, width, height);
    }

    public static void updateDisplay()
    {
        Display.sync(Frames);
        Display.update();
    }

    public static void closeDisplay()
    {
        Display.destroy();
    }

    public static void setTitle(String title)
    {
        Display.setTitle(title);
    }

    public static void setSize(int width, int height)
    {
        DisplayManager.width = width;
        DisplayManager.height = height;
    }
}

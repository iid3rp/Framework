package render;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.*;

public class DisplayManager
{
    public static final int Width = 1280;
    public static final int Height = 720;
    private static final int Frames = 60;

    private static long lastTime;
    private static float delta;

    public static void createDisplay()
    {
        ContextAttribs attributes = new ContextAttribs(3, 2);
        attributes = attributes.withForwardCompatible(true);
        attributes = attributes.withProfileCore(true);

        try {
            Display.setDisplayMode(new DisplayMode(Width, Height));
            Display.create(new PixelFormat(), attributes);
            Display.setTitle("Dark Matter Suite");
        }
        catch(LWJGLException e) {
            throw new RuntimeException(e);
        }

        GL11.glViewport(0, 0, Width, Height);
        lastTime = getCurrentTime();
    }

    public static void updateDisplay()
    {
        Display.sync(Frames);
        Display.update();
        long currentTime = getCurrentTime();
        delta = (currentTime - lastTime) / 1000f;
        lastTime = currentTime;
    }

    public static float getFrameTimeSeconds()
    {
        return delta;
    }

    public static void closeDisplay()
    {
        Display.destroy();
    }

    private static long getCurrentTime()
    {
        return Sys.getTime() * 1000 / Sys.getTimerResolution();
    }
}

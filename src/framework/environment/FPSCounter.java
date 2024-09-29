package framework.environment;

import framework.toolkit.Display;

public class FPSCounter
{
    private static final double UPDATE_INTERVAL = 0.5;
    private static double totalTime = 0;
    private static int frameCount = 0;
    private static double fps = 0;
    private static int counter = 0;

    public static void update()
    {
        totalTime += Display.getDeltaInSeconds();
        frameCount++;

        if (totalTime >= UPDATE_INTERVAL)
        {
            fps = frameCount / totalTime;
            counter = (int) Math.round(fps);
            totalTime = 0;
            frameCount = 0;
        }
    }

    public static int getCounter()
    {
        return counter;
    }
}
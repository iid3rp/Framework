package framework.scripting;

import framework.Display.DisplayManager;
import framework.environment.Scene;

public class Count implements FrameworkScript
{
    private float i;
    private int seconds = 0;

    @Override
    public void run(Scene scene)
    {
        if(i >= 1000) {
            System.out.println("cursor location: " + scene.getEvent().getCurrentRay());
            i = 0; // Reset the counter after printing
        }
        i += (1000 * DisplayManager.getDeltaInSeconds());
    }

    @Override
    public boolean whilst()
    {
        return i != 100_000_000;
    }
}

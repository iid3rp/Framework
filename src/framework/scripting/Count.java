package framework.scripting;

import framework.environment.Scene;
import framework.toolkit.Display;

public class Count implements FrameworkScript
{
    private float i;
    public int seconds = 0;

    @Override
    public void run(Scene scene)
    {
        if(Display.getFps() == 0)
            seconds++;
    }

    @Override
    public boolean whilst()
    {
        return i != 100_000_000;
    }
}

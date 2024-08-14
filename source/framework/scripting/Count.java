package framework.scripting;

import framework.h.Display;
import framework.environment.Scene;

public class Count implements FrameworkScript
{
    private float i;
    public int seconds = 0;

    @Override
    public void run(Scene scene)
    {
        if(Display.getVolatileFPS() == 0)
            seconds++;
    }

    @Override
    public boolean whilst()
    {
        return i != 100_000_000;
    }
}

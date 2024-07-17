package framework.scripting;

import framework.environment.Environment;
import framework.environment.Scene;

/**
 * Represents a framework script that can be created with
 * custom logic to run on a separate thread within {@link Environment}
 * class.
 * This script can interact with the provided Scene object
 * to perform actions within the game.
 */
public interface FrameworkScript
{
    /**
     * This variable will determine if the instance of this {@link FrameworkScript}
     * object will stay after the calculation is done.
     * It has to initialize it with {@link #setStay(boolean flag) setStay(true)} to
     * make sure it will stay as it is.
     * Initialize it as {@link #setStay(boolean flag) setStay(false)}
     * when it is time to stop.
     * <p>
     * This is very useful when creating movement methods that need time and
     * a bit of calculation that needs to be iterated through the rendering
     * process.
     */
    boolean stay = false;

    void run(Scene scene);

    default boolean setStay(boolean flag)
    {
        return flag;
    }

    default boolean isStaying()
    {
        return stay;
    }
}

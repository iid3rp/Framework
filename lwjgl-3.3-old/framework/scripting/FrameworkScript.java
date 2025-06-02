package framework.scripting;

import framework.environment.Environment;
import framework.environment.Scene;

/**
 * Represents a framework script that can be created with custom logic to run on the main thread
 * within the {@link Environment} class while maintaining the
 * OpenGL contexts in a much more efficient way by running the whole thing once and run again
 * every single frame of the game loop when {@link #whilst()} function is still returning true rather
 * than using a thread with a {@code for() / while()} loop inside.
 * This script can interact with the provided {@link Scene} object
 * to perform actions within the game.
 * <p>
 * The {@code FrameworkScript} interface is designed to allow custom scripting within a game framework.
 * Implementations of this interface can define specific behavior that will be executed in the context of a scene.
 * </p>
 */
public interface FrameworkScript {

    /**
     * Executes the custom logic defined by the implementing class.
     * This method will be called with the {@link Scene} object to
     * perform actions within the game.
     *
     * @param scene the {@code Scene} object within which the script will run
     */
    void run(Scene scene);

    /**
     * Sets the stay flag to determine whether this script instance should remain active.
     */
    default boolean whilst()
    {
        return false;
    }
}

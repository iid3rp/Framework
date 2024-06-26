package environment;

/**
 Represents a framework script that can be created with
 custom logic to run on a separate thread within a game environment.
 This script can interact with the provided Scene object
 to perform actions within the game.
 */

public interface FrameworkScript
{
    public void run(Scene scene);
}

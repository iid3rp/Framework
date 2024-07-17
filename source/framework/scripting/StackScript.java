package framework.scripting;

import framework.environment.Environment;
import framework.environment.Scene;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * The {@code StackScript} class creates a stack of interfaces that are meant to be queued,
 * distinguishing itself from traditional multithreading approaches using the {@link Thread} class.
 * <p>
 * This design adheres to the philosophy of having OpenGL contexts run on a single thread.
 * This approach ensures robust and secure threading by managing contexts such as shaders,
 * OpenGL capabilities, and more. The OpenGL contexts are intended to run within the {@code main()}
 * method, maintaining the same threading as the compilation of shaders and other components.
 * <p>
 * <p>
 * <h3><a>What if I just create a thread with the scene as a parameter?</a></h3>
 * <p>
 * It is important to note that it is technically possible to run this method and use the
 * {@link Scene} object using the {@link Thread} class, but most of them are
 * part of the OpenGL context that should be only running in the {@code main()} method,
 * Attempting to interfere the contexts will result in runtime exceptions.
 * One of them being like this
 * <pre>{@code
 * FATAL ERROR in native method: Thread[#<number>,Thread-main]:
 * No context is current or a function that is not available in the current context was called.
 * The JVM will abort execution.
 * }</pre>
 * <p>
 * This class is used by the {@link Environment} class and will be added to a queue for execution
 * when appropriate. The class implements {@link Iterable}, allowing it to be used in enhanced for loops
 * for improved code readability.
 * <p>
 * The {@code StackScript} class provides the following key functionalities:
 * <ul>
 *   <li>{@code run(FrameworkScript s)}: Adds a {@link FrameworkScript} to the list of scripts to be run.</li>
 *   <li>{@code iterator()}: Returns an iterator over the elements in the list of scripts, allowing iteration
 *   using an enhanced for loop.</li>
 * </ul>
 * The stack operates in a FIFO (First-In-First-Out) manner, ensuring that the most recently added item
 * is processed last.
 * <p>
 * Example usage:
 * <pre>{@code
 *
 * // environment class
 * StackScript stackScript = new StackScript();
 *
 * // run a script at any class
 * Environment.run(scene -> {
 *      [...]
 * });
 *
 * // game loop:
 * FrameworkScript reference = null;
 * for(FrameworkScript script : stack) {
 *     script.run(scene);
 *     if(!script.whilst())
 *         reference = script;
 * }
 * if(reference != null)
 *     stack.remove(reference);
 * }</pre>
 * <p>
 * The {@code StackScript} class is designed to be flexible and easily integrable with other components
 * of the system, providing a reliable way to manage and queue interfaces within an OpenGL context.
 *
 * @see Scene
 * @see Environment
 *
 * @author Francis Madanlo
 */
public class StackScript implements Iterable<FrameworkScript>
{
    /**
     * This will be the storage of the stack (scripts) that will
     * be processed in the while loop of the {@link Environment} class.
     */
    public ArrayList<FrameworkScript> scripts = new ArrayList<>();

    /**
     * Adds a {@link FrameworkScript} to the list of scripts to be run.
     *
     * @param s the {@code FrameworkScript} to be added
     * @return {@code true} if the script was added successfully, {@code false} otherwise
     */
    public boolean run(FrameworkScript s)
    {
        return scripts.add(s);
    }

    public boolean remove(FrameworkScript s)
    {
        return scripts.remove(s);
    }

    /**
     * Returns an iterator over the elements in the list of scripts.
     *
     * @return an {@code Iterator<FrameworkScript>} over the elements in the list of scripts
     */
    @Override
    public Iterator<FrameworkScript> iterator()
    {
        return scripts.iterator();
    }
}


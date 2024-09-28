package toolkit;

import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.opengl.GL46.*;

public final class Display implements Hardware
{
    private static final int DEFAULT_WINDOW_WIDTH = 1280;
    private static final int DEFAULT_WINDOW_HEIGHT = 720;
    private static final int DEFAULT_REFRESH_RATE = 120;
    private static final String TITLE = "";
    private static int width;
    private static int height;
    private static int refreshRate;
    private static int title;
    private static long window;
    private static int frames;
    private static int currentFPSCount;
    private static boolean showFPSTitle;
    private static long time;
    private static double lastFrameTime;
    private static double deltaInSeconds;
    private static Keyboard keyboard;
    private static Mouse mouse;

    // Hide the constructor
    private Display() {}

    public static void createDisplay(int width, int height, String title, int refreshRate)
    {
        if (!glfwInit()) {
            throw new RuntimeException("ERROR: GLFW wasn't initialized");
        }

        glfwWindowHint(GLFW_DEPTH_BITS, 24); // Set the depth bits to 24
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_REFRESH_RATE, refreshRate);
        glfwWindowHint(GLFW_DECORATED,
                GL_TRUE);

        window = glfwCreateWindow(width, height, title, 0, 0);

        // entry #1
        if (window == 0)
            throw new RuntimeException("Failed to create window");

        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        // entry #2
        assert vidMode != null;
        glfwSetWindowPos(window, (vidMode.width() - DEFAULT_WINDOW_WIDTH) / 2, (vidMode.height() - DEFAULT_WINDOW_HEIGHT) / 2);

        // initialize the hardware capabilities
        keyboard = new Keyboard();
        mouse = new Mouse();

        // register keyboard input callback
        glfwSetKeyCallback(window, keyboard);

        // and the mouse too
        glfwSetCursorPosCallback(window, mouse.getMouseMoveCallback());
        glfwSetMouseButtonCallback(window, mouse.getMouseButtonsCallback());
        glfwSetScrollCallback(window, mouse.getMouseScrollCallback());

        // the window information
        glfwMakeContextCurrent(window);
        createCapabilities();
        glfwShowWindow(window);

        // Setting the value to 1 should limit to 60 FPS
        glfwSwapInterval(0);

        // the timeframes
        lastFrameTime = getCurrentTime();
    }

    public static void createDisplay()
    {
        if (!glfwInit()) {
            throw new RuntimeException("ERROR: GLFW wasn't initialized");
        }

        glfwWindowHint(GLFW_DEPTH_BITS, 24); // Set the depth bits to 24
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_REFRESH_RATE, DEFAULT_REFRESH_RATE);
        glfwWindowHint(GLFW_DECORATED,
                GL_TRUE);

        window = glfwCreateWindow(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT, TITLE, 0, 0);

        // entry #1
        if (window == 0)
            throw new RuntimeException("Failed to create window");

        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        // entry #2
        assert vidMode != null;
        glfwSetWindowPos(window, (vidMode.width() - DEFAULT_WINDOW_WIDTH) / 2, (vidMode.height() - DEFAULT_WINDOW_HEIGHT) / 2);

        // initialize the hardware capabilities
        keyboard = new Keyboard();
        mouse = new Mouse();

        // register keyboard input callback
        glfwSetKeyCallback(window, keyboard);

        // and the mouse too
        glfwSetCursorPosCallback(window, mouse.getMouseMoveCallback());
        glfwSetMouseButtonCallback(window, mouse.getMouseButtonsCallback());
        glfwSetScrollCallback(window, mouse.getMouseScrollCallback());

        // the window information
        glfwMakeContextCurrent(window);
        createCapabilities();
        glfwShowWindow(window);

        // Setting the value to 1 should limit to 60 FPS
        glfwSwapInterval(0);

        // the timeframes
        lastFrameTime = getCurrentTime();
    }

    public static void updateDisplay()
    {
        Mouse.mouseMoved = false;
        Mouse.mouseScrolled = false;
        Mouse.mouseDragged = false;
        glfwPollEvents();
        glfwSwapBuffers(window);

        if (showFPSTitle) {
            frames++;

            if (System.currentTimeMillis() > time + 1000) {
                glfwSetWindowTitle(window, TITLE + " | FPS: " + frames);
                time = System.currentTimeMillis();
                currentFPSCount = frames;
                frames = 0;
            }
        }

        double currentFrameTime = getCurrentTime();
        deltaInSeconds = (currentFrameTime - lastFrameTime) / 1000;
        lastFrameTime = currentFrameTime;
    }

    public static void closeDisplay() {
        mouse.destroy();
        keyboard.close();
        glfwWindowShouldClose(window);
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    public static boolean shouldDisplayClose() {
        return !glfwWindowShouldClose(window);
    }

    public static String getOpenGlVersionMessage() {
        return glGetString(GL_VERSION);
    }

    public static String getLwjglVersionMessage() { return org.lwjgl.Version.getVersion(); }

    public static void setShowFPSTitle(boolean showFPSTitle)
    {
        Display.showFPSTitle = showFPSTitle;

        if (!showFPSTitle) {
            frames = 0;
            time = 0;
        }
    }

    public static int getFps()
    {
        return frames;
    }

    public static int getCurrentFPSCount()
    {
        return currentFPSCount;
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static float getDeltaInSeconds() {
        return (float) deltaInSeconds;
    }

    private static double getCurrentTime() {
        return glfwGetTime() * 1000;
    }

    public static void setWidth(int width)
    {
        Display.width = width;
        glfwSetWindowSize(window, width, height);
    }

    public static void setHeight(int height)
    {
        Display.height = height;
        glfwSetWindowSize(window, width, height);
    }

    public static void setTitle(int title)
    {
        Display.title = title;
    }
}

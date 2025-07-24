package framework.environment;

import framework.entity.Camera;
import framework.hardware.Display;
import framework.loader.ModelLoader;
import framework.main.Main;
import framework.renderer.MasterRenderer;
import framework.shader.EntityShader;
import org.lwjgl.opengl.GL20;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.GL_TRUE;

public final class Engine
{
    public static ExecutorService background;
    public static ExecutorService physics;
    private static Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();
    public static Scene scene;
    public static Camera test;

    public static void setCamera(Camera camera)
    {
        Engine.test = camera;
    }

    static class NamedThreadFactory implements ThreadFactory
    {
        private final String namePrefix;
        private final AtomicInteger nextId = new AtomicInteger(1);

        public NamedThreadFactory(String poolName) {
            this.namePrefix = poolName + "-Thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, namePrefix + nextId.getAndIncrement());
            thread.setDaemon(true);
            return thread;
        }
    }

    public static Future<?> backgroundExecute(Runnable task)
    {
        return background.submit(task);
    }

    public static void start()
    {
        //glfwWindowHint(GLFW_VISIBLE, GL_TRUE);
        loop();
    }

    public static void createThreads()
    {
        int corePoolSize = Math.max(1, Runtime.getRuntime().availableProcessors() - 1); // e.g., N-1 cores for calculations
        int maximumPoolSize = Runtime.getRuntime().availableProcessors() * 2; // Allow some burst capacity
        long keepAliveTime = 36000L; // Seconds
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(100_000);

        background = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue,
                new NamedThreadFactory("calc"),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        System.out.println("Environment started. Loop commencing.");
        System.out.println("MatrixCalcExecutor configured: Core=" + corePoolSize + ", Max=" + maximumPoolSize + ", QueueSize=" + workQueue.remainingCapacity());
    }

    public static void loop()
    {
        MasterRenderer.setRenderer();
        while(Display.shouldDisplayClose())
        {
            runAllScripts();
            test.move();

            MasterRenderer.prepare();
            EntityShader.bind();
            MasterRenderer.render(Main.ent);
            EntityShader.unbind();

            Display.updateDisplay();
        }
        exit();
    }

    private static void runAllScripts()
    {
        if(!tasks.isEmpty())
            tasks.remove().run();
    }


    public static void setScene(Scene scene)
    {
        Engine.scene = scene;
    }

    public static void mainExecute(Runnable task)
    {
        tasks.add(task);
    }

    public static Scene getScene()
    {
        return scene;
    }

    private static void exit()
    {
        shutdownThreads();
        EntityShader.destroy();
        ModelLoader.destroy();
        Display.closeDisplay();
    }

    private static void shutdownThreads()
    {
        background.shutdown();
        try {
            if (!background.awaitTermination(10, TimeUnit.MILLISECONDS))
                background.shutdownNow();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

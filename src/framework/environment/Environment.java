package framework.environment;

import framework.hardware.Display;
import framework.loader.ModelLoader;
import framework.main.Main;
import framework.renderer.MasterRenderer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class Environment
{
    public static ExecutorService calc;
    public static ExecutorService physics;
    public static Scene scene;

    // Custom ThreadFactory for better debugging
    static class NamedThreadFactory implements ThreadFactory
    {
        private final String namePrefix;
        private final java.util.concurrent.atomic.AtomicInteger nextId = new java.util.concurrent.atomic.AtomicInteger(1);

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

    /**
     * This is the method you asked for!
     * It takes a Runnable interface (your calculation logic) and runs it in a different thread.
     *
     * @param task The Runnable representing the calculation to be performed.
     */
    public static void calculate(Runnable task)
    {
        calc.submit(task);
    }

    public static Future<?> calculate(Future<Object> method)
    {
        return null;
    }

    public static void start()
    {
        createThreads();
        simulateThread();
        loop();
    }

    private static void simulateThread()
    {
        for(int i = 0; i < 200; i++)
        {
            Environment.calculate(() ->
            {
                System.out.println("hello world!");
                try {
                    Thread.currentThread().sleep(2000);
                }
                catch(InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private static void createThreads()
    {
        int corePoolSize = Math.max(1, Runtime.getRuntime().availableProcessors() - 1); // e.g., N-1 cores for calculations
        int maximumPoolSize = Runtime.getRuntime().availableProcessors() * 2; // Allow some burst capacity
        long keepAliveTime = 36000L; // Seconds
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(100_000);

        calc = new ThreadPoolExecutor(
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
        while(Display.shouldDisplayClose())
        {
            MasterRenderer.prepare();
            MasterRenderer.render(Main.model);
            Display.updateDisplay();
        }
        exit();
    }

    private static void runAllScripts()
    {

    }


    public static void setScene(Scene scene)
    {
        Environment.scene = scene;
    }

    public static Scene getScene()
    {
        return scene;
    }

    private static void exit()
    {
        shutdownThreads();
        ModelLoader.destroy();
        Display.closeDisplay();
    }

    private static void shutdownThreads()
    {
        calc.shutdown();
        try {
            if (!calc.awaitTermination(10, TimeUnit.MILLISECONDS)) {
                System.err.println("CalculationUpdaterExecutor did not terminate in time. Forcing shutdown.");
                calc.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

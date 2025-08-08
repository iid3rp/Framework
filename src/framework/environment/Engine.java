package framework.environment;

import framework.hardware.Display;
import framework.loader.ModelLoader;
import framework.main.Main;
import framework.renderer.MasterRenderer;
import framework.shader.GLShader;
import org.jetbrains.annotations.NotNull;

import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public final class Engine {
    // Add these constants to the top of your Engine class
    private static final int PHYSICS_TICKS_PER_SECOND = 1000;
    private static final double PHYSICS_TICK_TIME_NANOS = 1_000_000_000.0 / PHYSICS_TICKS_PER_SECOND;
    public static final float PHYSICS_DELTA_TIME = 1.0f / PHYSICS_TICKS_PER_SECOND;

    private static ExecutorService background;
    private static final BlockingQueue<Runnable> physicsTasks = new LinkedBlockingQueue<>();
    private static Thread physicsThread;
    private static Queue<Runnable> mainTasks = new ConcurrentLinkedQueue<>();
    private static Scene scene;

    static class NamedThreadFactory implements ThreadFactory {
        private final String namePrefix;
        private final AtomicInteger nextId = new AtomicInteger(1);

        public NamedThreadFactory(String poolName) {
            this.namePrefix = poolName + "-Thread-";
        }

        @Override
        public Thread newThread(@NotNull Runnable r) {
            Thread thread = new Thread(r, namePrefix + nextId.getAndIncrement());
            thread.setDaemon(true);
            return thread;
        }
    }

    public static Future<?> backgroundExecute(Runnable task) {
        return background.submit(task);
    }

    public static void start() {
        loop();
    }

    public static void createThreads() {
        int corePoolSize = Math.max(1, Runtime.getRuntime().availableProcessors() - 1);
        int maximumPoolSize = Runtime.getRuntime().availableProcessors() * 2;
        long keepAliveTime = 36000L;
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

        startPhysicsLoop();

        System.out.println("Environment started. Loop commencing.");
        System.out.println("MatrixCalcExecutor configured: Core=" + corePoolSize + ", Max=" + maximumPoolSize + ", QueueSize=" + workQueue.remainingCapacity());
    }

    private static void startPhysicsLoop()
    {
        physicsThread = new Thread(() ->
        {
            long lastTickTime = System.nanoTime();
            double accumulator = 0;

            while (Display.shouldDisplayClose())
            {
                long currentTime = System.nanoTime();
                // Add the real time that has passed to an accumulator
                accumulator += (currentTime - lastTickTime);
                lastTickTime = currentTime;

                if (accumulator > PHYSICS_TICK_TIME_NANOS * 10) {
                    accumulator = PHYSICS_TICK_TIME_NANOS * 10;
                }

                while (accumulator >= PHYSICS_TICK_TIME_NANOS) {
                    // --- RUN ONE PHYSICS TICK ---
                    Runnable task;
                    while ((task = physicsTasks.poll()) != null) {
                        task.run();
                    }

                    if (scene != null) {
                        scene.moveCamera(PHYSICS_DELTA_TIME);
                    }
                    // --- END OF TICK ---
                    accumulator -= PHYSICS_TICK_TIME_NANOS;
                }
            }
        }, "Physics-Loop-Thread");

        physicsThread.setDaemon(true);
        physicsThread.start();
    }

    public static void physicsExecute(Runnable task) {
        physicsTasks.offer(task);
    }

    public static void loop() {
        MasterRenderer.setRenderer();
        while (Display.shouldDisplayClose())
        {
            runAllScripts();
            MasterRenderer.prepare();
            Main.ent.transformRotation(0, 0.03f, 0);
            MasterRenderer.render(scene);

            Display.updateDisplay();
        }
        exit();
    }

    private static void runAllScripts() {
        Runnable task;
        while ((task = mainTasks.poll()) != null) {
            task.run();
        }
    }

    public static void setScene(Scene scene) {
        Engine.scene = scene;
    }

    public static void mainExecute(Runnable task) {
        mainTasks.add(task);
    }

    public static Scene getScene() {
        return scene;
    }

    private static void exit() {
        shutdownThreads();
        GLShader.destroy();
        ModelLoader.destroy();
        Display.closeDisplay();
    }

    private static void shutdownThreads() {
        background.shutdown();
        try {
            if (!background.awaitTermination(10, TimeUnit.MILLISECONDS))
                background.shutdownNow();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void log(Object... args)
    {
        StringBuilder sb = new StringBuilder();
        for(Object o : args)
            sb.append(o).append(" ");
        System.out.println(sb);

    }
}

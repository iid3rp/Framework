package framework.environment;

import framework.renderer.MasterRenderer;
import framework.loader.ModelLoader;
import framework.event.MouseEvent;
import framework.scripting.FrameworkScript;
import framework.scripting.StackScript;
import framework.toolkit.Display;
import org.joml.Vector4f;

// the commented codes will be uncommented once the game is set up!
public final class Environment
{
    public static Scene scene;
    private static final StackScript stack = new StackScript();
//    public static FrameBufferObject multi = new FrameBufferObject(Display.getWidth(), Display.getHeight());
//    public static FrameBufferObject out = new FrameBufferObject(Display.getWidth(), Display.getHeight(), FrameBufferObject.DEPTH_TEXTURE);
//    public static FrameBufferObject bright = new FrameBufferObject(Display.getWidth(), Display.getHeight(), FrameBufferObject.DEPTH_TEXTURE);
//    public static FrameBufferObject mouseEventBuffer = new FrameBufferObject(Display.getWidth(), Display.getHeight(), FrameBufferObject.DEPTH_TEXTURE);
//    public static FrameBufferObject pixelBuffer = new FrameBufferObject(2, 2, FrameBufferObject.DEPTH_TEXTURE);

    public static void start()
    {
        // static method calling goes here:
        MasterRenderer.setRenderer(scene.getCamera());

        //PostProcessing.initialize();
        //ParticleMaster.initialize(MasterRenderer.getProjectionMatrix());

        if(scene != null)
        {
            MouseEvent event = new MouseEvent();
            event.setCamera(getScene().getCamera());
            event.setProjection(MasterRenderer.getProjectionMatrix());
            getScene().setEvent(event);


            loop();
            exit();
        }
        else throw new NullPointerException("no scene applied in the render.");
    }

    public static void loop()
    {
        long i = 0;

        while(Display.shouldDisplayClose())
        {
            // mouseEvent stuff
            scene.getEvent().update();

            //the shadow thingies


            // the player and the camera movements
            scene.getPlayer().move(scene.getTerrain());
            scene.getCamera().move();


            renderScene(new Vector4f(0, -1, 0, 1000000));


            runAllScripts();
            Display.updateDisplay();
        }
    }

    private static void runAllScripts()
    {
        FrameworkScript reference = null;
        for(FrameworkScript script : stack)
        {
            script.run(scene);
            if(!script.whilst())
                reference = script;
        }
        if(reference != null) {
            boolean x = stack.remove(reference);
            System.out.println(x);
        }
    }

    private static void renderScene(Vector4f vec4)
    {
        // the 3D space stuff...
        // the shadow thingies
        MasterRenderer.processTerrain(scene.getTerrain());
        MasterRenderer.processAllEntities(scene.getEntities());
        MasterRenderer.render(scene.getLights(), scene.getCamera(), vec4);
    }

    public static void run(FrameworkScript script)
    {
        stack.run(script);
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
        MasterRenderer.dispose();
        ModelLoader.destroy();
        Display.closeDisplay();
    }
}

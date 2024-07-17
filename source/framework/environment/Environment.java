package framework.environment;

import framework.Display.DisplayManager;
import framework.renderer.MasterRenderer;
import framework.loader.ModelLoader;
import framework.event.MouseEvent;
import framework.scripting.FrameworkScript;
import framework.scripting.StackScript;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

// the commented codes will be uncommented once the game is set up!
public final class Environment
{
    public static Scene scene;
    private static StackScript stack = new StackScript();

    // will be used in other tutorials in the future, don't worry!
    //public static FrameBufferObject multisample = new FrameBufferObject(Display.getWidth(), Display.getHeight());
    //public static FrameBufferObject out = new FrameBufferObject(Display.getWidth(), Display.getHeight(), FrameBufferObject.DEPTH_TEXTURE);
    //public static FrameBufferObject bright = new FrameBufferObject(Display.getWidth(), Display.getHeight(), FrameBufferObject.DEPTH_TEXTURE);

    public static void setScene(Scene scene)
    {
        Environment.scene = scene;
    }

    // intentional global-local variables
    //static FontType font;
    //static GUIText fps;

    public static void start()
    {
        // static method calling goes here:
        MasterRenderer.setRenderer();
        //PostProcessing.initialize();
        //TextMasterRenderer.initialize();
        //ParticleMaster.initialize(MasterRenderer.getProjectionMatrix());

        if(scene != null)
        {
            MouseEvent event = new MouseEvent();
            event.setCamera(getScene().getCamera());
            event.setProjection(MasterRenderer.getProjectionMatrix());
            getScene().setEvent(event);
            //font = new FontType(ModelLoader.loadTexture("comic"), "comic");
            //fps = new GUIText("fps count: 0", 1, font, new Vector2f(0, 0), 1f, false);
            //fps.setColor(1, 1, 0);

            loop();
            exit();
        }
        else throw new NullPointerException();
    }

    public static void loop()
    {
        while(DisplayManager.shouldDisplayClose())
        {
            //FPSCounter.update();
            // mouseEvent stuff
            scene.getEvent().update();

            // the shadow thingies

            //particle
            //if(scene.getParticleSystem() != null) {
            //    ParticleMaster.update(scene.getCamera());
            //    scene.getParticleSystem().generateParticles(scene.getPlayer().getPosition());
            //}
            //ParticleMaster.renderParticles(scene.getCamera());

            // debuggers
            //System.out.println(scene.getEvent().getCurrentTerrainPoint());
            //System.out.println(scene.getEvent().currentRay);


            // the player and the camera movements
            scene.getPlayer().move(scene.getTerrain());
            scene.getCamera().move();


            // frame buffers thingy:
            // apparently, the rendering process in this stuff is that,
            // the first 3 clips [0, 1, 2] have certain black spots
            // in their skyboxes, but now it is gone in the
            // fourth index, finally.
            // took me a day to fix that :sob:
//            GL11.glEnable(GL30.GL_CLIP_DISTANCE3);
//
//            // the reflection of the water
            MasterRenderer.buffer.bindReflectionFrameBuffer();
            float distance = 2 * (scene.getCamera().getPosition().y - 0);
            scene.getCamera().getPosition().y -= distance;
            scene.getCamera().invertPitch();
            renderScene();
            scene.getCamera().getPosition().y += distance;
            scene.getCamera().invertPitch();
//
//            // the refraction of the water
//            MasterRenderer.waterFrameBufferObject.bindRefractionFrameBuffer();
//            renderScene();
//
//            GL11.glDisable(GL30.GL_CLIP_DISTANCE3);
            MasterRenderer.buffer.unbindCurrentFrameBuffer();


            //MasterRenderer.renderShadowMap(scene.getEntities(), scene.getMainLight());
            // frame buffer stuff
            //multisample.bindFrameBuffer();
            renderScene();
            MasterRenderer.renderWaters(scene.getWaters(), scene.getCamera(), scene.getMainLight());
            //multisample.unbindFrameBuffer();
            //multisample.resolveToFrameBufferObject(GL30.GL_COLOR_ATTACHMENT0,out);
            //multisample.resolveToFrameBufferObject(GL30.GL_COLOR_ATTACHMENT1,bright);
            //PostProcessing.doPostProcessing(out.getColorTexture(), bright.getColorTexture());

            scene.getContentPane().render(scene.getContentPane().getComponents());
            //TextMasterRenderer.setText(fps, "fps count: " + FPSCounter.getCounter());
            //TextMasterRenderer.render();

            runAllScripts();
            DisplayManager.updateDisplay();
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

    private static void renderScene()
    {
        // the 3D space stuff...
            // the shadow thingies
        MasterRenderer.processTerrain(scene.getTerrain());
        MasterRenderer.processAllEntities(scene.getEntities());
        //MasterRenderer.processAllNormalMappedEntities(scene.getNormalMappedEntities());
        MasterRenderer.render(scene.getLights(), scene.getCamera());
    }

    public static void run(FrameworkScript script)
    {
        stack.run(script);
    }

    public static Scene getScene()
    {
        return scene;
    }

    private static void exit()
    {
        //PostProcessing.dispose();
        //multisample.dispose();
        //out.dispose();
        //bright.dispose();
        //ParticleMaster.dispose();
        //TextMasterRenderer.dispose();
        scene.getContentPane().dispose();
        MasterRenderer.destroy();
        ModelLoader.destroy();
        DisplayManager.closeDisplay();
    }
}

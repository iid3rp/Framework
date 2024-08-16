package framework.environment;

import framework.Scratch;
import framework.fontExperiment.Char;
import framework.fontExperiment.Font;
import framework.fontExperiment.FontFile;
import framework.fontExperiment.Text;
import framework.fontExperiment.TextEntityRenderer;
import framework.h.Display;
import framework.font.FontType;
import framework.font.GUIText;
import framework.particles.ParticleMaster;
import framework.post_processing.FrameBufferObject;
import framework.post_processing.PostProcessing;
import framework.renderer.MasterRenderer;
import framework.loader.ModelLoader;
import framework.event.MouseEvent;
import framework.scripting.Count;
import framework.scripting.FrameworkScript;
import framework.scripting.StackScript;
import framework.swing.PictureBox;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL30;

import java.util.List;

// the commented codes will be uncommented once the game is set up!
public final class Environment
{
    public static Scene scene;
    private static TextEntityRenderer textEntityRenderer = new TextEntityRenderer();
    private static StackScript stack = new StackScript();
    public static FrameBufferObject multi = new FrameBufferObject(Display.getWidth(), Display.getHeight());
    public static FrameBufferObject out = new FrameBufferObject(Display.getWidth(), Display.getHeight(), FrameBufferObject.DEPTH_TEXTURE);
    public static FrameBufferObject bright = new FrameBufferObject(Display.getWidth(), Display.getHeight(), FrameBufferObject.DEPTH_TEXTURE);
    public static FrameBufferObject mouseEventBuffer = new FrameBufferObject(Display.getWidth(), Display.getHeight(), FrameBufferObject.DEPTH_TEXTURE);
    public static FrameBufferObject pixelBuffer = new FrameBufferObject(2, 2, FrameBufferObject.DEPTH_TEXTURE);

    // intentional global-local variables

    static FontType font;
    static GUIText fps;
    public static void start()
    {
        // static method calling goes here:
        MasterRenderer.setRenderer(scene.getCamera());
        PostProcessing.initialize();
        //TextMasterRenderer.initialize();;
        ParticleMaster.initialize(MasterRenderer.getProjectionMatrix());

        if(scene != null)
        {
            MouseEvent event = new MouseEvent();
            event.setCamera(getScene().getCamera());
            event.setProjection(MasterRenderer.getProjectionMatrix());
            getScene().setEvent(event);
            font = new FontType(ModelLoader.loadTexture("comic.png"), "comic");
            fps = new GUIText("fps count: 0", 2, font, new Vector2f(.05f, .05f), 1f, false);
            fps.setColor(1, 1, 0);
            fps.setSize(120, 100);
            //scene.getContentPane().add(fps);
            //scene.getContentPane().add(img);


            //
            // these are just example of implementations for future debugging...
            //
            PictureBox pb = new PictureBox();
            pb.setTexture(ModelLoader.loadTexture("brat.png"));
            pb.setLocation(20, 20);
            pb.setImageLocation(-100,  -100);
            pb.setScale(500, 500);
            pb.setSize(400, 400);
            //scene.getContentPane().add(pb);


            loop();
            exit();
        }
        else throw new NullPointerException("no scene applied in the render.");
    }

    public static void loop()
    {
        long i = 0;

        // example implementation...
        Font x = FontFile.readFont("comic");
        System.out.println(x);
        List<Char> chars = x.getCharacters();
        Text text = new Text();
        text.setText(Scratch.guess.toUpperCase());
        text.setMaxWidth(1000);
        text.setFontSize(30);
        text.setSize(200, 500);
        text.setAlignment(Text.LEFT);

        for(Char c : chars) {
            System.out.println(c);
        }

        Count count = new Count();
        run(count);

        while(Display.shouldDisplayClose())
        {
            //FPSCounter.update();
            // mouseEvent stuff
            scene.getEvent().update();

            //the shadow thingies
            //MasterRenderer.renderShadowMap(scene.getEntities(), scene.getMainLight());

            //particle

            if(scene.getParticleSystem() != null) {
                Vector3f pos = new Vector3f(scene.getPlayer().getPosition());
                pos.y += 300;
                scene.getParticleSystem().generateParticles(pos);
                ParticleMaster.update(scene.getCamera());
            }


            //if(scene.getParticleSystem() != null) {
            //}
            //ParticleMaster.renderParticles(scene.getCamera());

            // debuggers
            //System.out.println(scene.getEvent().getCurrentTerrainPoint());
            //System.out.println(scene.getEvent().currentRay);


            // the player and the camera movements
            scene.getPlayer().move(scene.getTerrain());
            scene.getCamera().move();


            // frame buffers thingy:
//            GL11.glEnable(GL30.GL_CLIP_DISTANCE1);
//
//            // the reflection of the water
//            MasterRenderer.buffer.bindReflectionFrameBuffer();
//            float distance = 2 * (scene.getCamera().getPosition().y - 0);
//            scene.getCamera().getPosition().y -= distance;
//            scene.getCamera().invertPitch();
//            renderScene(new Vector4f(0, 1, 0, 0));
////
//            //the refraction of the water
//            scene.getCamera().getPosition().y += distance;
//            scene.getCamera().invertPitch();
//            MasterRenderer.buffer.bindRefractionFrameBuffer();
//            renderScene(new Vector4f(0, -1, 0, 0));
////
//            MasterRenderer.buffer.unbindCurrentFrameBuffer();
            //GL11.glDisable(GL30.GL_CLIP_DISTANCE1);


            //MasterRenderer.renderShadowMap(scene.getEntities(), scene.getMainLight());
            // frame buffer stuff
            multi.bindFrameBuffer();
            renderScene(new Vector4f(0, -1, 0, 1000000));
            //MasterRenderer.renderWaters(scene.getWaters(), scene.getCamera(), scene.getMainLight());
            if(scene.getParticleSystem() != null)
                ParticleMaster.renderParticles(scene.getCamera());
            multi.unbindFrameBuffer();

            //multi.resolveToScreen();
            multi.resolveToFrameBufferObject(GL30.GL_COLOR_ATTACHMENT0, out);
            multi.resolveToFrameBufferObject(GL30.GL_COLOR_ATTACHMENT1, bright);
            multi.resolveToFrameBufferObject(GL30.GL_COLOR_ATTACHMENT2, mouseEventBuffer);
            multi.resolveToScreen();
            //multi.resolvePixel(mouseEventBuffer);
            if(i % 2 == 0)
                scene.getEvent().resolveColorPickFromPixel(mouseEventBuffer, pixelBuffer);
            i++;

            //if(!Keyboard.isKeyDown(Keyboard.E))
            PostProcessing.doPostProcessing(out.getColorTexture(), bright.getColorTexture());

            textEntityRenderer.render(x, text);
            scene.getContentPane().render(scene.getContentPane().getComponents());
            //text.setText("seconds: " + count.seconds);
            //TextMasterRenderer.render();
            runAllScripts();
            System.out.println(Display.getDeltaInSeconds());
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
        //pixel.dispose();
        PostProcessing.dispose();
        multi.dispose();
        out.dispose();
        mouseEventBuffer.dispose();
        bright.dispose();
        ParticleMaster.dispose();
        //TextMasterRenderer.dispose();
        scene.getContentPane().dispose();
        MasterRenderer.dispose();
        ModelLoader.destroy();
        Display.closeDisplay();
    }
}

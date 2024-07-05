package environment;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMasterRenderer;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;
import particles.ParticleMaster;
import render.DisplayManager;
import render.MasterRenderer;
import render.ModelLoader;

import java.io.File;

public final class Environment
{
    public static Scene scene;

    public static void setScene(Scene scene)
    {
        Environment.scene = scene;
    }

    public static void start()
    {
        // static method calling goes here:
        MasterRenderer.setRenderer();
        TextMasterRenderer.initialize();
        ParticleMaster.initialize(MasterRenderer.getProjectionMatrix());

        if(scene != null)
        {
            if(getScene().getEvent() != null)
            {
                getScene().getEvent().setProjectionMatrix(MasterRenderer.getProjectionMatrix());
            }
            FontType font = new FontType(ModelLoader.loadTexture("comic"), new File("resources/font/comic.fnt"));
            GUIText fps = new GUIText("fps count: 0", 1, font, new Vector2f(0, 0), 1f, false);
            fps.setColor(1, 1, 0);

            while(!(Display.isCloseRequested()))
            {
                FPSCounter.update();
                // mouseEvent stuff
                scene.getEvent().update();

                // the shadow thingies
                MasterRenderer.renderShadowMap(scene.getEntities(), scene.getMainLight());

                //particle
                if(scene.getParticleSystem() != null) {
                    ParticleMaster.update(scene.getCamera());
                    scene.getParticleSystem().generateParticles(scene.getPlayer().getPosition());
                }
                ParticleMaster.renderParticles(scene.getCamera());

                // debuggers
                //System.out.println(scene.getEvent().getCurrentTerrainPoint());
                //System.out.println(scene.getEvent().currentRay);


                // the player and the camera movements
                scene.getPlayer().move(scene.getTerrain());
                scene.getCamera().move();


                // frame buffers thingy:
                // apparently, the rendering process in this stuff is that,
                // the first 3 clips [0, 1, 2] have certain black spots
                // in their sky boxes, but now it is gone in the
                // fourth index, finally.
                // took me a day to fix that :sob:
                GL11.glEnable(GL30.GL_CLIP_DISTANCE3);

                // the reflection of the water
                MasterRenderer.buffer.bindReflectionFrameBuffer();
                float distance = 2 * (scene.getCamera().getPosition().y -
                        scene.getWaters().getFirst().getHeight());
                scene.getCamera().getPosition().y -= distance;
                scene.getCamera().invertPitch();
                renderScene(new Vector4f(0, 1, 0,
                        -scene.getWaters().getFirst().getHeight() + 1.2f));
                scene.getCamera().getPosition().y += distance;
                scene.getCamera().invertPitch();

                // the refraction of the water
                MasterRenderer.buffer.bindRefractionFrameBuffer();
                renderScene(new Vector4f(0, -1, 0,
                        scene.getWaters().getFirst().getHeight()));
                GL11.glDisable(GL30.GL_CLIP_DISTANCE3);
                MasterRenderer.buffer.unbindCurrentFrameBuffer();

                //MasterRenderer.buffer.bindScreenFrameBuffer();
                //renderScene(new Vector4f(0, 0, 0, scene.getWaters().getFirst().getHeight()));

                MasterRenderer.buffer.unbindCurrentFrameBuffer();
                renderScene(new Vector4f(0, -1, 0, scene.getWaters().getFirst().getHeight()));


                MasterRenderer.renderWaters(scene.getWaters(), scene.getCamera(), scene.getMainLight());


                scene.getContentPane().render(scene.getContentPane().getComponents());
                TextMasterRenderer.setText(fps, "fps count: " + FPSCounter.getCounter());
                TextMasterRenderer.render();

                DisplayManager.updateDisplay();
            }
            exit();
        }
        else throw new NullPointerException();
    }

    private static void renderScene(Vector4f plane)
    {
        // the 3D space stuff...
        MasterRenderer.processTerrain(scene.getTerrain());
        MasterRenderer.processAllEntities(scene.getEntities());
        MasterRenderer.processAllNormalMappedEntities(scene.getNormalMappedEntities());
        MasterRenderer.renderLights(scene.getLights(), scene.getCamera(), plane);
    }

    public static Scene getScene()
    {
        return scene;
    }

    private static void exit()
    {
        ParticleMaster.dispose();
        TextMasterRenderer.dispose();
        scene.getContentPane().dispose();
        MasterRenderer.dispose();
        ModelLoader.dispose();
        DisplayManager.closeDisplay();
    }
}

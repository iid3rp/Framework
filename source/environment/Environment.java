package environment;

import entity.Entity;
import entity.Player;
import fontRendering.TextMasterRenderer;
import model.Model;
import model.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import render.DisplayManager;
import render.MasterRenderer;
import render.ModelLoader;
import render.ObjectLoader;
import swing.GUIRenderer;
import swing.GUITexture;
import texture.Texture;

import java.util.Random;

public final class Environment
{
    public static Scene scene;

    public static void setScene(Scene scene)
    {
        Environment.scene = scene;
    }

    public static void start()
    {
        GUIRenderer renderer = new GUIRenderer();
        if(scene != null)
        {
            Model treeModel = ObjectLoader.loadObject("tree");
            Texture treeTexture = new Texture(ModelLoader.loadTexture("tree"));
            TexturedModel treeTexturedModel = new TexturedModel(treeModel, treeTexture);

            Model grassModel = ObjectLoader.loadObject("grassModel");
            Texture grassTexture = new Texture(ModelLoader.loadTexture("grassTexture"));
            TexturedModel grassTexturedModel = new TexturedModel(grassModel, grassTexture);
            grassTexturedModel.getTexture().setTransparency(true);

            Model fernModel = ObjectLoader.loadObject("fern");
            Texture fernTexture = new Texture(ModelLoader.loadTexture("fern"));
            TexturedModel fernTexturedModel = new TexturedModel(fernModel, fernTexture);
            fernTexturedModel.getTexture().setTransparency(true);

            Texture flowerTexture = new Texture(ModelLoader.loadTexture("flower"));
            TexturedModel flowerTexturedModel = new TexturedModel(grassModel, flowerTexture);

            Entity box = new Entity(new TexturedModel(ObjectLoader.loadObject("box"), new Texture( ModelLoader.loadTexture("brat"))),
                    new Vector3f(0, 0, 0), 0, 0, 0, 1);
            Player player = new Player(box);
            scene.getEntities().add(player);
            Random random = new Random();

            long time = DisplayManager.getCurrentTime();

            GUITexture texture1 = new GUITexture();
            texture1.setBackgroundImage(MasterRenderer.buffer.getScreenTexture());
            texture1.setPosition(40, 40);
            texture1.setSize(384, 216);
            texture1.mirrorX();
            scene.add(texture1);

            GUITexture texture2 = new GUITexture();
            texture2.setBackgroundImage(MasterRenderer.buffer.getScreenTexture());
            texture2.setPosition(40, 296);
            texture2.setSize(384, 216);
            texture2.mirrorX();
            scene.add(texture2);

            GUITexture texture3 = new GUITexture();
            texture3.setBackgroundImage(MasterRenderer.buffer.getScreenTexture());
            texture3.setPosition(40, 542);
            texture3.setSize(384, 216);
            texture3.mirrorX();
            scene.add(texture3);

            float hello = DisplayManager.getFrameTimeSeconds();
            float world = 4;
            long stuff= 0;
            int actionsInSecond = 0;
            long startTime = DisplayManager.getCurrentTime();
            while(!(Display.isCloseRequested()))
            {
                actionsInSecond++;

                if(hello > world)
                {
                    float x = random.nextFloat(scene.getTerrain().getSize() / 2);
                    float z = random.nextFloat(scene.getTerrain().getSize() / 2);
                    float y = scene.getTerrain().getHeightOfTerrain(x, z);
                    // adding resources in the 3d environment
                    Entity fern = new Entity(fernTexturedModel,
                            new Vector3f(x, y, z),
                            0, 0, 0, 1);

                    x = random.nextFloat(scene.getTerrain().getSize() / 2);
                    z = random.nextFloat(scene.getTerrain().getSize() / 2);
                    y = scene.getTerrain().getHeightOfTerrain(x, z);
                    Entity tree = new Entity(treeTexturedModel,
                            new Vector3f(x, y, z),
                            0, 0, 0, 8);

                    x = random.nextFloat(scene.getTerrain().getSize() / 2);
                    z = random.nextFloat(scene.getTerrain().getSize() / 2);
                    y = scene.getTerrain().getHeightOfTerrain(x, z);
                    Entity grass = new Entity(grassTexturedModel,
                            new Vector3f(x, y, z),
                            0, 0, 0, 2);

                    x = random.nextFloat(scene.getTerrain().getSize() / 2);
                    z = random.nextFloat(scene.getTerrain().getSize() / 2);
                    y = scene.getTerrain().getHeightOfTerrain(x, z);
                    Entity flower = new Entity(flowerTexturedModel,
                            new Vector3f(x, y, z),
                            0, 0, 0, 2);

                    tree.transformRotation(0,random.nextFloat(1), 0);
                    grass.transformRotation(0,random.nextFloat(1), 0);
                    fern.transformRotation(0,random.nextFloat(1), 0);
                    fern.transformRotation(0,random.nextFloat(1), 0);
                    flower.transformRotation(0, random.nextFloat(1), 0);

                    scene.getEntities().add(tree);
                    scene.getEntities().add(grass);
                    scene.getEntities().add(fern);
                    scene.getEntities().add(flower);
                    //System.out.println("added");
                    world += .25f;
                    stuff++;

                    if(DisplayManager.getCurrentTime() - startTime >= 1000) {
                        //System.out.println("Actions in a second: " + actionsInSecond);
                        actionsInSecond = 0;
                        startTime = DisplayManager.getCurrentTime();
                    }
                }
                hello += DisplayManager.getFrameTimeSeconds();

                // mouseEvent stuff
                scene.getEvent().update();
                // debuggers
                //System.out.println(scene.getEvent().getCurrentTerrainPoint());
                //System.out.println(scene.getEvent().currentRay);

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

                MasterRenderer.buffer.bindScreenFrameBuffer();
                renderScene(new Vector4f(0, 0, 0, scene.getWaters().getFirst().getHeight()));

                MasterRenderer.buffer.unbindCurrentFrameBuffer();
                renderScene(new Vector4f(0, -1, 0, scene.getWaters().getFirst().getHeight()));


                MasterRenderer.renderWaters(scene.getWaters(), scene.getCamera(), scene.getMainLight());
                scene.getContentPane().render(scene.getContentPane().getComponents());
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
        TextMasterRenderer.dispose();
        scene.getContentPane().dispose();
        MasterRenderer.dispose();
        ModelLoader.dispose();
        DisplayManager.closeDisplay();
    }
}

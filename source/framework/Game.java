package framework;

import framework.entity.Camera;
import framework.entity.Entity;
import framework.entity.Light;
import framework.entity.Player;
import framework.model.TexturedModel;
import framework.swing.ContentPane;
import framework.swing.PictureBox;
import framework.terrains.Terrain;
import framework.textures.ModelTexture;
import framework.textures.TerrainTexture;
import framework.textures.TerrainTexturePack;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static framework.DisplayManager.closeDisplay;
import static framework.DisplayManager.createDisplay;
import static framework.DisplayManager.getLwjglVersionMessage;
import static framework.DisplayManager.getOpenGlVersionMessage;
import static framework.DisplayManager.setShowFPSTitle;
import static framework.DisplayManager.shouldDisplayClose;
import static framework.DisplayManager.updateDisplay;
import static java.lang.management.ManagementFactory.*;

public class Game
{
    static Random random = new Random();

    private static void start() {
        createDisplay();

        // Show FPS title only if debugging
        if (getRuntimeMXBean().getInputArguments().toString().contains("-agentlib:jdwp")) {
            setShowFPSTitle(true);
        }

        // uhm how about yes?
        setShowFPSTitle(true);

        System.out.println("OpenGL: " + getOpenGlVersionMessage());
        System.out.println("LWJGL: " + getLwjglVersionMessage());

        // Tree entity
        TexturedModel treeModel = new TexturedModel(
                ObjLoader.loadObjModel("resources/tree.obj"), new ModelTexture(ModelLoader.loadTexture("resources/tree.png")));

        // Low poly tree entity
        TexturedModel lowPolyTreeModel = new TexturedModel(
                ObjLoader.loadObjModel("resources/lowPolyTree.obj"), new ModelTexture(ModelLoader.loadTexture("resources/lowPolyTree.png")));

        // Grass entity
        TexturedModel grassModel = new TexturedModel(
                ObjLoader.loadObjModel("resources/grassModel.obj"), new ModelTexture(ModelLoader.loadTexture("resources/grassTexture.png")));
        grassModel.getModelTexture().setHasTransparency(true);
        grassModel.getModelTexture().setUseFakeLighting(true);

        // Fern entity
        ModelTexture fernTextureAtlas = new ModelTexture(ModelLoader.loadTexture("resources/fern.png"));
        fernTextureAtlas.setNumberOfRowsInTextureAtlas(2);
        TexturedModel fernModel = new TexturedModel(
                ObjLoader.loadObjModel("resources/fern.obj"), fernTextureAtlas);
        fernModel.getModelTexture().setHasTransparency(true);

        // Multi-textured Terrain
        TerrainTexture backgroundTexture = new TerrainTexture(ModelLoader.loadTexture("resources/grassy2.png"));
        TerrainTexture rTexture = new TerrainTexture(ModelLoader.loadTexture("resources/mud.png"));
        TerrainTexture gTexture = new TerrainTexture(ModelLoader.loadTexture("resources/grassFlowers.png"));
        TerrainTexture bTexture = new TerrainTexture(ModelLoader.loadTexture("resources/path.png"));
        TerrainTexture blendMap = new TerrainTexture(ModelLoader.loadTexture("resources/blendMap.png"));

        TerrainTexturePack terrainTexturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);

        // Terrain entityList
        Terrain terrain = new Terrain(0, 0, terrainTexturePack, blendMap);

        List<Entity> entityList = new ArrayList<>();

        for (int i = 0; i < 400; i++) {
            float x = random.nextFloat() * 800 - 400;
            float z = random.nextFloat() * -600;
            float y = terrain.getHeightOfTerrain(x, z);

            if (i % 20 == 0) {
                entityList.add(new Entity(lowPolyTreeModel, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 1));
            }

            x = random.nextFloat() * 800 - 400;
            z = random.nextFloat() * -600;
            y = terrain.getHeightOfTerrain(x, z);

            if (i % 20 == 0) {
                entityList.add(new Entity(treeModel, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 5));
            }

            x = random.nextFloat() * 800 - 400;
            z = random.nextFloat() * -600;
            y = terrain.getHeightOfTerrain(x, z);

            if (i % 10 == 0) {
                // assigns a random texture for each fern from its texture atlas
                entityList.add(new Entity(fernModel, random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 0.9f));
            }

            if (i % 5 == 0) {
                entityList.add(new Entity(grassModel, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 1));
            }
        }

        ArrayList<Light> lights = new ArrayList<>();

        Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1, 1,1));
        lights.add(light);
        MasterRenderer.setRenderer();

        TexturedModel bunnyModel = new TexturedModel(
                ObjLoader.loadObjModel("resources/stanfordBunny.obj"), new ModelTexture(ModelLoader.loadTexture("resources/white.png")));
        Entity entity = new Entity(bunnyModel, new Vector3f(0, 0, 0), 0,0,0,0.6f);
        Player player = new Player(entity);
        Camera camera = new Camera(player);

        ContentPane pane = new ContentPane();

        PictureBox box = new PictureBox();
        box.setBackgroundImage(ModelLoader.loadTexture("resources/brat.png"));
        box.setLocation(10, 10);
        box.setSize(300, 300);
        //pane.add(box);

        while (shouldDisplayClose()) {
            player.move(terrain);   // to do this with multiple Terrain, need to test first to know which Terrain the player's position is in
            //player.camera.move();

            MasterRenderer.processEntity(player);
            MasterRenderer.processTerrain(terrain);

            for (Entity e : entityList) {
                MasterRenderer.processEntity(e);
            }

            MasterRenderer.render(lights, player.camera);

            pane.render(pane.getComponents());
            updateDisplay();
        }

        MasterRenderer.destroy();
        ModelLoader.destroy();
        closeDisplay();
    }

    public static void main(String[] args) {
        start();
    }
}

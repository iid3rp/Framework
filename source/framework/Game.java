package framework;

import framework.loader.ModelLoader;
import framework.loader.ObjectLoader;
import framework.entity.Entity;
import framework.entity.Light;
import framework.entity.Player;
import framework.environment.Environment;
import framework.environment.Scene;
import framework.loader.TextureLoader;
import framework.model.TexturedModel;
import framework.normals.NormalMappedObjLoader;
import framework.scripting.Count;
import framework.swing.ContentPane;
import framework.swing.GUITexture;
import framework.swing.PictureBox;
import framework.terrains.Terrain;
import framework.textures.TerrainTexture;
import framework.textures.TerrainTexturePack;
import framework.textures.Texture;
import framework.water.WaterTile;
import org.joml.Vector3f;

import java.util.Random;

import static framework.Display.DisplayManager.createDisplay;
import static framework.Display.DisplayManager.getLwjglVersionMessage;
import static framework.Display.DisplayManager.getOpenGlVersionMessage;
import static framework.Display.DisplayManager.setShowFPSTitle;
import static java.lang.management.ManagementFactory.getRuntimeMXBean;

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
    }

    public static void main(String[] args) {
        start();
        Scene scene = new Scene();
        ContentPane panel = new ContentPane();
        Environment.setScene(scene);


        Light lighting = new Light(new Vector3f(1_000_000, 1_000_000, 1_000_000), new Vector3f(1f, 1f, 1f));

        scene.getLights().add(lighting);
        scene.getLights().add(new Light(new Vector3f(0, 10, 0), new Vector3f(1, 0, 1), new Vector3f(1, 0f, 200f)));
        scene.getLights().add(new Light(new Vector3f(20, 0, 20), new Vector3f(1, 0, 1), new Vector3f(1, 0.01f, 0.02f)));
        scene.getLights().add(new Light(new Vector3f(40, 0, 40), new Vector3f(1, 0, 1), new Vector3f(1, 0.1f, 0.002f)));
        scene.getLights().add(new Light(new Vector3f(-20, 0, 60), new Vector3f(1, 0, 0), new Vector3f(1, 0.01f, 0.02f)));
        scene.getLights().add(new Light(new Vector3f(0, 0, -80), new Vector3f(1, 0, 1), new Vector3f(1, 0.1f, 0.002f)));

        TerrainTexturePack bg = new TerrainTexturePack(
                new TerrainTexture(ModelLoader.loadTexture("grass.png")),
                new TerrainTexture(ModelLoader.loadTexture("mud.png")),
                new TerrainTexture(ModelLoader.loadTexture("path.png")),
                new TerrainTexture(ModelLoader.loadTexture("pine.png"))
        );
        TerrainTexture blend = new TerrainTexture(ModelLoader.loadTexture("blendMap.png"));
        Terrain terrain = new Terrain(0, 0, bg, blend);

        scene.setTerrain(terrain);

        TexturedModel barrel = new TexturedModel(NormalMappedObjLoader.loadObject("barrel.obj"),
                TextureLoader.generateTexture("barrel.png"));
        Entity barrelEntity = new Entity(barrel, new Vector3f(0, 200, 0), 0, 0, 0, 1);


        Random random = new Random();
        int progress = 0;


        Player player = new Player(barrelEntity);
        //player.setLight();
        //player.setLightColor(255, 255, 255);
        //player.setLightAttenuation(new Vector3f(.1f, .01f, .01f));
        scene.setPlayer(player);
        scene.getEntities().add(player);
        //scene.getLights().add(player.getLight());
        scene.setCamera(player.getCamera());

        GUITexture img = new PictureBox();
        img.setBackgroundImage(barrel.getTexture().getNormalMap());
        img.setSize(300, 300);
        img.setLocation(20, 20);

        //panel.add(img);
        scene.setContentPane(panel);


        //scene.add(new WaterTile(75, -75, 0));

        TexturedModel chrysalis = new TexturedModel(ObjectLoader.loadObjModel("tree.obj"),new Texture(ModelLoader.loadTexture("tree.png")));
        chrysalis.getTexture().setShineDampening(1f);
        chrysalis.getTexture().setReflectivity(.1f);

        Entity entity = new Entity(new TexturedModel(
                ObjectLoader.loadObjModel("crate.obj"),
                new Texture(ModelLoader.loadTexture("brat.png"))),
                new Vector3f(0, 0, 0), 0f, 0f, 0f, 1f);
        //scene.add(entity);

        for(int i = 0 ; i < 100; i++) {
            float x = random.nextFloat(terrain.getSize()) - (terrain.getSize() / 2);
            float z = random.nextFloat(terrain.getSize()) - (terrain.getSize() / 2);
            float y = terrain.getHeightOfTerrain(x, z);
            Entity crystal = new Entity(chrysalis, new Vector3f(x, y, z), 0, 0, 0, 10f);
            if(crystal.getPosition().y > 0) {
                scene.getEntities().add(crystal);
            }
            else i--;
        }

        WaterTile tile = new WaterTile(0, 0,0);
        scene.add(tile);


        Environment.run(new Count());
        Environment.start();
    }
}

import entity.Entity;
import entity.Light;
import entity.Player;
import environment.Environment;
import environment.Scene;
import event.MouseEvent;
import normals.NormalMappedObjLoader;
import render.DisplayManager;
import render.MasterRenderer;
import render.ModelLoader;
import render.ObjectLoader;
import streamio.Resources;
import swing.ContentPane;
import swing.GUITexture;
import swing.PictureBox;
import terrain.Terrain;
import texture.TerrainTexture;
import texture.TerrainTexturePack;
import texture.Texture;
import model.Model;

import model.TexturedModel;
import org.lwjgl.util.vector.Vector3f;
import util.Intention;
import water.WaterTile;

import java.util.Random;

public class Example
{
    public static void main(String[] a)
    {
        Resources.setResource("resources");
        DisplayManager.setTitle("Hello World!");
        DisplayManager.setSize(1280, 720);
        DisplayManager.createDisplay();

        Scene scene = new Scene();
        ContentPane panel = new ContentPane();
        Environment.setScene(scene);

        GUITexture img = new PictureBox();
        img.setBackgroundImage(ModelLoader.loadTexture("brat"));
        img.setSize(30, 30);
        img.setLocation(20, 20);

        panel.add(img);
        scene.setContentPane(panel);


        Model model = ObjectLoader.loadObject("dragon");
        Texture texture = new Texture(ModelLoader.loadTexture("brat"));
        TexturedModel texturedModel = new TexturedModel(model, texture);

        Texture reflection = texturedModel.getTexture();
        reflection.setShineDampening(5);
        reflection.setReflectivity(1);
        Light lighting = new Light(new Vector3f(1_000_000, 1_000_000, 1_000_000), new Vector3f(.9f, .8f, 1f));

        scene.getLights().add(lighting);
        scene.getLights().add(new Light(new Vector3f(0, 10, 0), new Vector3f(1, 0, 1), new Vector3f(1, 0f, 200f)));
        scene.getLights().add(new Light(new Vector3f(20, 0, 20), new Vector3f(1, 0, 1), new Vector3f(1, 0.01f, 0.02f)));
        scene.getLights().add(new Light(new Vector3f(40, 0, 40), new Vector3f(1, 0, 1), new Vector3f(1, 0.1f, 0.002f)));
        scene.getLights().add(new Light(new Vector3f(-20, 0, 60), new Vector3f(1, 0, 0), new Vector3f(1, 0.01f, 0.02f)));
        scene.getLights().add(new Light(new Vector3f(0, 0, -80), new Vector3f(1, 0, 1), new Vector3f(1, 0.1f, 0.002f)));

        TerrainTexturePack bg = new TerrainTexturePack(
                new TerrainTexture(ModelLoader.loadTexture("grass")),
                new TerrainTexture(ModelLoader.loadTexture("mud")),
                new TerrainTexture(ModelLoader.loadTexture("path")),
                new TerrainTexture(ModelLoader.loadTexture("pine"))
        );
        TerrainTexture blend = new TerrainTexture(ModelLoader.loadTexture("blendMap"));
        Terrain terrain = new Terrain(0, 0, bg, blend, "heightmap");

        scene.setTerrain(terrain);
        scene.getEntities().add(new Entity(texturedModel, new Vector3f(0, 0, -30), 0, 0, 0, 1));


        TexturedModel barrel = new TexturedModel(NormalMappedObjLoader.loadObject("barrel"),
                new Texture(ModelLoader.loadTexture("barrel")));
        barrel.getTexture().setNormalMap(ModelLoader.loadTexture("barrelNormal"));
        //barrel.getTexture().setReflectivity(.5f);
        //barrel.getTexture().setShineDampening(10);
        Entity barrelEntity = new Entity(barrel, new Vector3f(0, 200, 0), 0, 0, 0, 1);


        Random random = new Random();
        int progress = 0;

        Entity box = new Entity(
                new TexturedModel(NormalMappedObjLoader.loadObject("barrel"),
                new Texture(ModelLoader.loadTexture("brat"))),
                new Vector3f(0, 0, 0),
                0,
                0,
                0,
                2);
        Player player = new Player(barrelEntity);
        //player.setLight();
        //player.setLightColor(255, 255, 255);
        //player.setLightAttenuation(new Vector3f(.1f, .01f, .01f));
        scene.setPlayer(player);
        scene.getEntities().add(player);
        scene.getEntities().add(box);
        //scene.getLights().add(player.getLight());
        scene.setCamera(player.getCamera());


        scene.setTerrainSize(500, 500);
        scene.add(new WaterTile(75, -75, 0));

        TexturedModel chrysalis = new TexturedModel(ObjectLoader.loadObject("tree"),new Texture(ModelLoader.loadTexture("tree")));
        Entity entity = new Entity(new TexturedModel(
                ObjectLoader.loadObject("crate"),
                        new Texture(ModelLoader.loadTexture("brat"))),
                new Vector3f(0, -300, 0), 0f, 0f, 0f, 10f);
        scene.add(entity);

        for(int i = 0 ; i < 300; i++)
        {
            float x = random.nextFloat(terrain.getSize());
            float z = random.nextFloat(terrain.getSize());
            float y = terrain.getHeightOfTerrain(x, z);
            Entity crystal = new Entity(chrysalis, new Vector3f(x, y, z), 0, 0, 0, 10f);
            scene.getEntities().add(crystal);
        }

        @Intention(design = "forRemoval")
        PictureBox shadowMap = new PictureBox();
        shadowMap.setBackgroundImage(MasterRenderer.getShadowMapTexture());
        shadowMap.setSize(256, 144);
        shadowMap.setLocation(20, 70);
        scene.add(shadowMap);


        MouseEvent event = new MouseEvent();
        event.setCamera(scene.getCamera());

        scene.setEvent(event);
        Environment.start();
    }
}

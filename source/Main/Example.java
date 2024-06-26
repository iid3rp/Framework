package Main;

import entity.Entity;
import entity.Light;
import entity.Player;
import environment.Environment;
import environment.Scene;
import event.MouseEvent;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMasterRenderer;
import normalMappingObjConverter.NormalMappedObjLoader;
import org.lwjgl.util.vector.Vector2f;
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
import water.WaterTile;

import java.io.File;
import java.util.Random;

public class Example
{
    public static void main(String[] a)
    {
        Resources.setResource("resources");
        DisplayManager.setTitle("Hello World!");
        DisplayManager.setSize(1280, 720);
        DisplayManager.createDisplay();
        TextMasterRenderer.initialize();

        Scene scene = new Scene();
        ContentPane panel = new ContentPane();

        GUITexture img = new PictureBox();
        img.setBackgroundImage(ModelLoader.loadTexture("brat"));
        img.setSize(30, 30);
        img.setPosition(20, 20);

        panel.add(img);

        scene.setContentPane(panel);


        Model model = ObjectLoader.loadObject("dragon");
        Texture texture = new Texture(ModelLoader.loadTexture("brat"));
        TexturedModel texturedModel = new TexturedModel(model, texture);

        Texture reflection = texturedModel.getTexture();
        reflection.setShineDampening(5);
        reflection.setReflectivity(1);
        Light lighting = new Light(new Vector3f(0, 3, 0), new Vector3f(.9f, .8f, 1f));

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
        Terrain terrain = new Terrain(-.5f, -.5f, bg, blend, "heightmap");

        scene.setTerrain(terrain);
        scene.getEntities().add(new Entity(texturedModel, new Vector3f(0, 0, -30), 0, 0, 0, 1));


        TexturedModel barrel = new TexturedModel(NormalMappedObjLoader.loadObject("barrel"),
                new Texture(ModelLoader.loadTexture("barrel")));
        barrel.getTexture().setNormalMap(ModelLoader.loadTexture("barrelNormal"));
        barrel.getTexture().setReflectivity(.5f);
        barrel.getTexture().setShineDampening(10);
        Entity barrelEntity = new Entity(barrel, new Vector3f(0, 0, 0), 0, 0, 0, 1);


        Random random = new Random();
        int progress = 0;

        Entity box = new Entity(
                new TexturedModel(NormalMappedObjLoader.loadObject("box"),
                new Texture(ModelLoader.loadTexture("brat"))),
                new Vector3f(0, 0, 0),
                0,
                0,
                0,
                2);
        Player player = new Player(barrelEntity);
        player.setLight();
        player.setLightColor(255, 255, 255);
        player.setLightAttenuation(new Vector3f(.1f, .01f, .01f));
        scene.setPlayer(player);
        scene.getNormalMappedEntities().add(player);
        scene.getEntities().add(box);
        scene.getLights().add(player.getLight());
        scene.setCamera(player.getCamera());


        scene.setTerrainSize(500, 500);
        scene.add(new WaterTile(75, -75, 0));

        FontType type = new FontType(ModelLoader.loadTexture("comicMono"),
                new File("resources/font/comicMono.fnt"));

        GUIText text = new GUIText("""
                this is a text :3 that is so long that it's\s
                very very very very\s
                VERY VERY LONG!!!! and big :o""", 2, type, new Vector2f(.5f, .5f), .5f, true);
        text.setColor((float) 10 /255, (float) 255/255, (float) 10 /255);



        Environment.setScene(scene);
        MasterRenderer.setRenderer();

        MouseEvent event = new MouseEvent(Environment.getScene().getPlayer().getCamera(),
                MasterRenderer.getProjectionMatrix());

        scene.setEvent(event);
        Environment.start();
    }
}

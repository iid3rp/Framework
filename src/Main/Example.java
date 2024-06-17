package Main;

import Swing.GUIRenderer;
import Swing.GUITexture;
import entity.Entity;
import entity.Lighting;
import entity.Player;
import org.lwjgl.util.vector.Vector2f;
import render.DisplayManager;
import render.MasterRenderer;
import render.ModelLoader;
import render.ObjectLoader;
import streamio.Resources;
import terrain.Terrain;
import texture.TerrainTexture;
import texture.TerrainTexturePack;
import texture.Texture;
import model.Model;

import model.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Example
{
    public static void main(String[] a)
    {
        DisplayManager.createDisplay();
        MasterRenderer.setRenderer();
        Resources.setResource("resources");


        Model model = ObjectLoader.loadObject("dragon");
        Texture texture = new Texture(ModelLoader.loadTexture("brat"));
        TexturedModel texturedModel = new TexturedModel(model, texture);

        Texture reflection = texturedModel.getTexture();
        reflection.setShineDampening(5);
        reflection.setReflectivity(1);

        List<GUITexture> guis = new ArrayList<>();
        GUITexture guisTexture = new GUITexture(ModelLoader.loadTexture("brat"), new Vector2f(.5f, .5f), new Vector2f(.25f, .25f));
        //guis.add(guisTexture);

        Lighting lighting = new Lighting(new Vector3f(2000, 3000 ,2000), new Vector3f(1, 1, 1));

        TerrainTexture background = new TerrainTexture(ModelLoader.loadTexture("grassy"));
        TerrainTexture red = new TerrainTexture(ModelLoader.loadTexture("mud"));
        TerrainTexture green = new TerrainTexture(ModelLoader.loadTexture("grassFlowers"));
        TerrainTexture blue = new TerrainTexture(ModelLoader.loadTexture("path"));

        TerrainTexturePack pack = new TerrainTexturePack(background, red, green, blue);
        TerrainTexture blendMap = new TerrainTexture(ModelLoader.loadTexture("blendMap"));

        Model person = ObjectLoader.loadObject("bunny");
        Texture personTexture = new Texture(ModelLoader.loadTexture("brat"));
        TexturedModel personTexturedModel = new TexturedModel(person, personTexture);

        Texture reflection1 = personTexturedModel.getTexture();
        reflection1.setShineDampening(5);
        reflection1.setReflectivity(1);

        Player player = new Player(new Entity(personTexturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 1));

        Terrain terrain = new Terrain(-.5f, -.5f, pack, blendMap, "heightMap");


        List<Entity> entities = new ArrayList<>();
        entities.add(new Entity(texturedModel, new Vector3f(0, 0,-30), 0, 0, 0, 1));
        entities.add(player);

        Random random = new Random();
        int progress = 0;

        // resource load goes here:
        Model treeModel = ObjectLoader.loadObject("tree");
        Texture treeTexture = new Texture(ModelLoader.loadTexture("tree"));
        TexturedModel treeTexturedModel = new TexturedModel(treeModel, treeTexture);

        Model grassModel = ObjectLoader.loadObject("grassModel");
        Texture grassTexture = new Texture(ModelLoader.loadTexture("grassTexture"));
        TexturedModel grassTexturedModel = new TexturedModel(grassModel, grassTexture);
        grassTexturedModel.getTexture().setTransparency(true);
        grassTexturedModel.getTexture().setUseFakeLighting(true);

        Model fernModel = ObjectLoader.loadObject("fern");
        Texture fernTexture = new Texture(ModelLoader.loadTexture("fernAtlas"));
        fernTexture.setNumberOfRows(2);
        TexturedModel fernTexturedModel = new TexturedModel(fernModel, fernTexture);
        fernTexturedModel.getTexture().setTransparency(true);
        fernTexturedModel.getTexture().setUseFakeLighting(true);

        Texture flowerTexture = new Texture(ModelLoader.loadTexture("flower"));
        TexturedModel flowerTexturedModel = new TexturedModel(grassModel, flowerTexture);
        flowerTexturedModel.getTexture().setTransparency(true);
        flowerTexturedModel.getTexture().setUseFakeLighting(true);

        float x;
        float y;
        float z;
        // other trees
        for(int i = 0; i < 500; i++) {
            x = random.nextFloat(Terrain.SIZE) - (Terrain.SIZE / 2);
            z = random.nextFloat(Terrain.SIZE) - (Terrain.SIZE / 2);
            y = terrain.getHeightOfTerrain(x, z);
            // adding resources in the 3d environments

            Entity tree = new Entity(treeTexturedModel,
                    new Vector3f(x, y - 0.1f, z),
                    0, 0, 0, 8);
            tree.transformRotation(0, random.nextFloat(1), 0);
            entities.add(tree);
        }

        for(int i = 0; i < 500; i++) {
            x = random.nextFloat(Terrain.SIZE) - (Terrain.SIZE / 2);
            z = random.nextFloat(Terrain.SIZE) - (Terrain.SIZE / 2);
            y = terrain.getHeightOfTerrain(x, z);
            // adding resources in the 3d environments

            Entity fern = new Entity(fernTexturedModel,
                    new Vector3f(x, y - 0.1f, z),
                    0, 0, 0, 1);
            fern.setTextureIndex(random.nextInt(4));
            fern.transformRotation(0, random.nextFloat(1), 0);
            entities.add(fern);
        }

        System.out.println("lets go!!");

        GUIRenderer renderer = new GUIRenderer();

        while(!(Display.isCloseRequested()))
        {
            player.move(terrain);
            MasterRenderer.processAllEntities(entities);
            MasterRenderer.processTerrain(terrain);
            MasterRenderer.render(lighting, player);
            renderer.render(guis);
            DisplayManager.updateDisplay();
        }
        renderer.dispose();
        MasterRenderer.dispose();
        ModelLoader.dispose();
        DisplayManager.closeDisplay();
    }
}

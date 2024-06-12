package Main;

import entity.Camera;
import entity.Entity;
import entity.Lighting;
import render.DisplayManager;
import render.MasterRenderer;
import render.ModelLoader;
import render.ObjectLoader;
import streamio.Resources;
import terrain.Terrain;
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
        Resources.setResource("resourcesdfss");


        Model model = ObjectLoader.loadObject("dragon");
        Texture texture = new Texture(ModelLoader.loadTexture("brat"));
        TexturedModel texturedModel = new TexturedModel(model, texture);

        Texture reflection = texturedModel.getTexture();
        reflection.setShineDampening(5);
        reflection.setReflectivity(1);

        Camera camera = new Camera();
        Lighting lighting = new Lighting(new Vector3f(0, 3 ,0), new Vector3f(1, 1, 0));

        // episode 14 simple terrain
        // timestamp: 17:02
        Terrain[] terrains = new Terrain[]
        {
                new Terrain(-1, -1,
                        new Texture(ModelLoader.loadTexture("grass"))),
                new Terrain(0, -1,
                        new Texture(ModelLoader.loadTexture("grass"))),
            new Terrain(0, 0,
                    new Texture(ModelLoader.loadTexture("grass"))),
            new Terrain(-1, 0,
                    new Texture(ModelLoader.loadTexture("grass")))
        };

        terrains[0].getTexture().setShineDampening(5).setReflectivity(1);
        terrains[1].getTexture().setShineDampening(5).setReflectivity(1);
        terrains[2].getTexture().setShineDampening(5).setReflectivity(1);
        terrains[3].getTexture().setShineDampening(5).setReflectivity(1);

        List<Entity> entities = new ArrayList<>();
        entities.add(new Entity(texturedModel, new Vector3f(0, 0,-30), 0, 0, 0, 1));

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

        Model fernModel = ObjectLoader.loadObject("fern");
        Texture fernTexture = new Texture(ModelLoader.loadTexture("fern"));
        TexturedModel fernTexturedModel = new TexturedModel(fernModel, fernTexture);
        fernTexturedModel.getTexture().setTransparency(true);

        Texture flowerTexture = new Texture(ModelLoader.loadTexture("flower"));
        TexturedModel flowerTexturedModel = new TexturedModel(grassModel, flowerTexture);

        // other trees
        for(int i = 0; i < 500; i++)
        {
            // adding resources in the 3d environment
            Entity fern = new Entity(fernTexturedModel,
                    new Vector3f(random.nextInt(800) - 400, 0, random.nextInt(800) - 400),
                    0, 0, 0, 1);

            Entity tree = new Entity(treeTexturedModel,
                    new Vector3f(random.nextInt(800) - 400, 0, random.nextInt(800) - 400),
                    0, 0, 0, 8);

            Entity grass = new Entity(grassTexturedModel,
                    new Vector3f(random.nextInt(800) - 400, 0, random.nextInt(800) - 400),
                    0, 0, 0, 2);

            Entity flower = new Entity(flowerTexturedModel,
                    new Vector3f(random.nextInt(800) - 400, 0, random.nextInt(800) - 400),
                    0, 0, 0, 2);

            tree.transformRotation(0,random.nextFloat(1), 0);
            grass.transformRotation(0,random.nextFloat(1), 0);
            fern.transformRotation(0,random.nextFloat(1), 0);
            fern.transformRotation(0,random.nextFloat(1), 0);
            flower.transformRotation(0, random.nextFloat(1), 0);

            entities.add(tree);
            entities.add(grass);
            entities.add(fern);
            entities.add(flower);

            if(i % 50 == 0)
            {
                progress += 10;
                System.out.println("Loading Resources: " + progress);
            }
        }

        while(!(Display.isCloseRequested()))
        {
            MasterRenderer.processAllEntities(entities);
            entities.getFirst().transformRotation(0, 1, 0);
            camera.move();
            MasterRenderer.processAllTerrains(terrains);
            MasterRenderer.render(lighting, camera);
            DisplayManager.updateDisplay();
        }
        MasterRenderer.dispose();
        ModelLoader.dispose();
        DisplayManager.closeDisplay();
    }
}

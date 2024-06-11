package Main;

import entity.Camera;
import entity.Entity;
import entity.Lighting;
import render.DisplayManager;
import render.MasterRenderer;
import render.ModelLoader;
import render.ObjectLoader;
import terrain.Terrain;
import texture.Texture;
import model.Model;

import model.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DarkMatterSuite
{
    public static void main(String[] a)
    {

        DisplayManager.createDisplay();
        ModelLoader loader = new ModelLoader();


        Model model = ObjectLoader.loadObject("dragon", loader);
        Texture texture = new Texture(loader.loadTexture("brat"));
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
                new Terrain(-1, -1, loader,
                        new Texture(loader.loadTexture("grass"))),
                new Terrain(0, -1, loader,
                        new Texture(loader.loadTexture("grass"))),
            new Terrain(0, 0, loader,
                    new Texture(loader.loadTexture("grass"))),
            new Terrain(-1, 0, loader,
                    new Texture(loader.loadTexture("grass")))
        };

        terrains[0].getTexture().setShineDampening(5).setReflectivity(1);
        terrains[1].getTexture().setShineDampening(5).setReflectivity(1);
        terrains[2].getTexture().setShineDampening(5).setReflectivity(1);
        terrains[3].getTexture().setShineDampening(5).setReflectivity(1);

        List<Entity> entities = new ArrayList<>();
        entities.add(new Entity(texturedModel, new Vector3f(0, 0,-30), 0, 0, 0, 1));

        Random random = new Random();
        // other trees
        for(int i = 0; i < 500; i++)
        {
            // adding trees for instance...
            Model treeModel = ObjectLoader.loadObject("tree", loader);
            Texture treeTexture = new Texture(loader.loadTexture("tree"));
            TexturedModel treeTexturedModel = new TexturedModel(treeModel, treeTexture);
            entities.add(new Entity(treeTexturedModel,
                    new Vector3f(random.nextInt(800) - 400, 0, random.nextInt(800) - 400),
                    0, 0, 0, 8)
            );
        }

        MasterRenderer renderer = new MasterRenderer();

        while(!(Display.isCloseRequested()))
        {
            renderer.processAllEntities(entities);
            entities.getFirst().transformRotation(0, 1, 0);
            camera.move();
            renderer.processAllTerrains(terrains);
            renderer.render(lighting, camera);
            DisplayManager.updateDisplay();
        }
        renderer.dispose();
        loader.dispose();
        DisplayManager.closeDisplay();
    }
}

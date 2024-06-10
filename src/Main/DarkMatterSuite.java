package Main;

import entity.Camera;
import entity.Entity;
import entity.Lighting;
import render.DisplayManager;
import render.MasterRenderer;
import render.ModelLoader;
import render.ObjectLoader;
import texture.Texture;
import model.Model;

import model.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

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
        reflection.setShineDampening(10);
        reflection.setReflectivity(1);

        Camera camera = new Camera();
        Lighting lighting = new Lighting(new Vector3f(0, 0 ,0), new Vector3f(1, 1, 1));

        List<Entity> entities = new ArrayList<>();
        entities.add(new Entity(texturedModel, new Vector3f(0, -1 ,-30), 0, 0, 0, 1));

        MasterRenderer renderer = new MasterRenderer();

        while(!(Display.isCloseRequested()))
        {
            for(Entity entity : entities)
            {
                entities.getFirst().transformRotation(0, 1, 0);
                renderer.processEntity(entity);
            }
            camera.move();
            renderer.render(lighting, camera);
            DisplayManager.updateDisplay();
        }
        renderer.dispose();
        loader.dispose();
        DisplayManager.closeDisplay();
    }
}

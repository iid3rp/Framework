package Test;

import Entities.Camera;
import Entities.Entity;
import Render.DisplayManager;
import Render.ModelLoader;
import Render.ObjectLoader;
import Render.Render;
import Textures.ModelTexture;
import model.Model;

import Shaders.StaticShader;
import model.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

public class DarkMatterSuite
{
    public static void main(String[] a)
    {
        DisplayManager.createDisplay();
        ModelLoader loader = new ModelLoader();

        StaticShader shader = new StaticShader();
        Render render = new Render(shader);


        Model model = ObjectLoader.loadObject("stall", loader);

        // brat!
        ModelTexture texture = new ModelTexture(loader.loadTexture("brat"));
        TexturedModel texturedModel = new TexturedModel(model, texture);
        Entity entity = new Entity(texturedModel, new Vector3f(0, 0 ,-50), 0, 0, 0, 1);

        Camera camera = new Camera();

        while(!(Display.isCloseRequested()))
        {
            entity.transformPosition(0, 0, 0f);
            entity.transformRotation(1, 1, 0);
            camera.move();
            render.prepare();
            shader.start();
            shader.loadViewMatrix(camera);
            render.render(entity, shader);
            shader.stop();
            DisplayManager.updateDisplay();
        }

        shader.cleanUp();
        loader.dispose();
        DisplayManager.closeDisplay();
    }
}

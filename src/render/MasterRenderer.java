package render;

import entity.Camera;
import entity.Entity;
import entity.Lighting;
import shader.Shader;
import model.TexturedModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRenderer
{
    private Shader shader;
    private Render render;
    private Map<TexturedModel, List<Entity>> entities;

    public MasterRenderer()
    {
        shader = new Shader();
        render = new Render(shader);
        entities = new HashMap<>();
    }
    public void render(Lighting lighting, Camera camera)
    {
        render.prepare();
        shader.start();
        shader.loadLighting(lighting);
        shader.loadViewMatrix(camera);
        render.render(entities);
        shader.stop();
        entities.clear(); // very important!!
    }
    public void processEntity(Entity entity)
    {
        TexturedModel texturedModel = entity.getModel();
        List<Entity> batch = entities.get(texturedModel);
        if(batch != null)
        {
            batch.add(entity);
        }
        else
        {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entities.put(texturedModel, newBatch);
        }
    }
    public void dispose()
    {
        shader.dispose();
    }
}

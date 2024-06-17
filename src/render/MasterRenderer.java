package render;

import entity.Camera;
import entity.Entity;
import entity.Lighting;
import entity.Player;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import shader.EntityShader;
import model.TexturedModel;
import shader.TerrainShader;
import terrain.Terrain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRenderer
{
    private static float fieldOfView = 70;
    private static float nearPlane = .1f;
    private static float farPlane = 1000f;
    private static float red = 0.5f;
    private static float green = 0.5f;
    private static  float blue = 0.5f;
    private static Matrix4f projection;
    private static EntityShader shader;
    private static EntityRender render;
    private static Map<TexturedModel, List<Entity>> entities;
    private static TerrainRender terrainRender;
    private static TerrainShader terrainShader;
    private static List<Terrain> terrains;

    public static void setRenderer()
    {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        createProjectionMatrix();
        shader = new EntityShader();
        render = new EntityRender(shader, projection);
        entities = new HashMap<>();
        terrainShader = new TerrainShader();
        terrainRender = new TerrainRender(terrainShader, projection);
        terrains = new ArrayList<>();
    }

    public static void enableCulling()
    {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    public static void disableCulling()
    {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    public static void render(Lighting lighting, Camera camera)
    {
        prepare();

        //entity render
        shader.start();
        shader.loadSkyColor(red, green, blue);
        shader.loadLighting(lighting);
        shader.loadViewMatrix(camera);
        render.render(entities);
        shader.stop();

        // terrain render
        terrainShader.start();
        terrainShader.loadSkyColor(red, green, blue);
        terrainShader.loadLighting(lighting);
        terrainShader.loadViewMatrix(camera);
        terrainRender.render(terrains);
        terrainShader.stop();

        terrains.clear(); // very important!!
        entities.clear(); // very important!!
    }

    public static void processTerrain(Terrain terrain)
    {
        terrains.add(terrain);
    }

    public static void processEntity(Entity entity)
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

    public static void prepare()
    {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(red, green, blue, 1);
    }

    public static void dispose()
    {
        shader.dispose();
        terrainShader.dispose();
    }

    private static Matrix4f createProjectionMatrix()
    {
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float yScale = (float) (1f / Math.tan(Math.toRadians(fieldOfView / 2f)) * aspectRatio);
        float xScale = yScale / aspectRatio;
        float frustumLength = farPlane - nearPlane;

        projection = new Matrix4f();
        projection.m00 = xScale;
        projection.m11 = yScale;

        projection.m22 = -((farPlane + nearPlane) / frustumLength);
        projection.m23 = -1f;
        projection.m32 = -((2f * nearPlane * farPlane) / frustumLength);
        projection.m33 = 0f;
        return projection;

    }

    public static void processAllEntities(List<Entity> entities)
    {
        for(Entity entity : entities)
        {
            processEntity(entity);
        }
    }

    public static void processAllTerrains(Terrain[] terrains)
    {
        for(Terrain terrain : terrains)
        {
            processTerrain(terrain);
        }
    }

    public static void render(Lighting lighting, Player player)
    {
        render(lighting, player.camera);
    }
}

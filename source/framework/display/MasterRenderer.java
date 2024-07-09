package framework.display;

import framework.entity.Camera;
import framework.entity.Entity;
import framework.entity.EntityRenderer;
import framework.entity.Light;
import framework.model.TexturedModel;
import framework.normals.NormalMappingRenderer;
import framework.entity.EntityShader;
import framework.skybox.SkyboxRenderer;
import framework.terrain.TerrainRenderer;
import framework.terrain.TerrainShader;
import framework.shadow.ShadowMapMasterRenderer;
import framework.terrain.Terrain;
import framework.water.WaterFrameBuffers;
import framework.water.WaterRenderer;
import framework.water.WaterShader;
import framework.water.WaterTile;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRenderer
{
    public static float fieldOfView = 60;
    public static float nearPlane = .1f;
    public static float farPlane = 100_000f;
    public static float red = 1;
    public static float green = 1f;
    public static  float blue = 1f;
    private static Matrix4f projection;
    private static EntityShader entityShader;
    private static EntityRenderer entityRenderer;
    private static WaterRenderer waterRenderer;
    private static WaterShader waterShader;
    private static Map<TexturedModel, List<Entity>> entities;
    private static Map<TexturedModel, List<Entity>> normalEntities;
    private static TerrainRenderer terrainRender;
    private static TerrainShader terrainShader;
    private static List<Terrain> terrains;
    public static SkyboxRenderer skyboxRenderer;
    public static WaterFrameBuffers buffer;
    private static NormalMappingRenderer normalMappingRenderer;
    private static ShadowMapMasterRenderer shadowMap;

    public static void setRenderer()
    {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        createProjectionMatrix();
        entities = new HashMap<>();
        normalEntities = new HashMap<>();
        terrains = new ArrayList<>();
        waterShader = new WaterShader();
        entityShader = new EntityShader();
        buffer = new WaterFrameBuffers();
        terrainShader = new TerrainShader();
        waterRenderer = new WaterRenderer(waterShader, projection, buffer);
        entityRenderer = new EntityRenderer(entityShader, projection);
        terrainRender = new TerrainRenderer(terrainShader, projection);
        skyboxRenderer = new SkyboxRenderer(projection);
        normalMappingRenderer = new NormalMappingRenderer(projection);
    }

    public static Matrix4f getProjectionMatrix()
    {
        return projection;
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

    public static void renderLights(List<Light> lights, Camera camera, Vector4f plane)
    {
        prepare();

        //entity render
        entityShader.start();
        entityShader.loadSkyColor(red, green, blue);
        entityShader.loadClipPlane(plane);
        entityShader.loadLightAmount(lights.size());
        entityShader.loadLights(lights);
        entityShader.loadViewMatrix(camera);
        entityRenderer.render(entities, shadowMap.getToShadowMapSpaceMatrix());
        entityShader.stop();

        // normal entities
        normalMappingRenderer.render(normalEntities, plane, lights, camera);

        // terrain render
        terrainShader.start();
        terrainShader.loadSkyColor(red, green, blue);
        terrainShader.loadClipPlane(plane);
        terrainShader.loadLightAmount(lights.size());
        terrainShader.loadLights(lights);
        terrainShader.loadViewMatrix(camera);
        terrainRender.render(terrains, shadowMap.getToShadowMapSpaceMatrix());
        terrainShader.stop();

        // skybox rendering
        skyboxRenderer.render(camera);

        // very important!!
        terrains.clear();
        entities.clear();
        normalEntities.clear();
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

    public static void processNormalMappedEntity(Entity entity)
    {
        TexturedModel texturedModel = entity.getModel();
        List<Entity> batch = normalEntities.get(texturedModel);
        if(batch != null)
        {
            batch.add(entity);
        }
        else
        {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            normalEntities.put(texturedModel, newBatch);
        }
    }

    public static void prepare()
    {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClearColor(0, 0, 0, 0);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL13.glActiveTexture(GL13.GL_TEXTURE5);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, getShadowMapTexture());
    }

    public static void renderShadowMap(List<Entity> list, Light light)
    {
        for(Entity entity : list)
        {
            processEntity(entity);
        }
        shadowMap.render(entities, light);
        entities.clear();
    }

    public static int getShadowMapTexture()
    {
        return shadowMap.getShadowMap();
    }

    public static void dispose()
    {
        buffer.dispose();
        waterShader.dispose();
        entityShader.dispose();
        terrainShader.dispose();
        normalMappingRenderer.dispose();
        shadowMap.dispose();
    }

    private static void createProjectionMatrix()
    {
        projection = new Matrix4f();
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float yScale = (float) (1f / Math.tan(Math.toRadians(fieldOfView / 2f)));
        float xScale = yScale / aspectRatio;
        float frustumLength = farPlane - nearPlane;

        projection.m00 = xScale;
        projection.m11 = yScale;
        projection.m22 = -((farPlane + nearPlane) / frustumLength);
        projection.m23 = -1f;
        projection.m32 = -((2f * nearPlane * farPlane) / frustumLength);
        projection.m33 = 0f;
    }

    public static void processAllNormalMappedEntities(List<Entity> entities)
    {
        for(Entity entity : entities)
        {
            processNormalMappedEntity(entity);
        }
    }

    public static void processAllEntities(List<Entity> entities)
    {
        for(Entity entity : entities)
        {
            processEntity(entity);
        }
    }

    public static void setShadowMap(Camera camera)
    {
        shadowMap = new ShadowMapMasterRenderer(camera);
    }

    public static void renderWaters(List<WaterTile> waters, Camera camera, Light light)
    {
        waterRenderer.render(waters, camera, light);
    }

    public static void processAllTerrains(Terrain[] terrains)
    {
        for(Terrain terrain : terrains)
        {
            processTerrain(terrain);
        }
    }
}

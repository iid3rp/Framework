package framework.renderer;

import framework.hardware.Display;
import framework.entity.Camera;
import framework.entity.Entity;
import framework.entity.Light;
import framework.model.TexturedModel;
import framework.shader.EntityShader;
import framework.shader.TerrainShader;
import framework.shadow.ShadowMapMasterRenderer;
import framework.skybox.SkyboxRenderer;
import framework.terrains.Terrain;
import framework.water.WaterFrameBufferObject;
import framework.water.WaterRenderer;
import framework.water.WaterShader;
import framework.water.WaterTile;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

public class MasterRenderer {
    public static final float FOV = 60;
    public static final float NEAR_PLANE = 0.1f;
    public static final float FAR_PLANE = 100_000;
    public static final float SKY_RED = 0.95f;
    public static final float SKY_GREEN = 0.9f;
    public static final float SKY_BLUE = 0.67f;

    private static EntityShader entityShader;
    private static EntityRenderer entityRenderer;
    public static Map<TexturedModel, List<Entity>> entities;
    private static Matrix4f projectionMatrix;
    private static TerrainRenderer terrainRenderer;
    private static SkyboxRenderer skyboxRenderer;
    private static TerrainShader terrainShader;
    private static List<Terrain> terrainList;
    private static WaterShader waterShader;
    private static WaterRenderer waterRenderer;
    public static WaterFrameBufferObject buffer;

    public static ShadowMapMasterRenderer shadowRender;

    public static void setRenderer(Camera camera)
    {
        enableCulling();
        createProjectionMatrix();

        entityShader = new EntityShader();
        terrainShader = new TerrainShader();
        entities = new HashMap<>();

        entityRenderer = new EntityRenderer(entityShader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
        terrainList = new ArrayList<>();
        skyboxRenderer = new SkyboxRenderer(projectionMatrix);
        waterShader = new WaterShader();
        buffer = new WaterFrameBufferObject();
        waterRenderer = new WaterRenderer(waterShader, projectionMatrix, buffer);
        shadowRender = new ShadowMapMasterRenderer(camera);
    }

    public static void enableCulling() {
        // don't texture surface with normal vectors facing away from the "camera"
        // don't render back the faces of a model.
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
    }

    public static void disableCulling() {
        glDisable(GL_CULL_FACE);
    }

    public static void processEntity(Entity entity) {
        TexturedModel entityModel = entity.getTexturedModel();
        List<Entity> entityList = entities.get(entityModel);

        if (entityList == null) {
            List<Entity> newEntityList = new ArrayList<>();
            newEntityList.add(entity);
            entities.put(entityModel, newEntityList);
            return;
        }

        entityList.add(entity);
    }

    public static void render(List<Light> lights, Camera camera, Vector4f vec4) {
        prepare();

        entityShader.bind();
        entityShader.loadClipPlane(vec4);
        entityShader.loadSkyColor(SKY_RED, SKY_GREEN, SKY_BLUE);
        entityShader.loadLights(lights, camera);
        entityShader.loadViewMatrix(camera);
        entityRenderer.render(entities);
        entityShader.unbind();

        terrainShader.bind();
        terrainShader.loadClipPlane(vec4);
        terrainShader.loadSkyColor(SKY_RED, SKY_GREEN, SKY_BLUE);
        terrainShader.loadLights(lights);
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrainList, shadowRender.getToShadowMapSpaceMatrix());
        terrainShader.unbind();

        // skybox rendering
        skyboxRenderer.render(camera);

        entities.clear();
        terrainList.clear();
    }

    public static void processNormalMappedEntity(Entity entity)
    {
        TexturedModel texturedModel = entity.getTexturedModel();
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

    public static void processTerrain(Terrain terrain) {
        terrainList.add(terrain);
    }

    public static void renderShadowMap(List<Entity> ent, Light sun)
    {
        processAllEntities(ent);
        shadowRender.render(entities, sun);
        entities.clear();
    }

    public static int getShadowMapTexture()
    {
        return shadowRender.getShadowMap();
    }

    public static void dispose() {
        shadowRender.dispose();
        entityShader.dispose();
        terrainShader.dispose();
    }

    private static void prepare() {
        glEnable(GL_DEPTH_TEST);    // test which triangles are in front and render them in the correct order
        glClearColor(SKY_RED, SKY_GREEN, SKY_BLUE, 1);      // Load selected color into the color buffer
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);     // Clear the screen and draw with color in color buffer

        GL13.glActiveTexture(GL13.GL_TEXTURE5);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, getShadowMapTexture());
    }

    private static void createProjectionMatrix() {
        projectionMatrix = new Matrix4f();
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float yScale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
        float xScale = yScale / aspectRatio;
        float frustumLength = FAR_PLANE - NEAR_PLANE;

        projectionMatrix.m00(xScale);
        projectionMatrix.m11(yScale);
        projectionMatrix.m22(-((FAR_PLANE + NEAR_PLANE) / frustumLength));
        projectionMatrix.m23(-1);
        projectionMatrix.m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustumLength));
        projectionMatrix.m33(0);
    }

    public static void renderWaters(List<WaterTile> waters, Camera camera, Light light)
    {
        waterRenderer.render(waters, camera, light);
    }

    public static Matrix4f getProjectionMatrix()
    {
        return projectionMatrix;
    }

    public static void processAllEntities(List<Entity> entities)
    {
        for(Entity entity : entities)
        {
            processEntity(entity);
        }
    }
}

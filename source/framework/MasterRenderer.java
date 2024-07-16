package framework;

import framework.entity.Camera;
import framework.entity.Entity;
import framework.entity.Light;
import framework.model.TexturedModel;
import framework.shader.EntityShader;
import framework.shader.TerrainShader;
import framework.skybox.SkyboxRenderer;
import framework.terrains.Terrain;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

public class MasterRenderer {
    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 100_000;
    private static final float SKY_RED = 0.95f;
    private static final float SKY_GREEN = 0.9f;
    private static final float SKY_BLUE = 0.67f;

    private static EntityShader entityShader;
    private static EntityRenderer entityRenderer;
    private static Map<TexturedModel, List<Entity>> entities;
    private static Matrix4f projectionMatrix;
    private static TerrainRenderer terrainRenderer;
    private static SkyboxRenderer skyboxRenderer;
    private static TerrainShader terrainShader;
    private static List<Terrain> terrainList;

    public static void setRenderer()
    {
        enableCulling();
        entityShader = new EntityShader();
        terrainShader = new TerrainShader();
        entities = new HashMap<>();
        createProjectionMatrix();
        entityRenderer = new EntityRenderer(entityShader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
        terrainList = new ArrayList<>();
        skyboxRenderer = new SkyboxRenderer(projectionMatrix);
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

    public static void render(List<Light> lights, Camera camera) {
        prepare();

        entityShader.bind();
        entityShader.loadSkyColor(SKY_RED, SKY_GREEN, SKY_BLUE);
        entityShader.loadLights(lights);
        entityShader.loadViewMatrix(camera);
        entityRenderer.render(entities);
        entityShader.unbind();

        terrainShader.bind();
        terrainShader.loadSkyColor(SKY_RED, SKY_GREEN, SKY_BLUE);
        terrainShader.loadLights(lights);
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrainList);
        terrainShader.unbind();

        // skybox rendering
        skyboxRenderer.render(camera);

        entities.clear();
        terrainList.clear();
    }

    public static void processTerrain(Terrain terrain) {
        terrainList.add(terrain);
    }

    public static void destroy() {
        entityShader.dispose();
        terrainShader.dispose();
    }

    private static void prepare() {
        glEnable(GL_DEPTH_TEST);    // test which triangles are in front and render them in the correct order
        glClearColor(SKY_RED, SKY_GREEN, SKY_BLUE, 1);      // Load selected color into the color buffer
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);     // Clear the screen and draw with color in color buffer
    }

    private static void createProjectionMatrix() {
        float aspectRatio = (float) DisplayManager.getWindowWidth() / (float) DisplayManager.getWindowHeight();
        float yScale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float xScale = yScale / aspectRatio;
        float frustumLength = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00(xScale);
        projectionMatrix.m11(yScale);
        projectionMatrix.m22(-((FAR_PLANE + NEAR_PLANE) / frustumLength));
        projectionMatrix.m23(-1);
        projectionMatrix.m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustumLength));
        projectionMatrix.m33(0);
    }
}

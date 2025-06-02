package framework.renderer;

import framework.environment.Environment;
import framework.hardware.Display;
import framework.entity.Camera;
import framework.entity.Entity;
import framework.entity.Light;
import framework.lang.Mat4;
import framework.lang.Vec4;
import framework.model.TexturedModel;
import framework.shader.EntityShader;
import framework.shader.TerrainShader;
import framework.shadow.ShadowBox;
import framework.shadow.ShadowMapMasterRenderer;
import framework.skybox.SkyboxRenderer;
import framework.terrain.Terrain;
import framework.util.LinkList;
import framework.util.Map;
import framework.util.OctList;
import framework.water.WaterFrameBufferObject;
import framework.water.WaterRenderer;
import framework.water.WaterShader;
import framework.water.WaterTile;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;


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
    public static Map<TexturedModel, LinkList<Entity>> entities;
    private static Mat4 projectionMatrix;
    private static TerrainRenderer terrainRenderer;
    private static SkyboxRenderer skyboxRenderer;
    private static TerrainShader terrainShader;
    private static LinkList<Terrain> terrainList;
    private static WaterShader waterShader;
    private static WaterRenderer waterRenderer;
    public static WaterFrameBufferObject buffer;

    public static OctList<Entity> spaceEntities;

    public static ShadowMapMasterRenderer shadowRender;

    public static void setRenderer(Camera camera)
    {
        enableCulling();
        createProjectionMatrix();

        entityShader = new EntityShader();
        terrainShader = new TerrainShader();
        entities = new Map<>();
        spaceEntities = new OctList<>();

        entityRenderer = new EntityRenderer(entityShader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
        terrainList = new LinkList<>();
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
        LinkList<Entity> entityList = entities.get(entityModel);

        if (entityList == null) {
            LinkList<Entity> newEntityList = new LinkList<>();
            newEntityList.addAll(entity);
            entities.put(entityModel, newEntityList);
            return;
        }

        entityList.addAll(entity);
    }

    public static void render(LinkList<Light> lights, Camera camera, Vec4 vec4) {
        prepare();

        entityShader.bind();
        entityShader.loadClipPlane(vec4);
        entityShader.loadSkyColor(SKY_RED, SKY_GREEN, SKY_BLUE);
        entityShader.loadLights(lights, camera);
        entityShader.loadViewMatrix(camera);
        entityRenderer.render(entities, shadowRender.getToShadowMapSpaceMatrix());
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
        spaceEntities.clear();
    }

    @Deprecated
    public static void processNormalMappedEntity(Entity entity)
    {
        TexturedModel texturedModel = entity.getTexturedModel();
        LinkList<Entity> batch = entities.get(texturedModel);
        if(batch != null)
            batch.addAll(entity);
        else
        {
            LinkList<Entity> newBatch = new LinkList<>();
            newBatch.addAll(entity);
            entities.put(texturedModel, newBatch);
        }
    }

    public static void processTerrain(Terrain terrain) {
        terrainList.addAll(terrain);
    }

    public static void renderShadowMap(LinkList<Entity> ent, Light sun)
    {
        processAllEntities(ent);
        shadowRender.shadowBox.SHADOW_DISTANCE = Environment.getScene().getCamera().getDistance();
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
        projectionMatrix = new Mat4();
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float yScale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
        float xScale = yScale / aspectRatio;
        float frustumLength = FAR_PLANE - NEAR_PLANE;

        projectionMatrix.m00 = xScale;
        projectionMatrix.m11 = yScale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustumLength);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustumLength);
        projectionMatrix.m33 = 0;
    }

    public static void renderWaters(LinkList<WaterTile> waters, Camera camera, Light light)
    {
        waterRenderer.render(waters, camera, light);
    }

    public static Mat4 getProjectionMatrix()
    {
        return projectionMatrix;
    }

    public static void processAllEntities(LinkList<Entity> entities)
    {
//        Camera camera = Environment.getScene().getCamera();
//        spaceEntities.setPosition(camera.getPosition());
//
//        for(Entity entity : entities)
//            spaceEntities.add(entity,entity.getPosition());
//
//        LinkList<Entity> culledEntities = spaceEntities.getQuery(camera.getYaw(), camera.getPitch(), FOV);
//
//        for(Entity entity : culledEntities)
//            processEntity(entity);

        for(Entity entity : entities)
            processEntity(entity);

        //System.out.println(culledEntities.size());
    }
}

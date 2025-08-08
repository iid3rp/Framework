package framework.renderer;

import framework.entity.Entity;
import framework.environment.Scene;
import framework.hardware.Display;
import framework.lang.Mat4;
import framework.model.Model;
import framework.shader.GLShader;
import framework.textures.Texture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static framework.shader.GLShader.EntityShader;
import static framework.shader.GLShader.disableVertexArrays;
import static framework.shader.GLShader.enableVertexArrays;
import static org.lwjgl.opengl.GL30.*;

public class MasterRenderer {
    public static float fov = 70;
    public static float nearPlane = 0.1f;
    public static float farPlane = 100_000;
    public static float SKY_RED = 0.95f;
    public static float SKY_GREEN = 0.9f;
    public static float SKY_BLUE = 0.67f;
    private static Mat4 projectionMatrix;
    private static Map<Texture, Map<Model, List<Entity>>> entry;


    public static void setRenderer()
    {
        entry = new HashMap<>();
        projectionMatrix = new Mat4();
        updateProjectionMatrix();
        GLShader.initializeShaders();
        GLShader.bind(EntityShader.program);
        GLShader.loadUniform("projectionMatrix", projectionMatrix);
        GLShader.unbind();
        enableCulling();
        enableDepthTest();
    }

    private static void enableDepthTest()
    {
        glEnable(GL_DEPTH_TEST);
    }

    public static void enableCulling() {
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
    }

    public static void disableCulling() {
        glDisable(GL_CULL_FACE);
    }

    public static void processEntity() {

    }

    public static void prepare() {
        glEnable(GL_DEPTH_TEST);    // test which triangles are in front and render them in the correct order
        glClearColor(0,0, 0, 1);      // Load selected color into the color buffer
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);     // Clear the screen and draw with color in color buffer
    }

    private static float aspectRatio;
    private static float yScale;
    private static float xScale;
    private static float frustumLength;

    public static void updateProjectionMatrix()
    {
        projectionMatrix.identity();
        aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        yScale = (float) (1f / Math.tan(Math.toRadians(fov / 2f)));
        xScale = yScale / aspectRatio;
        frustumLength = farPlane - nearPlane;

        projectionMatrix.m00 = xScale;
        projectionMatrix.m11 = yScale;
        projectionMatrix.m22 = -((farPlane + nearPlane) / frustumLength);
        projectionMatrix.m23 = -1f;
        projectionMatrix.m32 = -((2f * nearPlane * farPlane) / frustumLength);
        projectionMatrix.m33 = 0f;
    }

    public static Mat4 getProjectionMatrix()
    {
        return projectionMatrix;
    }

    public static void render(Scene scene) {
        GLShader.bind(EntityShader.program);
        enableVertexArrays();

        processScene(scene);

        processEntities(scene.entities);
        renderBatchedEntities();

        disableVertexArrays();
        GLShader.unbind();
    }

    private static void processScene(Scene scene)
    {
        GLShader.loadUniform("viewMatrix", scene.camera.getViewMatrix());
        GLShader.loadLights(scene.lights);
    }


    private static void processEntities(List<Entity> entities)
    {
        for(Entity e : entities)
        {
            Map<Model, List<Entity>> modelMap = entry.get(e.getModel().getTexture());
            List<Entity> entityList;
            if(modelMap == null)
            {
                modelMap = new HashMap<>();
                entry.put(e.getModel().getTexture(), modelMap); // Add this line
                entityList = new ArrayList<>();
                modelMap.put(e.getModel().getModel(), entityList);
            }
            else
            {
                entityList = modelMap.computeIfAbsent(e.getModel().getModel(), k -> new ArrayList<>());
            }
            entityList.add(e);
        }
    }

    private static void renderBatchedEntities() {
        for (Entry<Texture, Map<Model, List<Entity>>> textureEntry : entry.entrySet()) {
            Texture texture = textureEntry.getKey();
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, texture.getTextureId());
            GLShader.loadTexture(texture);

            Map<Model, List<Entity>> modelsMap = textureEntry.getValue();
            for(Entry<Model, List<Entity>> modelEntry : modelsMap.entrySet()) {
                Model model = modelEntry.getKey();
                glBindVertexArray(model.getVaoId());

                List<Entity> entities = modelEntry.getValue();
                for(Entity e : entities) {

                    GLShader.loadUniform("transformationMatrix", e.getTransformationMatrix());
                    GLShader.loadUniform("hasTexture", true);
                    if(e.getModel().getTexture().getID() == 0)
                        GLShader.loadUniform("backgroundColor", e.getRed(), e.getGreen(), e.getBlue(), e.getAlpha());

                    glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_UNSIGNED_INT, 0);
                }
            }
        }

        entry.clear();
    }
}

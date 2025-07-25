package framework.renderer;



import framework.entity.Entity;
import framework.environment.Engine;
import framework.hardware.Display;
import framework.lang.Mat4;
import framework.model.Model;
import framework.shader.GLShader;

import static framework.shader.GLShader.EntityShader;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;

public class MasterRenderer {
    public static float fov = 70;
    public static float nearPlane = 0.1f;
    public static float farPlane = 100_000;
    public static float SKY_RED = 0.95f;
    public static float SKY_GREEN = 0.9f;
    public static float SKY_BLUE = 0.67f;
    private static Mat4 projectionMatrix;


    public static void setRenderer()
    {
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

    public static void render(Entity entity) {
        Model model = entity.getModel().getModel();
        glBindVertexArray(model.getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // enable uniforms
        GLShader.loadUniform("transformationMatrix", entity.getTransformationMatrix());
        GLShader.loadUniform("viewMatrix", Engine.test.getViewMatrix());
        GLShader.loadUniform("hasTexture", entity.getModel().getTexture().getTextureId() != 0);
        GLShader.loadUniform("backgroundColor", entity.getRed(), entity.getGreen(), entity.getBlue(), entity.getAlpha());

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, entity.getModel().getTexture().getTextureId());
        glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
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
}

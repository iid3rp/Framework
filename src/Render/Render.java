package Render;

import Entities.Entity;
import Shaders.StaticShader;
import Toolbox.MatrixMultiplication;
import model.Model;
import model.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.NVVertexAttribInteger64bit;
import org.lwjgl.util.vector.Matrix4f;
import util.Intention;

public class Render
{
    private static float fieldOfView = 80;
    private static float nearPlane = .1f;
    private static float farPlane = 1000f;
    private Matrix4f projection;

    public Render(StaticShader shader)
    {
        createProjectionMatrix();
        shader.start();
        shader.loadProjectionMatrix(projection);
        shader.stop();
    }

    public void prepare()
    {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0, 0, 0, 1);
        Display.update();
    }

    @Intention(design = "for textured models only")
    @Deprecated
    public void render(TexturedModel m)
    {
        Model model = m.getModel();
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, m.getTexture().getID());
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

    public void render(Entity entity, StaticShader shader)
    {
        TexturedModel txtModel = entity.getModel();
        Model model = txtModel.getModel();
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        @Intention(design = "for addtional texture and shader effect")
        Matrix4f matrix4f = MatrixMultiplication.transformMatrix(
                entity.getPosition(),
                entity.getRotationX(),
                entity.getRotationY(),
                entity.getRotationZ(),
                entity.getScale());
        shader.loadTransformMatrix(matrix4f);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, txtModel.getTexture().getID());
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

    private void createProjectionMatrix()
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

    }
}

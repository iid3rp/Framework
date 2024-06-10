package render;

import entity.Entity;
import shader.Shader;
import texture.Texture;
import toolbox.MatrixMultiplication;
import model.Model;
import model.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import util.Intention;

import java.util.List;
import java.util.Map;

public class Render
{
    private static float fieldOfView = 70;
    private static float nearPlane = .1f;
    private static float farPlane = 1000f;
    private Matrix4f projection;
    private Shader shader;

    public Render(Shader shader)
    {
        this.shader = shader;
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
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

    @Intention(design = "for single entity each time only")
    @Deprecated
    public void render(Entity entity, Shader shader)
    {
        TexturedModel txtModel = entity.getModel();
        Model model = txtModel.getModel();
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        @Intention(design = "for additional texture and shader effect")
        Matrix4f matrix4f = MatrixMultiplication.transformMatrix(
                entity.getPosition(),
                entity.getRotationX(),
                entity.getRotationY(),
                entity.getRotationZ(),
                entity.getScale());
        shader.loadTransformMatrix(matrix4f);

        Texture texture = txtModel.getTexture();
        shader.loadShine(texture.getShineDampening(), texture.getReflectivity());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, txtModel.getTexture().getID());
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    public void render(Map<TexturedModel, List<Entity>> entities)
    {
        for(TexturedModel texturedModel : entities.keySet())
        {
            prepareTexturedModels(texturedModel);
            List<Entity> batch = entities.get(texturedModel);
            for(Entity entity : batch)
            {
                prepareEntity(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES,
                        texturedModel.getModel().getVertexCount(),
                        GL11.GL_UNSIGNED_INT, 0);
            }
            unbindTexturedModels();
        }
    }

    public void prepareTexturedModels(TexturedModel texturedModel)
    {
        Model model = texturedModel.getModel();
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        Texture texture = texturedModel.getTexture();
        shader.loadShine(texture.getShineDampening(), texture.getReflectivity());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().getID());
    }

    private void unbindTexturedModels()
    {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private void prepareEntity(Entity entity)
    {
        @Intention(design = "for additional texture and shader effect")
        Matrix4f matrix4f = MatrixMultiplication.transformMatrix(
                entity.getPosition(),
                entity.getRotationX(),
                entity.getRotationY(),
                entity.getRotationZ(),
                entity.getScale());
        shader.loadTransformMatrix(matrix4f);
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

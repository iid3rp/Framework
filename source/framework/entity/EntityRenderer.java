package framework.entity;

import framework.model.Model;
import framework.model.TexturedModel;
import framework.display.MasterRenderer;
import framework.texture.Texture;
import framework.toolbox.GeomMath;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;
import util.Intention;

import java.util.List;
import java.util.Map;

public class EntityRenderer
{
    private EntityShader shader;
    public EntityRenderer(EntityShader shader, Matrix4f projection)
    {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projection);
        shader.connectTextureUnits();
        shader.stop();
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
    public void render(Entity entity, EntityShader shader)
    {
        TexturedModel txtModel = entity.getModel();
        Model model = txtModel.getModel();
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        @Intention(design = "for additional texture and shader effect")
        Matrix4f matrix4f = GeomMath.createTransformationMatrix(
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

    public void render(Map<TexturedModel, List<Entity>> entities, Matrix4f shadowMap, Vector4f plane)
    {
        shader.loadToShadowMapSpaceMatrix(shadowMap);
        for(TexturedModel texturedModel : entities.keySet())
        {
            prepareTexturedModel(texturedModel);
            List<Entity> batch = entities.get(texturedModel);
            for(Entity entity : batch)
            {
                // preparing entities with efficiency...
                // with an offset of 10 for a bit of bias...
                if(plane.y == -1 && entity.getPosition().y >= plane.w - 10)
                {
                    prepareEntity(entity);
                    GL11.glDrawElements(GL11.GL_TRIANGLES,
                            texturedModel.getModel().getVertexCount(),
                            GL11.GL_UNSIGNED_INT, 0);
                }
                if(plane.y == 1 && entity.getPosition().y <= plane.w + 10)
                {
                    prepareEntity(entity);
                    GL11.glDrawElements(GL11.GL_TRIANGLES,
                            texturedModel.getModel().getVertexCount(),
                            GL11.GL_UNSIGNED_INT, 0);
                }
            }
            unbindTexturedModels();
        }
    }

    public void prepareTexturedModel(TexturedModel texturedModel)
    {
        Model model = texturedModel.getModel();
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        Texture texture = texturedModel.getTexture();
        shader.loadNumberOfRows(texture.getNumberOfRows());
        if(texture.hasTransparency())
        {
            MasterRenderer.disableCulling();
        }
        shader.loadFakeLighting(texture.isUseFakeLighting());
        shader.loadShine(texture.getShineDampening(), texture.getReflectivity());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().getID());
        shader.loadUsesSpecularMap(texture.hasSpecularMap());
        if(texture.hasSpecularMap())
        {
            GL13.glActiveTexture(GL13.GL_TEXTURE1);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getSpecularMap());
        }
    }

    private void unbindTexturedModels()
    {
        MasterRenderer.enableCulling();
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private void prepareEntity(Entity entity)
    {
        @Intention(design = "for additional texture and shader effect")
        Matrix4f matrix4f = GeomMath.createTransformationMatrix(
                entity.getPosition(),
                entity.getRotationX(),
                entity.getRotationY(),
                entity.getRotationZ(),
                entity.getScale());
        shader.loadTransformMatrix(matrix4f);
        shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
    }
}
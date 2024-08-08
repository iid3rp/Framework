package framework.swing;

import framework.h.Display;
import framework.loader.ModelLoader;
import framework.model.Model;
import framework.util.GeomMath;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;

public class GUIRenderer
{
    private final Model quad;
    private GUIShader shader;
    private Vector2f size;
    public GUIRenderer()
    {
        float[] positions = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1};
        quad = ModelLoader.loadToVao(positions, 2);
        shader = new GUIShader();
        size = new Vector2f(1, 1);
    }

    public void setSize(int x, int y)
    {
        float normalizeX = (float) x / Display.getWidth();
        float normalizeY = (float) y / Display.getHeight();
        size.set(normalizeX, normalizeY);
    }

    public void render(List<GUITexture> guis)
    {
        shader.bind();
        GL30.glBindVertexArray(quad.getVaoId());
        GL20.glEnableVertexAttribArray(0);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        // rendering
        for(GUITexture texture : guis)
        {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTexture());
            GL13.glActiveTexture(GL13.GL_TEXTURE1);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getClipTexture());
            Matrix4f matrix = GeomMath.createTransformationMatrix(texture.getPosition(), texture.getRotation(), texture.getScale());
            shader.loadPosition(texture.getRawPosition());
            shader.loadSize(texture.getSize());
            shader.loadScale(texture.getScale());
            shader.loadTransformation(matrix);
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
        }
        GL11.glEnable((GL11.GL_DEPTH_TEST));
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.unbind();
    }

    public void dispose()
    {
        shader.dispose();
    }
}

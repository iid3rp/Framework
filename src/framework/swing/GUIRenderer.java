package framework.swing;

import framework.environment.Scene;
import framework.hardware.Display;
import framework.lang.Mat4;
import framework.lang.Vec2;
import framework.lang.Vec3;
import framework.loader.ModelLoader;
import framework.model.Model;
import framework.lang.Math;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;

public class GUIRenderer
{
    private final Model quad;
    private GUIShader shader;
    private Vec2 size;

    public static float[] positions =
    {
            -1, -1,
            -1, 1,
            1, -1,
            1, -1,
            -1, 1,
            1, 1
    };

    public static float[] coords = {
            0, 1,
            0, 0,
            1, 1,
            1, 1,
            0, 0,
            1, 0
    };


    public GUIRenderer()
    {
        quad = ModelLoader.loadToVao(positions, coords);
        shader = new GUIShader();
        size = new Vec2(1, 1);
    }

    public void setSize(int x, int y)
    {
        float normalizeX = (float) x / Display.getWidth();
        float normalizeY = (float) y / Display.getHeight();
        size.set(normalizeX, normalizeY);
    }

    public void render(List<Container> guis)
    {
        shader.bind();
        GL30.glBindVertexArray(quad.getVaoId());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        // rendering
        for(Container texture : guis)
        {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTexture());
            GL13.glActiveTexture(GL13.GL_TEXTURE1);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getClipTexture());
            Mat4 matrix = Math.createTransformationMatrix(texture.getPosition(), texture.getRotation(), texture.getSize());
            Mat4 displayMatrix = Math.createTransformationMatrix(Scene.offset, new Vec3(),  new Vec2(1));

            shader.loadSize(texture.getSize());
            shader.loadScale(texture.getScale());
            shader.loadTransformation(matrix);
            shader.loadImageLocation(texture.getImageLocation());
            shader.loadDisplayMatrix(displayMatrix);

            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
        }
        GL11.glEnable((GL11.GL_DEPTH_TEST));
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.unbind();
    }

    public void dispose()
    {
        shader.dispose();
    }
}

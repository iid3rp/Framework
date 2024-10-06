package framework.swing;

import framework.environment.Scene;
import framework.hardware.Display;
import framework.lang.Mat4;
import framework.lang.Vec2;
import framework.lang.Vec3;
import framework.loader.ModelLoader;
import framework.model.Model;
import framework.lang.Math;

import java.util.List;

import static org.lwjgl.opengl.GL46.*;

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
        glBindVertexArray(quad.getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_CULL_FACE);
        glDisable(GL_DEPTH_TEST);
        // rendering
        for(Container texture : guis)
        {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, texture.getTexture());
            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, texture.getClipTexture());
            Mat4 matrix = Math.createTransformationMatrix(texture.getPosition(), texture.getRotation(), texture.getSize());
            Mat4 displayMatrix = Math.createTransformationMatrix(Scene.offset, new Vec3(),  new Vec2(1));

            shader.loadSize(texture.getSize());
            shader.loadScale(texture.getScale());
            shader.loadTransformation(matrix);
            shader.loadImageLocation(texture.getImageLocation());
            shader.loadDisplayMatrix(displayMatrix);

            glDrawArrays(GL_TRIANGLES, 0, quad.getVertexCount());
        }
        glEnable((GL_DEPTH_TEST));
        glEnable(GL_CULL_FACE);
        glDisable(GL_BLEND);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
        shader.unbind();
    }

    public void dispose()
    {
        shader.dispose();
    }
}

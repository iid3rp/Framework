package framework.fontExperiment;

import framework.util.GeomMath;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class TextEntityRenderer
{
    private static TextShader shader;
    public static void initialize()
    {
        shader = new TextShader();
    }

    public static void render(Text text)
    {
        shader.bind();
        GL30.glBindVertexArray(text.getQuad().getVaoId());
        GL20.glEnableVertexAttribArray(0);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        // rendering
        //for() {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            GL13.glActiveTexture(GL13.GL_TEXTURE1);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            Matrix4f matrix = GeomMath.createTransformationMatrix(null, null, null);
            Matrix4f invMatrix = GeomMath.createTransformationMatrix(null, null, null);
            shader.loadPosition(null);
            shader.loadSize(null);
            shader.loadScale(null);
            shader.loadTransformation(matrix);
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 0);
        //}
        GL11.glEnable((GL11.GL_DEPTH_TEST));
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.unbind();
    }
}

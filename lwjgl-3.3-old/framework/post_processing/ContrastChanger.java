package framework.post_processing;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class ContrastChanger
{
    private ImageRenderer renderer;
    private ContrastShader shader;

    public ContrastChanger(int width, int height)
    {
        shader = new ContrastShader();
        renderer = new ImageRenderer(width, height);
    }

    public void render(int texture)
    {
        shader.bind();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        renderer.renderQuad();
        shader.unbind();
    }

    public int getOutputTexture()
    {
        return renderer.getOutputTexture();
    }

    public void dispose()
    {
        renderer.dispose();
        shader.dispose();
    }
}

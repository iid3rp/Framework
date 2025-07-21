package framework.post_processing;

import framework.loader.TextureLoader;
import framework.textures.Texture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.awt.image.BufferedImage;
import java.nio.IntBuffer;

public class PixelPicker
{
    private PixelShader shader;
    private ImageRenderer renderer;
    public PixelPicker()
    {
        shader = new PixelShader();
        renderer = new ImageRenderer(20, 20);
    }

    public void render(FrameBufferObject object)
    {
        shader.bind();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, object.getColorTexture());
        renderer.renderQuad();
        shader.unbind();
    }

    public int getOutputTexture()
    {
        return renderer.getOutputTexture();
    }

    public void dispose()
    {
        shader.dispose();
        renderer.dispose();
    }
}

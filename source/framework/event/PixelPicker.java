package framework.event;

import framework.loader.TextureLoader;
import framework.textures.Texture;

import java.awt.image.BufferedImage;
import java.nio.IntBuffer;

public class PixelPicker
{
    private Texture texture;
    private BufferedImage image;
    public PixelPicker()
    {
        image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        texture = new Texture(TextureLoader.loadTexture(image));
    }

    public int getId()
    {
        return texture.getTextureId();
    }

    public IntBuffer getBuffer()
    {
        return texture.getDiffuseBuffer();
    }
}

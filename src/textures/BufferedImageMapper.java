package textures;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

public final class BufferedImageMapper
{
    private BufferedImageMapper() {}

    public static BufferedImage generateSpecularMap(BufferedImage image)
    {
        return null;
    }
    public static BufferedImage generateNormals(BufferedImage image)
    {
        return null;
    }

    public static BufferedImage generateTextureAtlas(List<BufferedImage> images, int rows, int powerResolution)
    {
        BufferedImage[] references = new BufferedImage[rows * rows];
        int index = 0;
        if(images == null || images.size() > (rows * rows))
        {
            throw new IllegalArgumentException();
        }
        else for(BufferedImage image : images)
        {
            references[index++] = image;
        }
        return generateTextureAtlas(references, rows, powerResolution);
    }

    public static BufferedImage generateTextureAtlas(BufferedImage[] image, int rows, int powerResolution)
    {
        int index = 0;
        int length = (int) Math.pow(2   , powerResolution);
        int textureLength = length / rows;
        BufferedImage reference = new BufferedImage(length, length, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = reference.createGraphics();

        if(image == null || image.length > (rows * rows))
        {
            throw new IllegalArgumentException();
        }
        else for(BufferedImage i : image)
        {
            // might continue
            // TODO: do this part when done specular maps
            // i = i;
        }
        return null;
    }

    public static BufferedImage generateHeightMap(float[] vertices)
    {
        return null;
    }

    public static BufferedImage generateDistortionMap(int seed)
    {
        return null;
    }
}

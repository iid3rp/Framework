package framework.loader;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import framework.resources.Resources;
import framework.textures.Texture;
import framework.utils.Buffer;

import javax.imageio.ImageIO;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.GL_TEXTURE_LOD_BIAS;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class TextureLoader {
    private int width, height;
    private int textureId;

    public TextureLoader(String path) {
        textureId = loadTexture(path);
    }

    private Texture generateTexture(String path)
    {
        Texture texture;
        int[] pixels = new int[0];
        try
        {
            BufferedImage image = ImageIO.read(
                    Objects.requireNonNull(
                            Resources.class.getResourceAsStream("textures/" + path)));
            width = image.getWidth();
            height = image.getHeight();
            pixels = image.getRGB(0, 0, width, height, null, 0, width);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            // the textures will be set here
            // diffuse maps are the textures itself
            // normal maps will be based on x, y, and z components
            // specular maps will have three elements:
            //      -> specular / reflectivity
            //      -> fake lighting
            //      -> blooming intensity
            int[][] textureData = new int[][]
            {
                    new int[width * height], // diffuse maps
                    new int[width * height], // normal (bump) maps
                    new int[width * height]  // specular maps
            };

            for(int i = 0; i < width * height; i++)
            {
                byte a = (byte) ((pixels[i] & 0xff000000) >> 24);
                byte r = (byte) ((pixels[i] & 0xff0000) >> 16);
                byte g = (byte) ((pixels[i] & 0xff00) >> 8);
                byte b = (byte) (pixels[i] & 0xff);

                // diffuse making, simple implementation
                textureData[0][i] = a << 24 | b << 16 | g << 8 | r; // diffuse map goes here

                // normal mapping... will have a different approach
                byte xPlus = 0;
                byte xMinus = 0;
                byte y = (byte) ((byte) ((r + g + b) / 3) / (255 / a));
                byte zPlus = 0;
                byte zMinus = 0;

                // making sure that it will be on bounds
                if(i + 1 <= width * height)
                    xPlus =  (byte) ((pixels[i + 1] & 0xff0000) >> 16);
                if(i - 1 >= 0)
                    xMinus =  (byte) ((pixels[i - 1] & 0xff0000) >> 16);
                if(i + width <= width * height)
                    zPlus = (byte) (pixels[i] & 0xff);
                if(i - width >= 0)
                    zMinus = (byte) (pixels[i] & 0xff);

                // checking the normality of the height based on its y value...
                byte x,  _x, __x;
                byte z, _z, __z;
                if(xPlus > y)
                    _x = (byte) (y - (xPlus - y));
                else _x = (byte) (y + (y - xPlus));
                if(xMinus > y)
                    __x = (byte) (y - (xMinus - y));
                else __x = (byte) (y + (y - xMinus));
                if(zPlus > y)
                    _z = (byte) (y - (zPlus - y));
                else _z = (byte) (y + (y - zPlus));
                if(zMinus > y)
                    __z = (byte) (y - (zMinus - y));
                else __z = (byte) (y + (y - zMinus));
                x = (byte) ((byte) (_x + __x) / 2);
                z = (byte) ((byte) (_z + __z) / 2);

                textureData[1][i] = 255 | z << 16 | y << 8 | x; // normal map goes here

                // specular mapping will have a different approach
                byte spec;
                byte fake;
                byte bloom;

                byte average = (byte) ((byte) ((r + g + b) / 3) / (255 / a)); // average color
                spec = average; // specular
                fake = average; // fake

                // bloom defines in which how strong it is based on the
                // highest spectrum regardless of the color channel.
                if(r > g)
                    if(r > b)
                        bloom = r;
                    else bloom = b;
                else if(g > b)
                    bloom = g;
                else bloom = b;

                textureData[2][i] = 255 | bloom << 16 | fake << 8 | spec; // specular map goes here
            }

            int[] textures = new int[textureData.length];

            // create the integer as the id
            for(int i = 0; i < textures.length; i++)
            {
                textures[i] = glGenTextures();
                glBindTexture(GL_TEXTURE_2D, textures[i]);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE,
                        Buffer.createIntBuffer(textureData[i]));
                glBindTexture(GL_TEXTURE_2D, 0);

                // add things into the ModelLoader class
                glGenerateMipmap(GL_TEXTURE_2D);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
                glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -0.4f);
                ModelLoader.textureList.add(textures[i]);
            }

            // create your own texture with all the stuff needed with just one reference
            texture = new Texture();
            texture.setTextureID(textures[0]);
            texture.setNormalMap(textures[1]);
            texture.setSpecularMap(textures[2]);
        }
        return texture;
    }

    private int loadTexture(String path) {
        int[] pixels = new int[0];
        int result;
        try
        {
            BufferedImage image = ImageIO.read(
                    Objects.requireNonNull(
                            Resources.class.getResourceAsStream("textures/" + path)));
            width = image.getWidth();
            height = image.getHeight();
            pixels = new int[width * height];
            pixels = image.getRGB(0, 0, width, height, pixels, 0, width);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            int[] data = new int[width * height];

            for(int i = 0; i < width * height; i++) {
                int a = (pixels[i] & 0xff000000) >> 24;
                int r = (pixels[i] & 0xff0000) >> 16;
                int g = (pixels[i] & 0xff00) >> 8;
                int b = (pixels[i] & 0xff);

                data[i] = a << 24 | b << 16 | g << 8 | r;
            }

            result = glGenTextures();

            glBindTexture(GL_TEXTURE_2D, result);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE,
                    Buffer.createIntBuffer(data));
            glBindTexture(GL_TEXTURE_2D, 0);
        }
        return result;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTextureId() {
        return textureId;
    }
}

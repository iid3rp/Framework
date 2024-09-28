package framework.loader;

import framework.Main;
import framework.textures.Texture;
import framework.util.Buffer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.Objects;

import javax.imageio.ImageIO;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.GL_TEXTURE_LOD_BIAS;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class TextureLoader
{

    public static Texture generateTexture(String path)
    {
        Texture texture;
        int[] pixels;
        int width;
        int height;
        IntBuffer[] buffers = new IntBuffer[3];

        try {
            BufferedImage image = ImageIO.read(
                    Objects.requireNonNull(Main.class.getResourceAsStream("framework/textures/" + path))
            );
            width = image.getWidth();
            height = image.getHeight();
            pixels = image.getRGB(0, 0, width, height, null, 0, width);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int[][] textureData = new int[3][width * height]; // Diffuse, normal, and specular maps

        int[][] sobelX = {
                {-1, 0, 1},
                {-2, 0, 2},
                {-1, 0, 1}
        };

        int[][] sobelY = {
                {-1, -2, -1},
                { 0,  0,  0},
                { 1,  2,  1}
        };

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int i = y * width + x;
                int pixel = pixels[i];
                int a = (pixel >> 24) & 0xFF;
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = pixel & 0xFF;

                // Diffuse map
                textureData[0][i] = ((a << 24) | (r << 16) | (g << 8) | b);

                // Apply Sobel filter to generate a normal map
                float gx = 0, gy = 0;

                for (int ky = -1; ky <= 1; ky++) {
                    for (int kx = -1; kx <= 1; kx++) {
                        int sampleX = Math.min(Math.max(x + kx, 0), width - 1);
                        int sampleY = Math.min(Math.max(y + ky, 0), height - 1);
                        int sampleIndex = sampleY * width + sampleX;
                        int samplePixel = pixels[sampleIndex];
                        int sampleGray = (int)(0.21f * ((samplePixel >> 16) & 0xFF) +
                                0.72f * ((samplePixel >> 8) & 0xFF) +
                                0.07f * (samplePixel & 0xFF)); // Accurate grayscale

                        gx += sampleGray * sobelX[ky + 1][kx + 1];
                        gy += sampleGray * sobelY[ky + 1][kx + 1];
                    }
                }

                // Normalize the gradient to form a normal vector
                float magnitude = (float) Math.sqrt(gx * gx + gy * gy + 1);
                int nx = (int) ((gx / magnitude + 1) * 127.5);
                int ny = (int) ((gy / magnitude + 1) * 127.5);
                int nz = (int) ((1 / magnitude) * 127.5);

                textureData[1][i] = ((255 << 24) | (nz << 16) | (ny << 8) | nx);

                // Specular mapping
                byte average = (byte) ((r + g + b) / 3);
                byte spec = average; // Specular
                byte fake = average; // Fake lighting

                // Bloom intensity
                byte bloom = (byte) Math.max(Math.max(r, g), b);

                textureData[2][i] = ((255 << 24) | (bloom << 16) | (fake << 8) | spec);
            }
        }

        buffers[0] = Buffer.createIntBuffer(textureData[0]);
        buffers[1] = Buffer.createIntBuffer(textureData[1]);
        buffers[2] = Buffer.createIntBuffer(textureData[2]);

        int[] textures = new int[3];

        // Create textures
        for (int i = 0; i < textures.length; i++) {
            textures[i] = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, textures[i]);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE,
                    Buffer.createIntBuffer(textureData[i]));
            glBindTexture(GL_TEXTURE_2D, 0);

            glGenerateMipmap(GL_TEXTURE_2D);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR_MIPMAP_LINEAR);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -0.4f);
            ModelLoader.textureList.add(textures[i]);
        }
        texture = new Texture();
        texture.setTextureID(textures[0]);
        texture.setNormalMap(textures[1]);
        texture.setSpecularMap(textures[2]);
        texture.setDiffuseBuffer(buffers[0]);
        texture.setNormalBuffer(buffers[1]);
        texture.setSpecularBuffer(buffers[2]);

        return texture;
    }

    public static int loadTexture(BufferedImage img)
    {
        int width = img.getWidth();
        int height = img.getHeight();
        int[] pixels = new int[width * height];
        pixels = img.getRGB(0, 0, width, height, pixels, 0, width);

        int[] data = new int[width * height];

        for(int i = 0; i < width * height; i++) {
            int a = (pixels[i] & 0xff000000) >> 24;
            int r = (pixels[i] & 0xff0000) >> 16;
            int g = (pixels[i] & 0xff00) >> 8;
            int b = (pixels[i] & 0xff);

            data[i] = a << 24 | b << 16 | g << 8 | r;
        }

        int result = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, result);
        // linear makes everything non-pixelated...
        // will be making a todo list for this...
        // to make this customizable...
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE,
                Buffer.createIntBuffer(data));
        glBindTexture(GL_TEXTURE_2D, 0);
        return result;
    }

    public static int loadTexture(String path) {
        int[] pixels = new int[0];
        int result;
        int width = 0;
        int height = 0;
        try
        {
            BufferedImage image = ImageIO.read(
                    Objects.requireNonNull(
                            Main.class.getResourceAsStream("framework/textures/" + path)));
            return loadTexture(image);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}

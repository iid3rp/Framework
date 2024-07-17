package framework.loader;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import framework.resources.Resources;
import framework.utils.Buffer;

import javax.imageio.ImageIO;

import static org.lwjgl.opengl.GL11.*;

public class TextureLoader {
    private int width, height;
    private int textureId;

    public TextureLoader(String path) {
        textureId = load(path);
    }

    private int load(String path) {
        int[] pixels;
        try {
            BufferedImage image = ImageIO.read(
                    Objects.requireNonNull(
                            Resources.class.getResourceAsStream("textures/" + path)));
            width = image.getWidth();
            height = image.getHeight();
            pixels = new int[width * height];
            pixels = image.getRGB(0, 0, width, height, pixels, 0, width);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int[] data = new int[width * height];

        for (int i = 0; i < width * height; i++) {
            assert pixels != null;
            int a = (pixels[i] & 0xff000000) >> 24;
            int r = (pixels[i] & 0xff0000) >> 16;
            int g = (pixels[i] & 0xff00) >> 8;
            int b = (pixels[i] & 0xff);

            data[i] = a << 24 | b << 16 | g << 8 | r;
        }

        int result = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, result);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, Buffer.createIntBuffer(data));
        glBindTexture(GL_TEXTURE_2D, 0);

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

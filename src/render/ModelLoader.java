package render;

import model.Model;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class ModelLoader
{
    private static final List<Integer> vao = new ArrayList<>();
    private static final List<Integer> vbo = new ArrayList<>();
    private static final List<Integer> txt = new ArrayList<>();

    public static Model loadToVAO(float[] pos, float[] coords, float[] normals, int[] indices)
    {
        int id = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributes(0, 3, pos);
        storeDataInAttributes(1, 2, coords);
        storeDataInAttributes(2, 3, normals);
        unbindVAO();
        return new Model(id, indices.length);
    }

    public static int loadTexture(String filename)
    {
        Texture texture = null;
        int textureID;
        try {
            texture = TextureLoader.getTexture("PNG", new FileInputStream("resources/textures/"  + filename + ".png"));
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
        finally
        {
            assert texture != null;
            textureID = texture.getTextureID();
            txt.add(textureID);
        }
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        return textureID;
    }

    public static void dispose()
    {
        for(int i : vao) GL30.glDeleteVertexArrays(i);
        for(int i : vbo) GL15.glDeleteBuffers(i);
        for(int i : txt) GL11.glDeleteTextures(i);
    }

    private static int createVAO()
    {
        int id = GL30.glGenVertexArrays();
        vao.add(id);
        GL30.glBindVertexArray(id);
        return id;
    }

    private static void bindIndicesBuffer(int[] indices)
    {
        int vboID = GL15.glGenBuffers();
        vbo.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private static IntBuffer storeDataIntBuffer(int[] data)
    {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private static void storeDataInAttributes(int i, int coordinationSize, float[] f)
    {
        int id = GL15.glGenBuffers();
        vbo.add(id);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id);
        FloatBuffer b = storeDataInFloat(f);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, b, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(i, coordinationSize, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private static void unbindVAO()
    {
        GL30.glBindVertexArray(0);
    }

    private static FloatBuffer storeDataInFloat(float[] f)
    {
        FloatBuffer b = BufferUtils.createFloatBuffer(f.length);
        return b.put(f).flip();
    }
}

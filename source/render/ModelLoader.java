package render;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import model.Model;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GLContext;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import texture.TextureData;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
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

    public static Model loadToVAO(float[] pos, float[] coords, float[] normals, int[] indices, float[] tangents)
    {
        int id = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributes(0, 3, pos);
        storeDataInAttributes(1, 2, coords);
        storeDataInAttributes(2, 3, normals);
        storeDataInAttributes(3, 3, tangents);
        unbindVAO();
        return new Model(id, indices.length);
    }

    public static int loadToVAO(float[] pos, float[] coords)
    {
        int id = createVAO();
        storeDataInAttributes(0, 2, pos);
        storeDataInAttributes(1, 2, coords);
        unbindVAO();
        return id;
    }

    public static Model loadToVAO(float[] positions, int dimensions)
    {
        int VAOId = createVAO();
        storeDataInAttributes(0, dimensions, positions);
        unbindVAO();
        return new Model(VAOId, positions.length / dimensions);
    }

    public static int createEmptyVBO(int count)
    {
        int vbo = GL15.glGenBuffers();
        ModelLoader.vbo.add(vbo);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, count * 4L, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        return vbo;
    }

    public static void updateVBO(int vbo, float[] data, FloatBuffer buffer)
    {
        buffer.clear();
        buffer.put(data);
        buffer.flip();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer.capacity() * 4L, GL15.GL_STREAM_DRAW);
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buffer);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    public static void addInstanceAttribs(int vao,
                                          int vbo,
                                          int attrib,
                                          int size,
                                          int instancedLength,
                                          int offset)
    {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL30.glBindVertexArray(vao);
        GL20.glVertexAttribPointer(attrib, size, GL11.GL_FLOAT, false, instancedLength * 4, offset * 4L);
        GL33.glVertexAttribDivisor(attrib, 1);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
    }

    public static int loadCubeMap(String[] textureFiles)
    {
        int textureID = GL11.glGenTextures();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textureID);

        for(int i = 0; i < textureFiles.length; i++)
        {
            TextureData data = decodeTextureFile("resources/skybox/" + textureFiles[i] + ".png");
            GL11.glTexImage2D(
                    GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i,
                    0,
                    GL11.GL_RGBA,
                    data.getWidth(),
                    data.getHeight(),
                    0,
                    GL11.GL_RGBA,
                    GL11.GL_UNSIGNED_BYTE,
                    data.getBuffer());
        }
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        txt.add(textureID);
        return textureID;

    }

    private static TextureData decodeTextureFile(String fileName) {
        int width;
        int height;
        ByteBuffer buffer;
        try {
            FileInputStream in = new FileInputStream(fileName);
            PNGDecoder decoder = new PNGDecoder(in);
            width = decoder.getWidth();
            height = decoder.getHeight();
            buffer = ByteBuffer.allocateDirect(4 * width * height);
            decoder.decode(buffer, width * 4, Format.RGBA);
            buffer.flip();
            in.close();
        } catch (Exception e) {
            System.err.println("Tried to load texture " + fileName + ", didn't work");
            throw new RuntimeException();
        }
        return new TextureData(buffer, width, height);
    }

    public static int loadTexture(String filename)
    {
        Texture texture = null;
        int textureID;
        try {
            texture = TextureLoader.getTexture("PNG", new FileInputStream("resources/textures/"  + filename + ".png"));
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0);
            if(GLContext.getCapabilities().GL_EXT_texture_filter_anisotropic) {
                float amount = Math.min(4f, GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
                GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, amount);
            }
            else {
                System.out.println("not supported :((");
            }
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

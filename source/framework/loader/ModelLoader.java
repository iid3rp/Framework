package framework.loader;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import framework.model.Model;
import framework.resources.Resources;
import framework.textures.TextureData;
import framework.util.Buffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

public final class ModelLoader
{
    private static List<Integer> vaoList = new ArrayList<>();
    private static List<Integer> vboList = new ArrayList<>();
    protected static List<Integer> textureList = new ArrayList<>();
    private static int createVao() {
        int vaoId = glGenVertexArrays();            // initialize an empty VAO
        vaoList.add(vaoId);
        glBindVertexArray(vaoId);                   // select this vao
        return vaoId;
    }

    private static void storeDataInAttributeList(int attributeNumber, int vertexLength, float[] data) {
        int vboId = glGenBuffers();                                 // initialize an empty VBO
        vboList.add(vboId);
        glBindBuffer(GL_ARRAY_BUFFER, vboId);                       // select this VBO into the VAO id specified
        FloatBuffer buffer = Buffer.createFloatBuffer(data);   // make VBO from data
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);      // store data into VBO & Not going to edit this data
        glVertexAttribPointer(attributeNumber, vertexLength, GL_FLOAT, false, 0, 0);    // place VBO into VAO
        glBindBuffer(GL_ARRAY_BUFFER, 0);                   // unbind the VBO
    }

    private static void unbindVao() {
        glBindVertexArray(0);
    }

    private static void bindIndicesBuffer(int[] indices) {
        int vboId = glGenBuffers();
        vboList.add(vboId);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
        IntBuffer buffer = Buffer.createIntBuffer(indices);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
    }

    public static Model loadToVao(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
        int vaoId = createVao();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, positions);     // using VAO attribute 0. Could be any 0 through 15
        storeDataInAttributeList(1, 2, textureCoords);
        storeDataInAttributeList(2, 3, normals);
        unbindVao();
        return new Model(vaoId, indices.length);
    }

    // for the GUI rendering system
    public static Model loadToVao(float[] positions, int dimensions)
    {
        int VaoId = createVao();
        storeDataInAttributeList(0, dimensions, positions);
        unbindVao();
        return new Model(VaoId, positions.length * dimensions);
    }

    public static int loadTexture(String filename)
    {
        int textureId = TextureLoader.loadTexture(filename);
        glGenerateMipmap(GL_TEXTURE_2D);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, 10f);
        textureList.add(textureId);
        return textureId;
    }

    public static int loadCubeMap(String[] textureFiles)
    {
        int textureID = GL11.glGenTextures();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textureID);

        for(int i = 0; i < textureFiles.length; i++)
        {
            TextureData data = decodeTextureFile("skybox/" + textureFiles[i] + ".png");
            GL11.glTexImage2D
            (
                GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i,
                0,
                GL11.GL_RGBA,
                data.getWidth(),
                data.getHeight(),
                0,
                GL11.GL_RGBA,
                GL11.GL_UNSIGNED_BYTE,
                data.getBuffer()
            );
        }
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        textureList.add(textureID);
        return textureID;

    }

    private static TextureData decodeTextureFile(String fileName) {
        int width;
        int height;
        ByteBuffer buffer;
        try {
            InputStream in = Resources.class.getResourceAsStream(fileName);
            PNGDecoder decoder = new PNGDecoder(in);
            width = decoder.getWidth();
            height = decoder.getHeight();
            buffer = ByteBuffer.allocateDirect(4 * width * height);
            decoder.decode(buffer, width * 4, Format.RGBA);
            buffer.flip();
            assert in != null;
            in.close();
        } catch (Exception e) {
            System.err.println("Tried to load texture " + fileName + ", didn't work");
            throw new RuntimeException();
        }
        return new TextureData(buffer, width, height);
    }

    public static void destroy() {
        for (int vaoId : vaoList) {
            glDeleteVertexArrays(vaoId);
        }

        for (int vboId : vboList) {
            glDeleteBuffers(vboId);
        }

        for (int textureId : textureList) {
            glDeleteTextures(textureId);
        }
    }

    public static int createEmptyVbo(int floatCount)
    {
        int vbo = GL15.glGenBuffers();
        ModelLoader.vboList.add(vbo);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, floatCount * 4L, GL_STREAM_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return vbo;
    }

    public static void updateVbo(int vbo, float[] data, FloatBuffer buffer)
    {
        buffer.clear();
        buffer.put(data);
        buffer.flip();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, buffer.capacity() * 4L, GL_STREAM_DRAW);
        glBufferSubData(GL_ARRAY_BUFFER, 0, buffer);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public static void addInstanceAttribs(int vao, int vbo, int attrib, int data, int instanceLength, int offset)
    {
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBindVertexArray(vao);
        glVertexAttribPointer(attrib, data, GL_FLOAT, false, instanceLength * 4, offset * 4L);
        glVertexAttribDivisor(attrib, 1);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public static Model loadToVao(float[] pos, float[] coords, float[] normals, int[] indices, float[] tangents)
    {
        int id = createVao();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, pos);
        storeDataInAttributeList(1, 2, coords);
        storeDataInAttributeList(2, 3, normals);
        storeDataInAttributeList(3, 3, tangents);
        unbindVao();
        return new Model(id, indices.length);
    }

    public static int loadToVao(float[] vertexPositions, float[] textureCoords)
    {
        int vaoId = createVao();
        storeDataInAttributeList(0, 2, vertexPositions);
        storeDataInAttributeList(1, 2, textureCoords);
        unbindVao();
        return vaoId;
    }
}

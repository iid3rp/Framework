package framework.loader;


import framework.model.Model;
import framework.resources.Resources;
import framework.textures.TextureData;
import framework.util.Buffer;
import framework.util.LinkList;
import framework.util.PNGDecoder;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL46.*;

public final class ModelLoader
{
    private static LinkList<Integer> vaoList = new LinkList<>();
    private static LinkList<Integer> vboList = new LinkList<>();
    protected static LinkList<Integer> textureList = new LinkList<>();

    private static int createVao() {
        int vaoId = glGenVertexArrays();
        vaoList.add(vaoId);
        glBindVertexArray(vaoId);
        return vaoId;
    }

    private static void storeDataInAttributeList(int attributeNumber, int vertexLength, float[] data) {
        int vboId = glGenBuffers();
        vboList.add(vboId);
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        FloatBuffer buffer = Buffer.createFloatBuffer(data);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(attributeNumber, vertexLength, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
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

    public static Model loadToVaoInt(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
        int vaoId = createVao();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, positions);
        storeDataInAttributeList(1, 2, textureCoords);
        storeDataInAttributeList(2, 3, normals);
        unbindVao();
        return new Model(vaoId, indices.length);
    }

    // for the GUI rendering system (2D Objects)
    public static Model loadToVaoInt(float[] positions, int dimensions)
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
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, 20f);
        textureList.add(textureId);
        return textureId;
    }

    public static int loadCubeMap(String[] textureFiles)
    {
        int textureID = glGenTextures();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_CUBE_MAP, textureID);

        for(int i = 0; i < textureFiles.length; i++)
        {
            TextureData data = decodeTextureFile("skybox/" + textureFiles[i] + ".png");
            glTexImage2D
            (
                GL_TEXTURE_CUBE_MAP_POSITIVE_X + i,
                0,
                GL_RGBA,
                data.getWidth(),
                data.getHeight(),
                0,
                GL_RGBA,
                GL_UNSIGNED_BYTE,
                data.getBuffer()
            );
        }
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
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
            decoder.decode(buffer, width * 4, PNGDecoder.Format.RGBA);
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
        int vbo = glGenBuffers();
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

    public static Model loadToVaoInt(float[] pos, float[] coords, float[] normals, int[] indices, float[] tangents)
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

    public static int loadToVaoInt(float[] vertexPositions, float[] textureCoords)
    {
        int vaoId = createVao();
        storeDataInAttributeList(0, 2, vertexPositions);
        storeDataInAttributeList(1, 2, textureCoords);
        unbindVao();
        return vaoId;
    }

    public static Model loadToVao(float[] vertexPositions, float[] textureCoords)
    {
        int vaoId = loadToVaoInt(vertexPositions, textureCoords);
        return new Model(vaoId, vertexPositions.length);
    }

    public static Model loadToVao(float[] positions, int[] indices)
    {
        int vaoId = createVao();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, positions);
        unbindVao();
        return new Model(vaoId, indices.length);
    }

    private static void storeDataInAttributeList(int attributeNumber, float[] data) {
        int vboID = glGenBuffers();
        vboList.add(vboID);
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = Buffer.createFloatBuffer(data);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(attributeNumber, 3, GL_FLOAT, false,0,0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

}

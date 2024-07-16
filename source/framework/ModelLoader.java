package framework;

import framework.model.Model;
import framework.utils.Buffer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

public final class ModelLoader {
    private static List<Integer> vaoList = new ArrayList<>();
    private static List<Integer> vboList = new ArrayList<>();
    private static List<Integer> textureList = new ArrayList<>();
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

    public static int loadTexture(String filename) {
        TextureLoader textureLoader = new TextureLoader(filename);
        int textureId = textureLoader.getTextureId();
        glGenerateMipmap(GL_TEXTURE_2D);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -0.4f);
        textureList.add(textureId);
        return textureId;
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
}

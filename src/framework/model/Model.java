package framework.model;

import framework.loader.ModelLoader;

public class Model
{
    public static Model cube;
    public static Model square;

    static {
        float[] cubeVertices = {
                -1, 1, -1,
                -1, -1, -1,
                1, -1, -1,
                1, -1, -1,
                1, 1, -1,
                -1, 1, -1,

                -1, -1, 1,
                -1, -1, -1,
                -1, 1, -1,
                -1, 1, -1,
                -1, 1, 1,
                -1, -1, 1,

                1, -1, -1,
                1, -1, 1,
                1, 1, 1,
                1, 1, 1,
                1, 1, -1,
                1, -1, -1,

                -1, -1, 1,
                -1, 1, 1,
                1, 1, 1,
                1, 1, 1,
                1, -1, 1,
                -1, -1, 1,

                -1, 1, -1,
                1, 1, -1,
                1, 1, 1,
                1, 1, 1,
                -1, 1, 1,
                -1, 1, -1,

                -1, -1, -1,
                -1, -1, 1,
                1, -1, -1,
                1, -1, -1,
                -1, -1, 1,
                1, -1, 1
        };

        float[] squareVertices = {
                -1f, -1f, 0.0f,  // Bottom-left
                1f, -1f, 0.0f,  // Bottom-right
                1f, 1f, 0.0f,  // Top-right
                -1f, 1f, 0.0f   // Top-left
        };

    }


    private int vaoId;
    private int vertexCount;


    public Model(int vaoId, int length)
    {
        this.vaoId = vaoId;
        this.vertexCount = length;
    }

    public int getVaoId()
    {
        return vaoId;
    }

    public void setVaoId(int vaoId)
    {
        this.vaoId = vaoId;
    }

    public int getVertexCount()
    {
        return vertexCount;
    }

    public void setVertexCount(int vertexCount)
    {
        this.vertexCount = vertexCount;
    }
}

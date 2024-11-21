package framework.model;

import framework.loader.ModelLoader;

public class Model
{
    private float[] vertices;
    private float[] textureCoords;
    private float[] normals;
    private float[] tangents;
    private int[] indices;
    private float furthestPoint;
    private final int vaoId;
    private final int vertexCount;

    public static Model cube;
    public static Model square;

    static
    {
        float[] cubeVertices = {
                -1,  1, -1,
                -1, -1, -1,
                1, -1, -1,
                1, -1, -1,
                1,  1, -1,
                -1,  1, -1,

                -1, -1,  1,
                -1, -1, -1,
                -1,  1, -1,
                -1,  1, -1,
                -1,  1,  1,
                -1, -1,  1,

                1, -1, -1,
                1, -1,  1,
                1,  1,  1,
                1,  1,  1,
                1,  1, -1,
                1, -1, -1,

                -1, -1,  1,
                -1,  1,  1,
                1,  1,  1,
                1,  1,  1,
                1, -1,  1,
                -1, -1,  1,

                -1,  1, -1,
                1,  1, -1,
                1,  1,  1,
                1,  1,  1,
                -1,  1,  1,
                -1,  1, -1,

                -1, -1, -1,
                -1, -1,  1,
                1, -1, -1,
                1, -1, -1,
                -1, -1,  1,
                1, -1,  1
        };

        float[] squareVertices = {
                -1f, -1f, 0.0f,  // Bottom-left
                1f, -1f, 0.0f,  // Bottom-right
                1f,  1f, 0.0f,  // Top-right
                -1f,  1f, 0.0f   // Top-left
        };

        cube = ModelLoader.loadToVaoInt(cubeVertices, 3);
        square = ModelLoader.loadToVaoInt(squareVertices, 2);
    }

    public Model(int vaoId, int vertexCount)
    {
        this.vaoId = vaoId;
        this.vertexCount = vertexCount;
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }
}

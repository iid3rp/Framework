package framework.model;

import framework.loader.ModelLoader;

public record Model(int vaoId, int vertexCount)
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

        cube = ModelLoader.loadToVaoInt(cubeVertices, 3);
        square = ModelLoader.loadToVaoInt(squareVertices, 2);
    }

}

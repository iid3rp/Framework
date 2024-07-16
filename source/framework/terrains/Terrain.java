package framework.terrains;

import framework.ModelLoader;
import framework.model.Model;
import framework.textures.TerrainTexture;
import framework.textures.TerrainTexturePack;
import framework.utils.GeomMath;
import org.joml.Vector2f;
import org.joml.Vector3f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Terrain {
    private static final float SIZE = 800;
    private static final float MAX_HEIGHT = 40;
    private static final float MIN_HEIGHT = -100;
    private static final float MAX_PIXEL_COLOR = 256 * 256 * (float)256;

    String heightMap;
    private float x;
    private float z;
    private Model model;
    private TerrainTexturePack terrainTexturePack;
    private TerrainTexture blendMap;
    private float[][] heights;  // height of each vertex on the terrain
    private float size;

    public Terrain()
    {
        this.x = 0;
        this.z = 0;
    }

    public Terrain(int x, int z, TerrainTexturePack terrainTexturePack, TerrainTexture blendMap, String heightMapFilename) {
        this.x = x * SIZE;
        this.z = z * SIZE;
        this.model = generateTerrain(heightMapFilename);
        this.terrainTexturePack = terrainTexturePack;
        this.blendMap = blendMap;
    }

    public Terrain(int x, int z, TerrainTexturePack terrainTexturePack, TerrainTexture blendMap)
    {
        this.size = SIZE;
        this.x = x;
        this.z = z;
        this.terrainTexturePack = terrainTexturePack;
        this.blendMap = blendMap;
        this.model = generateTerrain(0);
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    public Model getModel() {
        return model;
    }

    public TerrainTexturePack getTerrainTexturePack() {
        return terrainTexturePack;
    }

    public TerrainTexture getBlendMap() {
        return blendMap;
    }

    public float getHeightOfTerrainUhmNo(float worldX, float worldZ) {
        // Define the number of sample points and the radius around the entity
        int numSamplePoints = 4;
        float sampleRadius = 0.5f;

        // Initialize the total height to 0
        float totalHeight = 0;

        // Sample the height at several points around the entity
        for (int i = 0; i < numSamplePoints; i++) {
            // Calculate the sample point coordinates
            float sampleX = worldX + (float) Math.cos(2 * Math.PI * i / numSamplePoints) * sampleRadius;
            float sampleZ = worldZ + (float) Math.sin(2 * Math.PI * i / numSamplePoints) * sampleRadius;

            // Calculate the height at the sample point
            float sampleHeight = getHeightOfTerrain(sampleX, sampleZ);

            // Add the sample height to the total height
            totalHeight += sampleHeight;
        }

        // Return the average height
        return totalHeight / numSamplePoints;
    }

    public float getHeightOfTerrain(float x, float z) {
        float terrainX = x + (getSize() / 2);
        float terrainZ = z + (getSize() / 2);
        float gridSquareSize = (size / (heights.length - 1));
        int gridX = (int) Math.floor(terrainX / gridSquareSize);
        int gridZ = (int) Math.floor(terrainZ / gridSquareSize);

        if (gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0) {
            return 0;
        }

        float xCoordinate = (terrainX % gridSquareSize) / gridSquareSize;
        float zCoordinate = (terrainZ % gridSquareSize) / gridSquareSize;
        float triangleHeight;

        if (xCoordinate <= (1 - zCoordinate)) {
            triangleHeight = GeomMath.barryCentric(
                    new Vector3f(0, heights[gridX][gridZ], 0),
                    new Vector3f(1, heights[gridX + 1][gridZ], 0),
                    new Vector3f(0, heights[gridX][gridZ + 1], 1),
                    new Vector2f(xCoordinate, zCoordinate)
            );
        } else {
            triangleHeight = GeomMath.barryCentric(
                    new Vector3f(1, heights[gridX + 1][gridZ], 0),
                    new Vector3f(1, heights[gridX + 1][gridZ + 1], 1),
                    new Vector3f(0, heights[gridX][gridZ + 1], 1),
                    new Vector2f(xCoordinate, zCoordinate)
            );
        }

        return triangleHeight;
    }

    private Model generateTerrain(String heightMapFilename) {
        BufferedImage bufferedImage;

        try {
            bufferedImage = ImageIO.read(new File(heightMapFilename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assert bufferedImage != null;

        final int VERTEX_COUNT = bufferedImage.getHeight();   // don't use too big a height map or the terrain will be high poly

        heights = new float[VERTEX_COUNT][VERTEX_COUNT];

        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count * 2];
        int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT)];
        int vertexPointer = 0;

        for (int i = 0; i < VERTEX_COUNT; i++) {
            for (int j = 0; j < VERTEX_COUNT; j++) {
                vertices[vertexPointer * 3] = j / ((float) VERTEX_COUNT - 1) * SIZE;
                float height = getHeight(j, i, bufferedImage);
                heights[j][i] = height;
                vertices[vertexPointer * 3 + 1] = height;
                vertices[vertexPointer * 3 + 2] = i / ((float) VERTEX_COUNT - 1) * SIZE;

                Vector3f normal = calculateNormal(j, i, bufferedImage);

                normals[vertexPointer * 3] = normal.x;
                normals[vertexPointer * 3 + 1] = normal.y;
                normals[vertexPointer * 3 + 2] = normal.z;
                textureCoords[vertexPointer * 2] = j / ((float) VERTEX_COUNT - 1);
                textureCoords[vertexPointer * 2 + 1] = i / ((float) VERTEX_COUNT - 1);
                vertexPointer++;
            }
        }

        int pointer = 0;

        for (int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
            for (int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
                int topLeft = (gz * VERTEX_COUNT) + gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
                int bottomRight = bottomLeft + 1;

                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }

        return ModelLoader.loadToVao(vertices, textureCoords, normals, indices);
    }

    private Model generateTerrain(int seed)
    {
        HeightGenerator gen = new HeightGenerator();
        int vertexCount = 128;
        heights = new float[vertexCount][vertexCount];

        int count = vertexCount * vertexCount;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count * 2];
        int[] indices = new int[6 * (vertexCount - 1) * (vertexCount - 1)];
        int vertexPointer = 0;
        for(int i = 0; i < vertexCount; i++)
        {
            for(int j = 0; j < vertexCount; j++)
            {
                vertices[vertexPointer * 3] = (float) j / ((float)vertexCount - 1) * size;
                float height = getHeight(j, i, gen);
                heights[j][i] = height;
                vertices[vertexPointer * 3 + 1] = height;
                vertices[vertexPointer * 3 + 2] = (float)i/((float)vertexCount - 1) * size;
                Vector3f normal = calculateNormal(j, i, gen);
                normals[vertexPointer * 3] = normal.x;
                normals[vertexPointer * 3 + 1] = normal.y;
                normals[vertexPointer * 3 + 2] = normal.z;
                textureCoords[vertexPointer*2] = (float)j/((float)vertexCount - 1);
                textureCoords[vertexPointer*2+1] = (float)i/((float)vertexCount - 1);
                vertexPointer++;
            }
        }
        int pointer = 0;
        for(int gz = 0; gz < vertexCount - 1; gz++)
        {
            for(int gx = 0; gx < vertexCount - 1; gx++)
            {
                int topLeft = (gz * vertexCount) + gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz + 1) * vertexCount) + gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }
        return ModelLoader.loadToVao(vertices, textureCoords, normals, indices);
    }

    private float getHeight(int x, int z,  BufferedImage bufferedImage) {
        if (x < 0 || x >= bufferedImage.getHeight() || z < 0 || z >= bufferedImage.getHeight()) {
            return 0;   // out of bounds
        }

        float height = bufferedImage.getRGB(x, z);
        height += MAX_PIXEL_COLOR / 2f;
        height /= MAX_PIXEL_COLOR / 2f;
        height *= MAX_HEIGHT;
        return height;
    }

    private Vector3f calculateNormal(int x, int z, HeightGenerator generator)
    {
        float heightL = getHeight(x - 1,  z, generator);
        float heightR = getHeight(x + 1, z, generator);
        float heightD = getHeight(x, z - 1, generator);
        float heightU = getHeight(x, z + 1, generator);
        Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
        normal.normalize();
        return normal;

    }

    private float getHeight(int x, int z, HeightGenerator generator)
    {
        return generator.generateHeight(x, z);
    }

    private float getHeightFromRGB(int x, int z, BufferedImage bufferedImage) {
        if (x < 0 || x >= bufferedImage.getWidth() || z < 0 || z >= bufferedImage.getHeight()) {
            return 0;   // out of bounds
        }

        int rgb = bufferedImage.getRGB(x, z);
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;

        // Convert RGB to grayscale using the standard formula
        float grayscale = 0.299f * red + 0.587f * green + 0.114f * blue;

        grayscale += MAX_PIXEL_COLOR / 2f;
        grayscale /= MAX_PIXEL_COLOR / 2f;
        grayscale *= MAX_HEIGHT;

        return grayscale;
    }

    private Vector3f calculateNormal(int x, int z, BufferedImage bufferedImage) {
        float heightL = getHeight(x - 1, z, bufferedImage);
        float heightR = getHeight(x + 1, z, bufferedImage);
        float heightD = getHeight(x, z - 1, bufferedImage);
        float heightU = getHeight(x, z + 1, bufferedImage);
        Vector3f normal= new Vector3f(heightL - heightR, 2.0f, heightD - heightU);
        normal.normalize();
        return normal;
    }

    public float getSize()
    {
        return size;
    }

    public void setSize(float size)
    {
        this.size = size;
    }
}

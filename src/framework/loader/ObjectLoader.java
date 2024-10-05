package framework.loader;

import framework.lang.Vec2;
import framework.lang.Vec3;
import framework.model.Model;
import framework.resources.Resources;
import framework.util.LinkList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ObjectLoader
{
    public static Model loadObject(String fileName) {
        InputStream fileReader;

        fileReader = Resources.class.getResourceAsStream("objects/" + fileName);

        assert fileReader != null;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileReader));
        String line;
        LinkList<Vec3> vertices = new LinkList<>();
        LinkList<Vec2> textures = new LinkList<>();
        LinkList<Vec3> normals = new LinkList<>();
        LinkList<Integer> indices = new LinkList<>();
        float[] verticesArray;
        float[] texturesArray = null;
        float[] normalsArray = null;
        int[] indicesArray;
        int vertexLength;

        try {
            while(true) {
                line = bufferedReader.readLine();
                if (line == null) break; // Prevent NullPointerException
                String[] currentLine = line.split(" ");

                if (line.startsWith("v ")) {
                    Vec3 vertex = new Vec3(
                            Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3])
                    );
                    vertices.add(vertex);
                }
                else if (line.startsWith("vt ")) {
                    Vec2 texture = new Vec2(
                            Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2])
                    );
                    textures.add(texture);
                }
                else if (line.startsWith("vn ")) {
                    Vec3 normal = new Vec3(
                            Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3])
                    );
                    normals.add(normal);
                }
                else if (line.startsWith("f ")) {
                    texturesArray = new float[vertices.size() * 2];
                    normalsArray = new float[vertices.size() * 3];
                    break;
                }
            }

            Vec2[] texArray = new Vec2[textures.size()];
            Vec3[] normalArray = new Vec3[normals.size()];

            int i = 0;
            for(Vec2 v : textures.queue())
                texArray[i++] = v;

            i = 0;
            for(Vec3 v : normals.queue())
                normalArray[i++] = v;

            while (line != null) {
                if (!line.startsWith("f ")) {
                    line = bufferedReader.readLine();
                    continue;
                }

                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");

                processVertex(vertex1, indices, texArray, normalArray, texturesArray, normalsArray);
                processVertex(vertex2, indices, texArray, normalArray, texturesArray, normalsArray);
                processVertex(vertex3, indices, texArray, normalArray, texturesArray, normalsArray);

                line = bufferedReader.readLine();
            }

            bufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        verticesArray = new float[vertices.size() * 3];
        indicesArray = new int[indices.size()];

        int vertexPointer = 0;

        for (Vec3 vertex : vertices.queue()) {
            verticesArray[vertexPointer++] = vertex.x;
            verticesArray[vertexPointer++] = vertex.y;
            verticesArray[vertexPointer++] = vertex.z;
        }

        int i = 0;
        for(int n : indices.queue())
            indicesArray[i++] = n;

        // Calculate Tangents
        float[] tangentsArray = new float[verticesArray.length * 3];
        assert texturesArray != null;
        for (i = 0; i < indicesArray.length; i += 3) {
            int index0 = indicesArray[i];
            int index1 = indicesArray[i + 1];
            int index2 = indicesArray[i + 2];

            // Positions
            Vec3 pos0 = new Vec3(
                    verticesArray[index0 * 3],
                    verticesArray[index0 * 3 + 1],
                    verticesArray[index0 * 3 + 2]
            );
            Vec3 pos1 = new Vec3(
                    verticesArray[index1 * 3],
                    verticesArray[index1 * 3 + 1],
                    verticesArray[index1 * 3 + 2]
            );
            Vec3 pos2 = new Vec3(
                    verticesArray[index2 * 3],
                    verticesArray[index2 * 3 + 1],
                    verticesArray[index2 * 3 + 2]
            );

            // Texture coordinates
            Vec2 uv0 = new Vec2(
                    texturesArray[index0 * 2],
                    texturesArray[index0 * 2 + 1]
            );
            Vec2 uv1 = new Vec2(
                    texturesArray[index1 * 2],
                    texturesArray[index1 * 2 + 1]
            );
            Vec2 uv2 = new Vec2(
                    texturesArray[index2 * 2],
                    texturesArray[index2 * 2 + 1]
            );

            // Edges of the triangle: position delta
            Vec3 deltaPos1 = Vec3.sub(pos1, pos0, null);
            Vec3 deltaPos2 = Vec3.sub(pos2, pos0, null);

            // UV delta
            float deltaU1 = uv1.x - uv0.x;
            float deltaV1 = uv1.y - uv0.y;
            float deltaU2 = uv2.x - uv0.x;
            float deltaV2 = uv2.y - uv0.y;

            float r = 1.0f / (deltaU1 * deltaV2 - deltaU2 * deltaV1);
            Vec3 tangent = new Vec3();
            tangent.x = r * (deltaV2 * deltaPos1.x - deltaV1 * deltaPos2.x);
            tangent.y = r * (deltaV2 * deltaPos1.y - deltaV1 * deltaPos2.y);
            tangent.z = r * (deltaV2 * deltaPos1.z - deltaV1 * deltaPos2.z);

            // Accumulate the tangents for each vertex of the triangle
            tangentsArray[index0 * 3]     += tangent.x;
            tangentsArray[index0 * 3 + 1] += tangent.y;
            tangentsArray[index0 * 3 + 2] += tangent.z;

            tangentsArray[index1 * 3]     += tangent.x;
            tangentsArray[index1 * 3 + 1] += tangent.y;
            tangentsArray[index1 * 3 + 2] += tangent.z;

            tangentsArray[index2 * 3]     += tangent.x;
            tangentsArray[index2 * 3 + 1] += tangent.y;
            tangentsArray[index2 * 3 + 2] += tangent.z;
        }

        // Normalize the tangents
        for(i = 0; i < tangentsArray.length; i += 3) {
            Vec3 tangent = new Vec3(
                    tangentsArray[i],
                    tangentsArray[i + 1],
                    tangentsArray[i + 2]
            );
            tangent.normalize();
            tangentsArray[i]     = tangent.x;
            tangentsArray[i + 1] = tangent.y;
            tangentsArray[i + 2] = tangent.z;
        }

        return ModelLoader.loadToVaoInt(verticesArray, texturesArray, normalsArray,  indicesArray, tangentsArray);
    }

    private static void processVertex(String[] vertexData, LinkList<Integer> indices, Vec2[] textures, Vec3[] normals, float[] textureArray, float[] normalsArray)
    {
        int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;

        indices.add(currentVertexPointer);

        Vec2 currentTexture = textures[Integer.parseInt(vertexData[1]) - 1];
        System.out.println(Integer.parseInt(vertexData[1]) - 1);

        textureArray[currentVertexPointer * 2] = currentTexture.x;
        textureArray[currentVertexPointer * 2 + 1] = 1 - currentTexture.y;

        Vec3 currentNormal = normals[Integer.parseInt(vertexData[2]) - 1];

        normalsArray[currentVertexPointer * 3] = currentNormal.x;
        normalsArray[currentVertexPointer * 3 + 1] = currentNormal.y;
        normalsArray[currentVertexPointer * 3 + 2] = currentNormal.z;
    }
}

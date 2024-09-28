package framework.loader;

import framework.model.Model;
import framework.resources.Resources;
import framework.util.LinkList;
import framework.util.Vec3f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ObjLoader
{
    public static Model loadObject(String fileName) {
        InputStream fileReader;

        fileReader = Resources.class.getResourceAsStream("objects/" + fileName);

        assert fileReader != null;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileReader));
        String line;
        LinkList<Vector3f> vertices = new LinkList<>();
        LinkList<Vector2f> textures = new LinkList<>();
        LinkList<Vector3f> normals = new LinkList<>();
        LinkList<Integer> indices = new LinkList<>();
        float[] verticesArray;
        float[] texturesArray = null;
        float[] normalsArray = null;
        int[] indicesArray;

        try {
            while(true) {
                line = bufferedReader.readLine();
                if (line == null) break; // Prevent NullPointerException
                String[] currentLine = line.split(" ");

                if (line.startsWith("v ")) {
                    Vector3f vertex = new Vector3f(
                            Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3])
                    );
                    vertices.add(vertex);
                }
                else if (line.startsWith("vt ")) {
                    Vector2f texture = new Vector2f(
                            Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2])
                    );
                    textures.add(texture);
                }
                else if (line.startsWith("vn ")) {
                    Vector3f normal = new Vector3f(
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

            Vector2f[] texArray = new Vector2f[textures.size()];
            Vector3f[] normalArray = new Vector3f[normals.size()];

            for(int i = 0; i < vertices.size(); i++)
                texArray[i] = textures.removeFirst();

            for(int i = 0; i < vertices.size(); i++)
                normalArray[i] = normals.removeFirst();

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

        for (Vector3f vertex : vertices.queue()) {
            verticesArray[vertexPointer++] = vertex.x;
            verticesArray[vertexPointer++] = vertex.y;
            verticesArray[vertexPointer++] = vertex.z;
        }

        for (int i = 0; i < indices.size(); i++)
            indicesArray[i] = indices.removeFirst();

        // Calculate Tangents
        float[] tangentsArray = new float[vertices.size() * 3];
        assert texturesArray != null;
        for (int i = 0; i < indicesArray.length; i += 3) {
            int index0 = indicesArray[i];
            int index1 = indicesArray[i + 1];
            int index2 = indicesArray[i + 2];

            // Positions
            Vector3f pos0 = new Vector3f(
                    verticesArray[index0 * 3],
                    verticesArray[index0 * 3 + 1],
                    verticesArray[index0 * 3 + 2]
            );
            Vector3f pos1 = new Vector3f(
                    verticesArray[index1 * 3],
                    verticesArray[index1 * 3 + 1],
                    verticesArray[index1 * 3 + 2]
            );
            Vector3f pos2 = new Vector3f(
                    verticesArray[index2 * 3],
                    verticesArray[index2 * 3 + 1],
                    verticesArray[index2 * 3 + 2]
            );

            // Texture coordinates
            Vector2f uv0 = new Vector2f(
                    texturesArray[index0 * 2],
                    texturesArray[index0 * 2 + 1]
            );
            Vector2f uv1 = new Vector2f(
                    texturesArray[index1 * 2],
                    texturesArray[index1 * 2 + 1]
            );
            Vector2f uv2 = new Vector2f(
                    texturesArray[index2 * 2],
                    texturesArray[index2 * 2 + 1]
            );

            // Edges of the triangle: position delta
            Vector3f deltaPos1 = Vec3f.sub(pos1, pos0, null);
            Vector3f deltaPos2 = Vec3f.sub(pos2, pos0, null);

            // UV delta
            float deltaU1 = uv1.x - uv0.x;
            float deltaV1 = uv1.y - uv0.y;
            float deltaU2 = uv2.x - uv0.x;
            float deltaV2 = uv2.y - uv0.y;

            float r = 1.0f / (deltaU1 * deltaV2 - deltaU2 * deltaV1);
            Vector3f tangent = new Vector3f();
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
        for(int i = 0; i < tangentsArray.length; i += 3) {
            Vector3f tangent = new Vector3f(
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

    private static void processVertex(String[] vertexData, LinkList<Integer> indices, Vector2f[] textures, Vector3f[] normals, float[] textureArray, float[] normalsArray)
    {
        int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;

        indices.add(currentVertexPointer);

        Vector2f currentTexture = textures[Integer.parseInt(vertexData[1]) - 1];

        textureArray[currentVertexPointer * 2] = currentTexture.x;
        textureArray[currentVertexPointer * 2 + 1] = 1 - currentTexture.y;

        Vector3f currentNormal = normals[Integer.parseInt(vertexData[2]) - 1];

        normalsArray[currentVertexPointer * 3] = currentNormal.x;
        normalsArray[currentVertexPointer * 3 + 1] = currentNormal.y;
        normalsArray[currentVertexPointer * 3 + 2] = currentNormal.z;
    }
}

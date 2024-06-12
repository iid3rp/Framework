package render;

import model.Model;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ObjectLoader
{
    public static Model loadObject(String fileName)
    {
        FileReader file;
        String line;
        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        float[] verticesArray;
        float[] normalsArray;
        float[] textureArray;
        int[] indicesArray;
        try
        {
            file = new FileReader("resources/objects/" + fileName + ".obj");
            BufferedReader reader = new BufferedReader(file);

            while(true)
            {
                line = reader.readLine();
                String[] currentLine = line.split(" ");
                Object type;
                if(line.startsWith("v "))
                {
                    Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]), Float.parseFloat((currentLine[3])));
                    vertices.add(vertex);
                }
                else if(line.startsWith("vt "))
                {
                    Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]));
                    textures.add(texture);
                }
                else if(line.startsWith("vn "))
                {
                    Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]), Float.parseFloat((currentLine[3])));
                    normals.add(normal);
                }
                else if(line.startsWith("f "))
                {
                    textureArray = new float[vertices.size() * 2];
                    normalsArray = new float[vertices.size() * 3];
                    break;
                }
            }

            while(line != null)
            {
                if(!line.startsWith("f "))
                {
                    line = reader.readLine();
                    continue;
                }
                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");

                processVertex(vertex1, indices, textures, normals, textureArray, normalsArray);
                processVertex(vertex2, indices, textures, normals, textureArray, normalsArray);
                processVertex(vertex3, indices, textures, normals, textureArray, normalsArray);
                line = reader.readLine();
            }
            reader.close();
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        verticesArray = new float[vertices.size() * 3];
        indicesArray = new int[indices.size()];
        int vertexPointer = 0;

        for(Vector3f vertex : vertices)
        {
            verticesArray[vertexPointer++] = vertex.x;
            verticesArray[vertexPointer++] = vertex.y;
            verticesArray[vertexPointer++] = vertex.z;
        }

        for(int i = 0; i < indices.size(); i++)
        {
            indicesArray[i] = indices.get(i);
        }
        return ModelLoader.loadToVAO(verticesArray, textureArray, normalsArray, indicesArray);
    }

    private static void processVertex(String[] data, List<Integer> indices,
                                      List<Vector2f> textures, List<Vector3f> normals,
                                      float[] texturesArray, float[] normalsArray)
    {
        int currentVertexPointer = Integer.parseInt(data[0]) - 1;
        indices.add(currentVertexPointer);
        Vector2f currentTexturePointer = textures.get(Integer.parseInt(data[1]) - 1);
        texturesArray[currentVertexPointer * 2] = currentTexturePointer.x;
        texturesArray[currentVertexPointer * 2 + 1] = 1 - currentTexturePointer.y;
        Vector3f currentNormalPointer = normals.get(Integer.parseInt(data[2]) - 1);
        normalsArray[currentVertexPointer * 3] = currentNormalPointer.x;
        normalsArray[currentVertexPointer * 3 + 1] = currentNormalPointer.y;
        normalsArray[currentVertexPointer * 3 + 2] = currentNormalPointer.z;
    }
}

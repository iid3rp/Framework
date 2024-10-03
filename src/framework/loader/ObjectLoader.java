package framework.loader;

import framework.model.Model;
import framework.normal.VertexNormal;
import framework.resources.Resources;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@link ObjectLoader} class is responsible for loading and processing object files in the project.
 * It contains methods to handle the loading of 3D object models from .obj files,
 * including parsing vertices, textures, normals, and indices.
 * <p>
 * <h2>Loading Methods</h2>
 * This class provides two ways to load an object.
 * One is the regular loading of the object with no calculation of the normals and tangents.
 * While the other one does, with a more improvement of calculation that enables to load a model
 * with a much more accuracy with the lighting.
 * Both methods read an object file, extract vertex, texture, and normal data,
 * and calculate tangents for lighting effects.
 * It processes the vertices to handle duplicate vertices, calculates tangents for each triangle,
 * and removes unused vertices before converting the data into arrays for rendering.
 * <p>
 * Additionally, the {@link ObjectLoader} class includes the {@link #loadObjModel(String file)} method,
 * which loads an object model by processing vertices, textures, normals, and indices from an object file.
 * It creates arrays for vertices, textures, normals, and indices to be used in rendering the 3D object.
 * <p>
 * Key components in this class include the {@link VertexNormal} class,
 * which represents vertices in 3D space with texture and normal information,
 * and methods for processing vertices, textures, and normals.
 * The {@link #calculateTangents(VertexNormal, VertexNormal, VertexNormal, List)}
 * method is used to calculate tangents for lighting calculations based on texture coordinates.
 *
 * @see VertexNormal
 */
public class ObjectLoader
{
    private ObjectLoader() {}

    public static Model loadObjModel(String filename) {
        InputStream fileReader;

        fileReader = Resources.class.getResourceAsStream("objects/" + filename);

        assert fileReader != null;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileReader));
        String line;
        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        float[] verticesArray;
        float[] texturesArray;
        float[] normalsArray;
        int[] indicesArray;

        try {
            while(true) {
                line = bufferedReader.readLine();
                String[] currentLine = line.split(" ");

                if (line.startsWith("v ")) {
                    Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    vertices.add(vertex);
                } else if (line.startsWith("vt ")) {
                    Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
                    textures.add(texture);
                } else if (line.startsWith("vn ")) {
                    Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    normals.add(normal);
                } else if (line.startsWith("f ")) {
                    texturesArray = new float[vertices.size() * 2];
                    normalsArray = new float[vertices.size() * 3];
                    break;
                }
            }

            while (line != null) {
                if (!line.startsWith("f ")) {
                    line = bufferedReader.readLine();
                    continue;
                }

                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");

                processVertex(vertex1, indices, textures, normals, texturesArray, normalsArray);
                processVertex(vertex2, indices, textures, normals, texturesArray, normalsArray);
                processVertex(vertex3, indices, textures, normals, texturesArray, normalsArray);

                line = bufferedReader.readLine();
            }

            bufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        verticesArray = new float[vertices.size() * 3];
        indicesArray = new int[indices.size()];

        int vertexPointer = 0;

        for (Vector3f vertex : vertices) {
            verticesArray[vertexPointer++] = vertex.x;
            verticesArray[vertexPointer++] = vertex.y;
            verticesArray[vertexPointer++] = vertex.z;
        }

        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = indices.get(i);
        }

        return ModelLoader.loadToVaoInt(verticesArray, texturesArray, normalsArray, indicesArray);
    }

    private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals, float[] textureArray, float[] normalsArray) {
        int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;

        indices.add(currentVertexPointer);

        Vector2f currentTexture = textures.get(Integer.parseInt(vertexData[1]) - 1);

        textureArray[currentVertexPointer * 2] = currentTexture.x;
        textureArray[currentVertexPointer * 2 + 1] = 1 - currentTexture.y;

        Vector3f currentNormal = normals.get(Integer.parseInt(vertexData[2]) - 1);

        normalsArray[currentVertexPointer * 3] = currentNormal.x;
        normalsArray[currentVertexPointer * 3 + 1] = currentNormal.y;
        normalsArray[currentVertexPointer * 3 + 2] = currentNormal.z;
    }

    public static Model loadObject(String objFileName) {
        BufferedReader reader;
        InputStream objFile = Resources.class.getResourceAsStream("objects/" + objFileName);
        assert objFile != null;
        reader = new BufferedReader(new InputStreamReader(objFile));
        String line;
        List<VertexNormal> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        try {
            while (true) {
                line = reader.readLine();
                if (line == null) break;
                if (line.startsWith("v ")) {
                    String[] currentLine = line.split(" ");
                    Vector3f vertex = new Vector3f(
                            Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3])
                    );
                    VertexNormal newVertex = new VertexNormal(vertices.size(), vertex);
                    vertices.add(newVertex);

                } else if (line.startsWith("vt ")) {
                    String[] currentLine = line.split(" ");
                    Vector2f texture = new Vector2f(
                            Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2])
                    );
                    textures.add(texture);
                } else if (line.startsWith("vn ")) {
                    String[] currentLine = line.split(" ");
                    Vector3f normal = new Vector3f(
                            Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3])
                    );
                    normals.add(normal);
                } else if (line.startsWith("f ")) {
                    break;
                }
            }
            while (line != null && line.startsWith("f ")) {
                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");
                VertexNormal v0 = processVertex(vertex1, vertices, indices);
                VertexNormal v1 = processVertex(vertex2, vertices, indices);
                VertexNormal v2 = processVertex(vertex3, vertices, indices);
                calculateTangents(v0, v1, v2, textures);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error reading the file");
        }
        removeUnusedVertices(vertices);
        float[] verticesArray = new float[vertices.size() * 3];
        float[] texturesArray = new float[vertices.size() * 2];
        float[] normalsArray = new float[vertices.size() * 3];
        float[] tangentsArray = new float[vertices.size() * 3];
        convertDataToArrays(vertices, textures, normals, verticesArray, texturesArray, normalsArray, tangentsArray);
        int[] indicesArray = convertIndicesListToArray(indices);

        return ModelLoader.loadToVaoInt(verticesArray, texturesArray, normalsArray, indicesArray, tangentsArray);
    }

    private static void calculateTangents(VertexNormal v0, VertexNormal v1, VertexNormal v2, List<Vector2f> textures) {
        Vector3f deltaPos1 = new Vector3f(v1.getPosition()).sub(v0.getPosition());
        Vector3f deltaPos2 = new Vector3f(v2.getPosition()).sub(v0.getPosition());
        Vector2f uv0 = textures.get(v0.getTextureIndex());
        Vector2f uv1 = textures.get(v1.getTextureIndex());
        Vector2f uv2 = textures.get(v2.getTextureIndex());
        Vector2f deltaUv1 = new Vector2f(uv1).sub(uv0);
        Vector2f deltaUv2 = new Vector2f(uv2).sub(uv0);

        float r = 1.0f / (deltaUv1.x * deltaUv2.y - deltaUv1.y * deltaUv2.x);
        deltaPos1.mul(deltaUv2.y);
        deltaPos2.mul(deltaUv1.y);
        Vector3f tangent = new Vector3f(deltaPos1).sub(deltaPos2).mul(r);
        v0.addTangent(tangent);
        v1.addTangent(tangent);
        v2.addTangent(tangent);
    }

    private static VertexNormal processVertex(String[] vertex, List<VertexNormal> vertices, List<Integer> indices) {
        int index = Integer.parseInt(vertex[0]) - 1;
        VertexNormal currentVertex = vertices.get(index);
        int textureIndex = Integer.parseInt(vertex[1]) - 1;
        int normalIndex = Integer.parseInt(vertex[2]) - 1;
        if (!currentVertex.isSet()) {
            currentVertex.setTextureIndex(textureIndex);
            currentVertex.setNormalIndex(normalIndex);
            indices.add(index);
            return currentVertex;
        } else {
            return dealWithAlreadyProcessedVertex(currentVertex, textureIndex, normalIndex, indices, vertices);
        }
    }

    private static int[] convertIndicesListToArray(List<Integer> indices) {
        int[] indicesArray = new int[indices.size()];
        for (int i = 0; i < indicesArray.length; i++) {
            indicesArray[i] = indices.get(i);
        }
        return indicesArray;
    }

    private static void convertDataToArrays(List<VertexNormal> vertices, List<Vector2f> textures, List<Vector3f> normals,
                                            float[] verticesArray, float[] texturesArray, float[] normalsArray, float[] tangentsArray) {
        for (int i = 0; i < vertices.size(); i++) {
            VertexNormal currentVertex = vertices.get(i);
            Vector3f position = currentVertex.getPosition();
            Vector2f textureCoords = textures.get(currentVertex.getTextureIndex());
            Vector3f normalVector = normals.get(currentVertex.getNormalIndex());
            Vector3f tangent = currentVertex.getAverageTangent();
            verticesArray[i * 3] = position.x;
            verticesArray[i * 3 + 1] = position.y;
            verticesArray[i * 3 + 2] = position.z;
            texturesArray[i * 2] = textureCoords.x;
            texturesArray[i * 2 + 1] = 1 - textureCoords.y;
            normalsArray[i * 3] = normalVector.x;
            normalsArray[i * 3 + 1] = normalVector.y;
            normalsArray[i * 3 + 2] = normalVector.z;
            tangentsArray[i * 3] = tangent.x;
            tangentsArray[i * 3 + 1] = tangent.y;
            tangentsArray[i * 3 + 2] = tangent.z;
        }
    }

    private static VertexNormal dealWithAlreadyProcessedVertex(VertexNormal previousVertex, int newTextureIndex, int newNormalIndex,
                                                               List<Integer> indices, List<VertexNormal> vertices) {
        if (previousVertex.hasSameTextureAndNormal(newTextureIndex, newNormalIndex)) {
            indices.add(previousVertex.getIndex());
            return previousVertex;
        } else {
            VertexNormal anotherVertex = previousVertex.getDuplicateVertex();
            if (anotherVertex != null) {
                return dealWithAlreadyProcessedVertex(anotherVertex, newTextureIndex, newNormalIndex, indices, vertices);
            } else {
                VertexNormal duplicateVertex = previousVertex.duplicate(vertices.size());
                duplicateVertex.setTextureIndex(newTextureIndex);
                duplicateVertex.setNormalIndex(newNormalIndex);
                previousVertex.setDuplicateVertex(duplicateVertex);
                vertices.add(duplicateVertex);
                indices.add(duplicateVertex.getIndex());
                return duplicateVertex;
            }
        }
    }

    private static void removeUnusedVertices(List<VertexNormal> vertices) {
        for (VertexNormal vertex : vertices) {
            vertex.averageTangents();
            if (!vertex.isSet()) {
                vertex.setTextureIndex(0);
                vertex.setNormalIndex(0);
            }
        }
    }
}

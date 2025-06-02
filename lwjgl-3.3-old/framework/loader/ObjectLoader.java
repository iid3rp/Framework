package framework.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import framework.lang.Normal;
import framework.lang.Vec2;
import framework.lang.Vec3;
import framework.model.Model;
import framework.resources.Resources;

public class ObjectLoader {

    private static final String RES_LOC = "objects/";

    public static Model loadObject(String objFileName) {
        BufferedReader isr;
        InputStream objFile = Resources.class.getResourceAsStream("objects/" + objFileName);
        assert objFile != null;
        isr = new BufferedReader(new InputStreamReader(objFile));
        BufferedReader reader = new BufferedReader(isr);
        String line;
        List<Normal> vertices = new ArrayList<>();
        List<Vec2> textures = new ArrayList<>();
        List<Vec3> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        try {
            while (true) {
                line = reader.readLine();
                if (line.startsWith("v ")) {
                    String[] currentLine = line.split(" ");
                    Vec3 vertex = new Vec3(Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3]));
                    Normal newVertex = new Normal(vertices.size(), vertex);
                    vertices.add(newVertex);

                } else if (line.startsWith("vt ")) {
                    String[] currentLine = line.split(" ");
                    Vec2 texture = new Vec2(Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]));
                    textures.add(texture);
                } else if (line.startsWith("vn ")) {
                    String[] currentLine = line.split(" ");
                    Vec3 normal = new Vec3(Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3]));
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
                Normal v0 = processVertex(vertex1, vertices, indices);
                Normal v1 = processVertex(vertex2, vertices, indices);
                Normal v2 = processVertex(vertex3, vertices, indices);
                calculateTangents(v0, v1, v2, textures);//NEW
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
        float furthest = convertDataToArrays(vertices, textures, normals, verticesArray,
                texturesArray, normalsArray, tangentsArray);
        int[] indicesArray = convertIndicesListToArray(indices);

        return ModelLoader.loadToVaoInt(verticesArray, texturesArray, normalsArray, indicesArray, tangentsArray);
    }

    //NEW 
    private static void calculateTangents(Normal v0, Normal v1, Normal v2,
                                          List<Vec2> textures) {
        Vec3 deltaPos1 = Vec3.sub(v1.getPosition(), v0.getPosition(), null);
        Vec3 deltaPos2 = Vec3.sub(v2.getPosition(), v0.getPosition(), null);
        Vec2 uv0 = textures.get(v0.getTextureIndex());
        Vec2 uv1 = textures.get(v1.getTextureIndex());
        Vec2 uv2 = textures.get(v2.getTextureIndex());
        Vec2 deltaUv1 = Vec2.sub(uv1, uv0, null);
        Vec2 deltaUv2 = Vec2.sub(uv2, uv0, null);

        float r = 1.0f / (deltaUv1.x * deltaUv2.y - deltaUv1.y * deltaUv2.x);
        deltaPos1.scale(deltaUv2.y);
        deltaPos2.scale(deltaUv1.y);
        Vec3 tangent = Vec3.sub(deltaPos1, deltaPos2, null);
        tangent.scale(r);
        v0.addTangent(tangent);
        v1.addTangent(tangent);
        v2.addTangent(tangent);
    }

    private static Normal processVertex(String[] vertex, List<Normal> vertices,
                                              List<Integer> indices) {
        int index = Integer.parseInt(vertex[0]) - 1;
        Normal currentVertex = vertices.get(index);
        int textureIndex = Integer.parseInt(vertex[1]) - 1;
        int normalIndex = Integer.parseInt(vertex[2]) - 1;
        if (!currentVertex.isSet()) {
            currentVertex.setTextureIndex(textureIndex);
            currentVertex.setNormalIndex(normalIndex);
            indices.add(index);
            return currentVertex;
        } else {
            return dealWithAlreadyProcessedVertex(currentVertex, textureIndex, normalIndex, indices,
                    vertices);
        }
    }

    private static int[] convertIndicesListToArray(List<Integer> indices) {
        int[] indicesArray = new int[indices.size()];
        for (int i = 0; i < indicesArray.length; i++) {
            indicesArray[i] = indices.get(i);
        }
        return indicesArray;
    }

    private static float convertDataToArrays(List<Normal> vertices, List<Vec2> textures,
                                             List<Vec3> normals, float[] verticesArray, float[] texturesArray,
                                             float[] normalsArray, float[] tangentsArray) {
        float furthestPoint = 0;
        for (int i = 0; i < vertices.size(); i++) {
            Normal currentVertex = vertices.get(i);
            if (currentVertex.getLength() > furthestPoint) {
                furthestPoint = currentVertex.getLength();
            }
            Vec3 position = currentVertex.getPosition();
            Vec2 textureCoordinates = textures.get(currentVertex.getTextureIndex());
            Vec3 normalVector = normals.get(currentVertex.getNormalIndex());
            Vec3 tangent = currentVertex.getAverageTangent();
            verticesArray[i * 3] = position.x;
            verticesArray[i * 3 + 1] = position.y;
            verticesArray[i * 3 + 2] = position.z;
            texturesArray[i * 2] = textureCoordinates.x;
            texturesArray[i * 2 + 1] = 1 - textureCoordinates.y;
            normalsArray[i * 3] = normalVector.x;
            normalsArray[i * 3 + 1] = normalVector.y;
            normalsArray[i * 3 + 2] = normalVector.z;
            tangentsArray[i * 3] = tangent.x;
            tangentsArray[i * 3 + 1] = tangent.y;
            tangentsArray[i * 3 + 2] = tangent.z;

        }
        return furthestPoint;
    }

    private static Normal dealWithAlreadyProcessedVertex(Normal previousVertex, int newTextureIndex,
                                                               int newNormalIndex, List<Integer> indices, List<Normal> vertices) {
        if (previousVertex.hasSameTextureAndNormal(newTextureIndex, newNormalIndex)) {
            indices.add(previousVertex.getIndex());
            return previousVertex;
        } else {
            Normal anotherVertex = previousVertex.getDuplicateVertex();
            if (anotherVertex != null) {
                return dealWithAlreadyProcessedVertex(anotherVertex, newTextureIndex,
                        newNormalIndex, indices, vertices);
            } else {
                Normal duplicateVertex = previousVertex.duplicate(vertices.size());//NEW
                duplicateVertex.setTextureIndex(newTextureIndex);
                duplicateVertex.setNormalIndex(newNormalIndex);
                previousVertex.setDuplicateVertex(duplicateVertex);
                vertices.add(duplicateVertex);
                indices.add(duplicateVertex.getIndex());
                return duplicateVertex;
            }
        }
    }

    private static void removeUnusedVertices(List<Normal> vertices) {
        for (Normal vertex : vertices) {
            vertex.averageTangents();
            if (!vertex.isSet()) {
                vertex.setTextureIndex(0);
                vertex.setNormalIndex(0);
            }
        }
    }

}
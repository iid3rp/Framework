package framework.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import framework.io.Mesh;
import framework.io.Resources;

public class ObjectLoader {

    public static void loadAllObjects() {
        File meshDir = new File(System.getProperty("user.home") + File.separator + "framework" + File.separator + "objects");
        System.out.println(meshDir.getAbsolutePath());
        if (!meshDir.exists()) {
            System.out.println("Mesh directory does not exist.");
            return;
        }

        File[] files = meshDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".obj"));
        if (files == null || files.length == 0) {
            System.out.println("No OBJ files found in: " + Mesh.MESH_DIRECTORY);
            return;
        }

        for (File file : files) {
            String fileName = file.getName();
            String meshName = fileName.substring(0, fileName.lastIndexOf('.'));

            System.out.println(file.getAbsolutePath() + " " + fileName + " " + meshName);
            Mesh mesh = parseObjFromFile2(file.getAbsolutePath(), meshName);
            mesh.exportObject();
            System.out.println("Processed and exported: " + meshName);
        }
    }

    public static Mesh parseObjFromFile2(String path, String meshName)
    {
        List<Float> vertices = new ArrayList<>();
        List<Float> textures = new ArrayList<>();
        List<Float> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        // Temporary lists to store the raw data from the OBJ file
        float[] verticesArr, normalsArr, texturesArr;
        int[] indicesArr;

        int lineNumber = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(path)))
        {
            String line;
            contentLoop:
            for(;;)
            {
                line = reader.readLine();
                String[] content = line.split("\\s+");


                switch(content[0]) {
                    case "v":
                        vertices.add(Float.parseFloat(content[1]));
                        vertices.add(Float.parseFloat(content[2]));
                        vertices.add(Float.parseFloat(content[3]));
                        break;
                    case "vt":
                        textures.add(Float.parseFloat(content[1]));
                        textures.add(Float.parseFloat(content[2]));
                        break;
                    case "vn":
                        normals.add(Float.parseFloat(content[1]));
                        normals.add(Float.parseFloat(content[2]));
                        normals.add(Float.parseFloat(content[3]));
                        break;
                    case "f":
                        texturesArr = new float[vertices.size() * 2];
                        normalsArr = new float[vertices.size() * 3];
                        break contentLoop;
                }
            }

            while(line != null && !line.startsWith("f "))
                line = reader.readLine();

            while(line != null)
            {
                String[] content = line.split("\\s+");
                String[] vert1 = content[1].split("/");
                String[] vert2 = content[2].split("/");
                String[] vert3 = content[3].split("/");

                processVertex(vert1, indices, textures, normals, texturesArr, normalsArr);
                processVertex(vert2, indices, textures, normals, texturesArr, normalsArr);
                processVertex(vert3, indices, textures, normals, texturesArr, normalsArr);

                line = reader.readLine();
            }
        }
        catch(IOException e)
        {
            throw new RuntimeException(e.getMessage() + "\nCannot parse file!");
        }

        verticesArr = new float[vertices.size() * 3];
        indicesArr = new int[indices.size()];

        for(int i = 0; i < vertices.size(); i++)
            verticesArr[i] = vertices.get(i);
        for(int i = 0; i < indices.size(); i++)
            indicesArr[i] = indices.get(i);

        Mesh mesh = new Mesh(meshName);
        mesh.indices = indicesArr;
        mesh.positions = verticesArr;
        mesh.normals = normalsArr;
        mesh.textures = texturesArr;
        return mesh;
    }

    public static void processVertex(String[] vertexData, List<Integer> indices, List<Float> textures, List<Float> normals, float[] texturesArr, float[] normalsArr)
    {
        int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
        indices.add(currentVertexPointer);

        // textures
        float texX = textures.get((Integer.parseInt(vertexData[1]) * 2) - 2);
        float texY = textures.get((Integer.parseInt(vertexData[1]) * 2) - 1);
        texturesArr[currentVertexPointer * 2] = texX;
        texturesArr[currentVertexPointer * 2 + 1] = texY;

        float normX = normals.get((Integer.parseInt(vertexData[2]) * 3) - 3);
        float normY = normals.get((Integer.parseInt(vertexData[2]) * 3) - 2);
        float normZ = normals.get((Integer.parseInt(vertexData[2]) * 3) - 1);
        normalsArr[currentVertexPointer * 3] = normX;
        normalsArr[currentVertexPointer * 3 + 1] = normY;
        normalsArr[currentVertexPointer * 3 + 2] = normZ;



    }

    /**
     * Parses an OBJ file from the filesystem and returns a Mesh object.
     */
    public static Mesh parseObjFromFile(String filePath, String meshName) throws IOException {
        List<Float> positions = new ArrayList<>();
        List<Float> textures = new ArrayList<>();
        List<Float> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        // Temporary lists to store the raw data from the OBJ file
        List<Float> rawPositions = new ArrayList<>();
        List<Float> rawTextures = new ArrayList<>();
        List<Float> rawNormals = new ArrayList<>();

        // Fixed regex patterns - more flexible to handle different number formats
        Pattern positionPattern = Pattern.compile("^v\\s+([+-]?\\d*\\.?\\d+(?:[eE][+-]?\\d+)?)\\s+([+-]?\\d*\\.?\\d+(?:[eE][+-]?\\d+)?)\\s+([+-]?\\d*\\.?\\d+(?:[eE][+-]?\\d+)?)");
        Pattern texturePattern = Pattern.compile("^vt\\s+([+-]?\\d*\\.?\\d+(?:[eE][+-]?\\d+)?)\\s+([+-]?\\d*\\.?\\d+(?:[eE][+-]?\\d+)?)");
        Pattern normalPattern = Pattern.compile("^vn\\s+([+-]?\\d*\\.?\\d+(?:[eE][+-]?\\d+)?)\\s+([+-]?\\d*\\.?\\d+(?:[eE][+-]?\\d+)?)\\s+([+-]?\\d*\\.?\\d+(?:[eE][+-]?\\d+)?)");

        int lineNumber = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                // Skip empty lines and comments
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                // Parse vertex positions
                if (line.startsWith("v ")) {
                    Matcher posMatcher = positionPattern.matcher(line);
                    if (posMatcher.find()) {
                        rawPositions.add(Float.parseFloat(posMatcher.group(1)));
                        rawPositions.add(Float.parseFloat(posMatcher.group(2)));
                        rawPositions.add(Float.parseFloat(posMatcher.group(3)));
                    } else {
                        System.err.println("Warning: Could not parse vertex position at line " + lineNumber + ": " + line);
                    }
                    continue;
                }

                // Parse texture coordinates
                if (line.startsWith("vt ")) {
                    Matcher texMatcher = texturePattern.matcher(line);
                    if (texMatcher.find()) {
                        rawTextures.add(Float.parseFloat(texMatcher.group(1)));
                        rawTextures.add(Float.parseFloat(texMatcher.group(2)));
                    } else {
                        System.err.println("Warning: Could not parse texture coordinate at line " + lineNumber + ": " + line);
                    }
                    continue;
                }

                // Parse vertex normals
                if (line.startsWith("vn ")) {
                    Matcher normMatcher = normalPattern.matcher(line);
                    if (normMatcher.find()) {
                        rawNormals.add(Float.parseFloat(normMatcher.group(1)));
                        rawNormals.add(Float.parseFloat(normMatcher.group(2)));
                        rawNormals.add(Float.parseFloat(normMatcher.group(3)));
                    } else {
                        System.err.println("Warning: Could not parse vertex normal at line " + lineNumber + ": " + line);
                    }
                    continue;
                }

                // Parse faces - simplified approach
                if (line.startsWith("f ")) {
                    String[] parts = line.split("\\s+");
                    if (parts.length < 4) {
                        System.err.println("Warning: Face with less than 3 vertices at line " + lineNumber + ": " + line);
                        continue;
                    }

                    // Handle triangular faces (convert quads to triangles if needed)
                    List<String> faceVertices = new ArrayList<>();
                    faceVertices.addAll(Arrays.asList(parts).subList(1, parts.length));

                    // Triangulate if more than 3 vertices (simple fan triangulation)
                    for (int i = 1; i < faceVertices.size() - 1; i++) {
                        // Process triangle: vertex 0, vertex i, vertex i+1
                        String[] vertices = {faceVertices.get(0), faceVertices.get(i), faceVertices.get(i + 1)};

                        for (String vertex : vertices) {
                            try {
                                String[] indicesStr = vertex.split("/");

                                // Parse indices (OBJ uses 1-based indexing)
                                int posIndex = Integer.parseInt(indicesStr[0]) - 1;
                                int texIndex = -1;
                                int normIndex = -1;

                                if (indicesStr.length > 1 && !indicesStr[1].isEmpty()) {
                                    texIndex = Integer.parseInt(indicesStr[1]) - 1;
                                }
                                if (indicesStr.length > 2 && !indicesStr[2].isEmpty()) {
                                    normIndex = Integer.parseInt(indicesStr[2]) - 1;
                                }

                                // Add vertex data to the final lists with bounds checking
                                if (posIndex >= 0 && posIndex * 3 + 2 < rawPositions.size()) {
                                    positions.add(rawPositions.get(posIndex * 3));
                                    positions.add(rawPositions.get(posIndex * 3 + 1));
                                    positions.add(rawPositions.get(posIndex * 3 + 2));
                                } else {
                                    System.err.println("Warning: Invalid position index " + (posIndex + 1) + " at line " + lineNumber);
                                    // Add default position to maintain consistency
                                    positions.add(0.0f);
                                    positions.add(0.0f);
                                    positions.add(0.0f);
                                }

                                if (texIndex >= 0 && texIndex * 2 + 1 < rawTextures.size()) {
                                    textures.add(rawTextures.get(texIndex * 2));
                                    textures.add(rawTextures.get(texIndex * 2 + 1));
                                } else if (rawTextures.size() > 0) {
                                    // Add default texture coordinates if available
                                    textures.add(0.0f);
                                    textures.add(0.0f);
                                }

                                if (normIndex >= 0 && normIndex * 3 + 2 < rawNormals.size()) {
                                    normals.add(rawNormals.get(normIndex * 3));
                                    normals.add(rawNormals.get(normIndex * 3 + 1));
                                    normals.add(rawNormals.get(normIndex * 3 + 2));
                                } else if (rawNormals.size() > 0) {
                                    // Add default normal if available
                                    normals.add(0.0f);
                                    normals.add(0.0f);
                                    normals.add(1.0f);
                                }

                                // Add index for the vertex
                                indices.add(indices.size());

                            } catch (NumberFormatException e) {
                                System.err.println("Warning: Could not parse face indices at line " + lineNumber + ": " + vertex);
                            }
                        }
                    }
                }
            }
        }

        // Debug output
        System.out.println("Parsed OBJ file: " + filePath);
        System.out.println("Raw data - Positions: " + rawPositions.size()/3 + ", Textures: " + rawTextures.size()/2 + ", Normals: " + rawNormals.size()/3);
        System.out.println("Final data - Positions: " + positions.size() + ", Textures: " + textures.size() + ", Normals: " + normals.size() + ", Indices: " + indices.size());

        // Create mesh using Mesh constructor and setters
        Mesh mesh = new Mesh(meshName);

        // Convert lists to primitive arrays using Mesh setters
        float[] positionsArray = new float[positions.size()];
        for (int i = 0; i < positions.size(); i++) {
            positionsArray[i] = positions.get(i);
        }
        mesh.setPositions(positionsArray);

        float[] texturesArray = new float[textures.size()];
        for (int i = 0; i < textures.size(); i++) {
            texturesArray[i] = textures.get(i);
        }
        mesh.setTextures(texturesArray);

        float[] normalsArray = new float[normals.size()];
        for (int i = 0; i < normals.size(); i++) {
            normalsArray[i] = normals.get(i);
        }
        mesh.setNormals(normalsArray);

        int[] indicesArray = new int[indices.size()];
        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = indices.get(i);
        }
        mesh.setIndices(indicesArray);

        System.out.println("Final arrays - Positions: " + positionsArray.length + ", Textures: " + texturesArray.length + ", Normals: " + normalsArray.length + ", Indices: " + indicesArray.length);

        return mesh;
    }

    /**
     * Parses a Wavefront OBJ file and returns a Mesh object containing the parsed data.
     * This utility method uses a single-pass approach to read the file and build the mesh data.
     * It avoids using any external vector libraries and relies on primitive arrays.
     *
     * @param filePath The path to the OBJ file.
     * @return A Mesh object containing the vertex positions, texture coordinates, normals, and indices.
     * @throws IOException If an error occurs while reading the file.
     */
    public static Mesh parseObj(String filePath, String name) throws IOException {
        List<Float> positions = new ArrayList<>();
        List<Float> textures = new ArrayList<>();
        List<Float> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        // Temporary lists to store the raw data from the OBJ file
        List<Float> rawPositions = new ArrayList<>();
        List<Float> rawTextures = new ArrayList<>();
        List<Float> rawNormals = new ArrayList<>();

        // Regex patterns for parsing lines
        Pattern positionPattern = Pattern.compile("^v\\s+(-?\\d+\\.?\\d*)\\s+(-?\\d+\\.?\\d*)\\s+(-?\\d+\\.?\\d*)$");
        Pattern texturePattern = Pattern.compile("^vt\\s+(-?\\d+\\.?\\d*)\\s+(-?\\d+\\.?\\d*)$");
        Pattern normalPattern = Pattern.compile("^vn\\s+(-?\\d+\\.?\\d*)\\s+(-?\\d+\\.?\\d*)\\s+(-?\\d+\\.?\\d*)$");
        Pattern facePattern = Pattern.compile("^f\\s+((?:\\d+/\\d*/\\d*|\\d+//\\d*)\\s+){3,}");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(Resources.getClassResource().getResourceAsStream("objects/" + filePath + ".obj"))))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Parse vertex positions
                Matcher posMatcher = positionPattern.matcher(line);
                if (posMatcher.matches()) {
                    rawPositions.add(Float.parseFloat(posMatcher.group(1)));
                    rawPositions.add(Float.parseFloat(posMatcher.group(2)));
                    rawPositions.add(Float.parseFloat(posMatcher.group(3)));
                    continue;
                }

                // Parse texture coordinates
                Matcher texMatcher = texturePattern.matcher(line);
                if (texMatcher.matches()) {
                    rawTextures.add(Float.parseFloat(texMatcher.group(1)));
                    rawTextures.add(Float.parseFloat(texMatcher.group(2)));
                    continue;
                }

                // Parse vertex normals
                Matcher normMatcher = normalPattern.matcher(line);
                if (normMatcher.matches()) {
                    rawNormals.add(Float.parseFloat(normMatcher.group(1)));
                    rawNormals.add(Float.parseFloat(normMatcher.group(2)));
                    rawNormals.add(Float.parseFloat(normMatcher.group(3)));
                    continue;
                }

                // Parse faces
                Matcher faceMatcher = facePattern.matcher(line);
                if (faceMatcher.matches()) {
                    String[] parts = line.split("\\s+");
                    // Assuming triangular faces (f v1 v2 v3 ...)
                    for (int i = 1; i < parts.length; i++) {
                        String[] indicesStr = parts[i].split("/");

                        // v/vt/vn format
                        int posIndex = Integer.parseInt(indicesStr[0]) - 1;
                        int texIndex = indicesStr.length > 1 && !indicesStr[1].isEmpty() ? Integer.parseInt(indicesStr[1]) - 1 : -1;
                        int normIndex = indicesStr.length > 2 && !indicesStr[2].isEmpty() ? Integer.parseInt(indicesStr[2]) - 1 : -1;

                        // Add vertex data to the final lists
                        positions.add(rawPositions.get(posIndex * 3));
                        positions.add(rawPositions.get(posIndex * 3 + 1));
                        positions.add(rawPositions.get(posIndex * 3 + 2));

                        if (texIndex != -1) {
                            textures.add(rawTextures.get(texIndex * 2));
                            textures.add(rawTextures.get(texIndex * 2 + 1));
                        }

                        if (normIndex != -1) {
                            normals.add(rawNormals.get(normIndex * 3));
                            normals.add(rawNormals.get(normIndex * 3 + 1));
                            normals.add(rawNormals.get(normIndex * 3 + 2));
                        }

                        // Add index for the vertex
                        indices.add(indices.size());
                    }
                }
            }
        }

        // Convert lists to primitive arrays
        Mesh mesh = new Mesh(name);
        mesh.positions = new float[positions.size()];
        for (int i = 0; i < positions.size(); i++) {
            mesh.positions[i] = positions.get(i);
        }

        mesh.textures = new float[textures.size()];
        for (int i = 0; i < textures.size(); i++) {
            mesh.textures[i] = textures.get(i);
        }

        mesh.normals = new float[normals.size()];
        for (int i = 0; i < normals.size(); i++) {
            mesh.normals[i] = normals.get(i);
        }

        mesh.indices = new int[indices.size()];
        for (int i = 0; i < indices.size(); i++) {
            mesh.indices[i] = indices.get(i);
        }

        return mesh;
    }


//    public static Model loadObject(String objFileName) {
//        BufferedReader isr;
//        InputStream objFile = Resources.class.getResourceAsStream("objects/" + objFileName);
//        assert objFile != null;
//        isr = new BufferedReader(new InputStreamReader(objFile));
//        BufferedReader reader = new BufferedReader(isr);
//        String line;
//        List<Normal> vertices = new ArrayList<>();
//        List<Vec2> textures = new ArrayList<>();
//        List<Vec3> normals = new ArrayList<>();
//        List<Integer> indices = new ArrayList<>();
//        try {
//            while (true) {
//                line = reader.readLine();
//                if (line.startsWith("v ")) {
//                    String[] currentLine = line.split(" ");
//                    Vec3 vertex = new Vec3(Float.parseFloat(currentLine[1]),
//                            Float.parseFloat(currentLine[2]),
//                            Float.parseFloat(currentLine[3]));
//                    Normal newVertex = new Normal(vertices.size(), vertex);
//                    vertices.add(newVertex);
//
//                } else if (line.startsWith("vt ")) {
//                    String[] currentLine = line.split(" ");
//                    Vec2 texture = new Vec2(Float.parseFloat(currentLine[1]),
//                            Float.parseFloat(currentLine[2]));
//                    textures.add(texture);
//                } else if (line.startsWith("vn ")) {
//                    String[] currentLine = line.split(" ");
//                    Vec3 normal = new Vec3(Float.parseFloat(currentLine[1]),
//                            Float.parseFloat(currentLine[2]),
//                            Float.parseFloat(currentLine[3]));
//                    normals.add(normal);
//                } else if (line.startsWith("f ")) {
//                    break;
//                }
//            }
//            while (line != null && line.startsWith("f ")) {
//                String[] currentLine = line.split(" ");
//                String[] vertex1 = currentLine[1].split("/");
//                String[] vertex2 = currentLine[2].split("/");
//                String[] vertex3 = currentLine[3].split("/");
//                Normal v0 = processVertex(vertex1, vertices, indices);
//                Normal v1 = processVertex(vertex2, vertices, indices);
//                Normal v2 = processVertex(vertex3, vertices, indices);
//                calculateTangents(v0, v1, v2, textures);//NEW
//                line = reader.readLine();
//            }
//            reader.close();
//        } catch (IOException e) {
//            System.err.println("Error reading the file");
//        }
//        removeUnusedVertices(vertices);
//        float[] verticesArray = new float[vertices.size() * 3];
//        float[] texturesArray = new float[vertices.size() * 2];
//        float[] normalsArray = new float[vertices.size() * 3];
//        float[] tangentsArray = new float[vertices.size() * 3];
//        float furthest = convertDataToArrays(vertices, textures, normals, verticesArray,
//                texturesArray, normalsArray, tangentsArray);
//        int[] indicesArray = convertIndicesListToArray(indices);
//
//        return ModelLoader.loadToVaoInt(verticesArray, texturesArray, normalsArray, indicesArray, tangentsArray);
//    }
//
//    //NEW
//    private static void calculateTangents(Normal v0, Normal v1, Normal v2,
//                                          List<Vec2> textures) {
//        Vec3 deltaPos1 = Vec3.sub(v1.getPosition(), v0.getPosition(), null);
//        Vec3 deltaPos2 = Vec3.sub(v2.getPosition(), v0.getPosition(), null);
//        Vec2 uv0 = textures.get(v0.getTextureIndex());
//        Vec2 uv1 = textures.get(v1.getTextureIndex());
//        Vec2 uv2 = textures.get(v2.getTextureIndex());
//        Vec2 deltaUv1 = Vec2.sub(uv1, uv0, null);
//        Vec2 deltaUv2 = Vec2.sub(uv2, uv0, null);
//
//        float r = 1.0f / (deltaUv1.x * deltaUv2.y - deltaUv1.y * deltaUv2.x);
//        deltaPos1.scale(deltaUv2.y);
//        deltaPos2.scale(deltaUv1.y);
//        Vec3 tangent = Vec3.sub(deltaPos1, deltaPos2, null);
//        tangent.scale(r);
//        v0.addTangent(tangent);
//        v1.addTangent(tangent);
//        v2.addTangent(tangent);
//    }
//
//    private static Normal processVertex(String[] vertex, List<Normal> vertices,
//                                              List<Integer> indices) {
//        int index = Integer.parseInt(vertex[0]) - 1;
//        Normal currentVertex = vertices.get(index);
//        int textureIndex = Integer.parseInt(vertex[1]) - 1;
//        int normalIndex = Integer.parseInt(vertex[2]) - 1;
//        if (!currentVertex.isSet()) {
//            currentVertex.setTextureIndex(textureIndex);
//            currentVertex.setNormalIndex(normalIndex);
//            indices.add(index);
//            return currentVertex;
//        } else {
//            return dealWithAlreadyProcessedVertex(currentVertex, textureIndex, normalIndex, indices,
//                    vertices);
//        }
//    }
//
//    private static int[] convertIndicesListToArray(List<Integer> indices) {
//        int[] indicesArray = new int[indices.size()];
//        for (int i = 0; i < indicesArray.length; i++) {
//            indicesArray[i] = indices.get(i);
//        }
//        return indicesArray;
//    }
//
//    private static float convertDataToArrays(List<Normal> vertices, List<Vec2> textures,
//                                             List<Vec3> normals, float[] verticesArray, float[] texturesArray,
//                                             float[] normalsArray, float[] tangentsArray) {
//        float furthestPoint = 0;
//        for (int i = 0; i < vertices.size(); i++) {
//            Normal currentVertex = vertices.get(i);
//            if (currentVertex.getLength() > furthestPoint) {
//                furthestPoint = currentVertex.getLength();
//            }
//            Vec3 position = currentVertex.getPosition();
//            Vec2 textureCoordinates = textures.get(currentVertex.getTextureIndex());
//            Vec3 normalVector = normals.get(currentVertex.getNormalIndex());
//            Vec3 tangent = currentVertex.getAverageTangent();
//            verticesArray[i * 3] = position.x;
//            verticesArray[i * 3 + 1] = position.y;
//            verticesArray[i * 3 + 2] = position.z;
//            texturesArray[i * 2] = textureCoordinates.x;
//            texturesArray[i * 2 + 1] = 1 - textureCoordinates.y;
//            normalsArray[i * 3] = normalVector.x;
//            normalsArray[i * 3 + 1] = normalVector.y;
//            normalsArray[i * 3 + 2] = normalVector.z;
//            tangentsArray[i * 3] = tangent.x;
//            tangentsArray[i * 3 + 1] = tangent.y;
//            tangentsArray[i * 3 + 2] = tangent.z;
//
//        }
//        return furthestPoint;
//    }
//
//    private static Normal dealWithAlreadyProcessedVertex(Normal previousVertex, int newTextureIndex,
//                                                               int newNormalIndex, List<Integer> indices, List<Normal> vertices) {
//        if (previousVertex.hasSameTextureAndNormal(newTextureIndex, newNormalIndex)) {
//            indices.add(previousVertex.getIndex());
//            return previousVertex;
//        } else {
//            Normal anotherVertex = previousVertex.getDuplicateVertex();
//            if (anotherVertex != null) {
//                return dealWithAlreadyProcessedVertex(anotherVertex, newTextureIndex,
//                        newNormalIndex, indices, vertices);
//            } else {
//                Normal duplicateVertex = previousVertex.duplicate(vertices.size());//NEW
//                duplicateVertex.setTextureIndex(newTextureIndex);
//                duplicateVertex.setNormalIndex(newNormalIndex);
//                previousVertex.setDuplicateVertex(duplicateVertex);
//                vertices.add(duplicateVertex);
//                indices.add(duplicateVertex.getIndex());
//                return duplicateVertex;
//            }
//        }
//    }
//
//    private static void removeUnusedVertices(List<Normal> vertices) {
//        for (Normal vertex : vertices) {
//            vertex.averageTangents();
//            if (!vertex.isSet()) {
//                vertex.setTextureIndex(0);
//                vertex.setNormalIndex(0);
//            }
//        }
//    }

}
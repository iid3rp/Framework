package framework.normal;

import framework.loader.ModelLoader;
import framework.model.Model;
import framework.resources.Resources;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class NormalMappedObjLoader {

	public static Model loadOBJ(String objFileName) {
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

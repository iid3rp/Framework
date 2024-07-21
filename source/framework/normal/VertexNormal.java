package framework.normal;

import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class VertexNormal
{
	
	private static final int NO_INDEX = -1;
	
	private Vector3f position;
	private int textureIndex = NO_INDEX;
	private int normalIndex = NO_INDEX;
	private VertexNormal duplicateVertex = null;
	private int index;
	private float length;
	private List<Vector3f> tangents = new ArrayList<>();
	private Vector3f averagedTangent = new Vector3f(0, 0, 0);
	
	public VertexNormal(int index, Vector3f position){
		this.index = index;
		this.position = position;
		this.length = position.length();
	}
	
	public void addTangent(Vector3f tangent){
		tangents.add(tangent);
	}
	
	//NEW
    public VertexNormal duplicate(int newIndex){
		VertexNormal vertex = new VertexNormal(newIndex, position);
		vertex.tangents = this.tangents;
		return vertex;
	}
	
	public void averageTangents(){
		if(tangents.isEmpty()){
			return;
		}
		averagedTangent.set(0, 0, 0);
		for(Vector3f tangent : tangents){
			averagedTangent.add(tangent);
		}
		averagedTangent.normalize();
	}
	
	public Vector3f getAverageTangent(){
		return averagedTangent;
	}
	
	public int getIndex(){
		return index;
	}
	
	public float getLength(){
		return length;
	}
	
	public boolean isSet(){
		return textureIndex != NO_INDEX && normalIndex != NO_INDEX;
	}
	
	public boolean hasSameTextureAndNormal(int textureIndexOther, int normalIndexOther){
		return textureIndexOther==textureIndex && normalIndexOther==normalIndex;
	}
	
	public void setTextureIndex(int textureIndex){
		this.textureIndex = textureIndex;
	}
	
	public void setNormalIndex(int normalIndex){
		this.normalIndex = normalIndex;
	}

	public Vector3f getPosition() {
		return position;
	}

	public int getTextureIndex() {
		return textureIndex;
	}

	public int getNormalIndex() {
		return normalIndex;
	}

	public VertexNormal getDuplicateVertex() {
		return duplicateVertex;
	}

	public void setDuplicateVertex(VertexNormal duplicateVertex) {
		this.duplicateVertex = duplicateVertex;
	}

}

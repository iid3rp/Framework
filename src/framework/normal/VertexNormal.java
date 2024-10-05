package framework.normal;

import framework.lang.Vec3;

import java.util.ArrayList;
import java.util.List;

public class VertexNormal
{
	
	private static final int NO_INDEX = -1;
	
	private Vec3 position;
	private int textureIndex = NO_INDEX;
	private int normalIndex = NO_INDEX;
	private VertexNormal duplicateVertex = null;
	private int index;
	private float length;
	private List<Vec3> tangents = new ArrayList<>();
	private Vec3 averagedTangent = new Vec3(0, 0, 0);
	
	public VertexNormal(int index, Vec3 position){
		this.index = index;
		this.position = position;
		this.length = position.length();
	}
	
	public void addTangent(Vec3 tangent){
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
		for(Vec3 tangent : tangents){
			averagedTangent.add(tangent);
		}
		averagedTangent.normalize();
	}
	
	public Vec3 getAverageTangent(){
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

	public Vec3 getPosition() {
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

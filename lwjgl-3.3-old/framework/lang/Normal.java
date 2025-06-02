package framework.lang;

import framework.util.LinkList;


public class Normal
{

    private static final int NO_INDEX = -1;

    private Vec3 position;
    private int textureIndex = NO_INDEX;
    private int normalIndex = NO_INDEX;
    private Normal duplicateVertex = null;
    private int index;
    private float length;
    private LinkList<Vec3> tangents = new LinkList<>();
    private Vec3 averagedTangent = new Vec3();

    public Normal(int index, Vec3 position){
        this.index = index;
        this.position = position;
        this.length = position.length();
    }

    public void addTangent(Vec3 tangent){
        tangents.addAll(tangent);
    }

    //NEW
    public Normal duplicate(int newIndex){
        Normal n = new Normal(newIndex, position);
        n.tangents = this.tangents;
        return n;
    }

    public void averageTangents(){
        if(tangents.isEmpty()){
            return;
        }
        for(Vec3 tangent : tangents){
            Vec3.add(averagedTangent, tangent, averagedTangent);
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
        return textureIndex!=NO_INDEX && normalIndex!=NO_INDEX;
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

    public Normal getDuplicateVertex() {
        return duplicateVertex;
    }

    public void setDuplicateVertex(Normal duplicateVertex) {
        this.duplicateVertex = duplicateVertex;
    }

}

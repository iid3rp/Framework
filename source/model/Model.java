package model;

public class Model
{
    private int vaoID;
    private int vertexCount;

    public Model(int id, int vc)
    {
        vaoID = id;
        vertexCount = vc;
    }

    public int getVaoID()
    {
        return vaoID;
    }

    public void setVaoID(int vaoID)
    {
        this.vaoID = vaoID;
    }

    public int getVertexCount()
    {
        return vertexCount;
    }

    public void setVertexCount(int vertexCount)
    {
        this.vertexCount = vertexCount;
    }
}

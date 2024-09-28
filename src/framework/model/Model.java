package framework.model;

public class Model
{
    private float furthestPoint;
    private final int vaoId;
    private final int vertexCount;

    public Model(int vaoId, int vertexCount)
    {
        this.vaoId = vaoId;
        this.vertexCount = vertexCount;
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }
}

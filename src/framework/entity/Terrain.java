package framework.entity;

import framework.model.Model;
import framework.model.TexturedModel;
import framework.textures.Texture;

public class Terrain extends Entity
{
    private int vertexCount;
    private float x;
    private float z;

    public Terrain(float studX, float studZ, float vertex, Texture texture)
    {
        super();
        Model m = createFlatModel(studX, studZ, vertex);
        TexturedModel model = new TexturedModel(m, texture);

        x = studX;
        z = studZ;
    }

    private Model createFlatModel(float studX, float studZ, float vertex)
    {
        return null;
    }
}

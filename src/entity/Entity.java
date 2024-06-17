package entity;

import model.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

/**
 * this is like the superclass of the whole rendering system in such that
 * it will render an entity, it could be a block, an object, or
 * anything at this point
 * <p>
 * @Citation: Karl Wimble (@ThinMatrix)
 * @author Francis Madanlo
 */
public class Entity
{
    private TexturedModel model;
    private Vector3f position;
    private float rotationX;
    private float rotationY;
    private float rotationZ;
    private float scale;
    private int textureIndex;
    public Entity(TexturedModel model, Vector3f position, float rotationX, float rotationY, float rotationZ, float scale)
    {
        this.model = model;
        this.position = position;
        this.rotationX = rotationX;
        this.rotationY = rotationY;
        this.rotationZ = rotationZ;
        this.scale = scale;
        textureIndex = 1;
    }

    public Entity(TexturedModel model, Vector3f position, float rotationX, float rotationY, float rotationZ, float scale, int textureIndex)
    {
        this.model = model;
        this.position = position;
        this.rotationX = rotationX;
        this.rotationY = rotationY;
        this.rotationZ = rotationZ;
        this.scale = scale;
        this.textureIndex = textureIndex;
    }

    public Entity(Entity entity)
    {
        // copy constructor
        this.model = entity.getModel();
        this.position = entity.getPosition();
        this.rotationX = entity.getRotationX();
        this.rotationY = entity.getRotationY();
        this.rotationZ = entity.getRotationZ();
        this.scale = entity.getScale();
        textureIndex = 1;
    }

    public void transformPosition(float x, float y, float z)
    {
        this.position.x += x;
        this.position.y += y;
        this.position.z += z;
    }

    public void transformRotation(float x, float y, float z)
    {
        this.rotationX += x;
        this.rotationY += y;
        this.rotationZ += z;
    }

    public float getTextureXOffset()
    {
        int column = textureIndex % model.getTexture().getNumberOfRows();
        return (float) column / (float) model.getTexture().getNumberOfRows();
    }

     public float getTextureYOffset()
     {
         int row = textureIndex / model.getTexture().getNumberOfRows();
         return (float) row / (float) model.getTexture().getNumberOfRows();
     }

    public TexturedModel getModel()
    {
        return model;
    }

    public void setModel(TexturedModel model)
    {
        this.model = model;
    }

    public Vector3f getPosition()
    {
        return position;
    }

    public void setPosition(Vector3f position)
    {
        this.position = position;
    }

    public float getRotationX()
    {
        return rotationX;
    }

    public void setRotationX(float rotationX)
    {
        this.rotationX = rotationX;
    }

    public float getRotationY()
    {
        return rotationY;
    }

    public void setRotationY(float rotationY)
    {
        this.rotationY = rotationY;
    }

    public float getRotationZ()
    {
        return rotationZ;
    }

    public void setRotationZ(float rotationZ)
    {
        this.rotationZ = rotationZ;
    }

    public float getScale()
    {
        return scale;
    }

    public void setScale(float scale)
    {
        this.scale = scale;
    }

    public void setTextureIndex(int index)
    {
        textureIndex = index;
    }
}

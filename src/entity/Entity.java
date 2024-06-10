package entity;

import model.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

/**
 * this is like the superclass of the whole rendering system in such that
 * it will render an enitity, it could be a block, an object, or
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
    public Entity(TexturedModel model, Vector3f position, float rotationX, float rotationY, float rotationZ, float scale)
    {
        this.model = model;
        this.position = position;
        this.rotationX = rotationX;
        this.rotationY = rotationY;
        this.rotationZ = rotationZ;
        this.scale = scale;
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
}

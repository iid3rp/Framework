package framework.entity;
import framework.environment.Engine;
import framework.lang.GeomMath;
import framework.lang.Mat4;
import framework.model.TexturedModel;

public class Entity
{
    private TexturedModel model;
    private float posX, posY, posZ;
    private float rotX, rotY, rotZ;
    private float scaleX, scaleY, scaleZ;
    private Mat4 transformationMatrix;
    private float red, green, blue, alpha;
    public Entity(TexturedModel model)
    {
        this.model = model;
    }

    public TexturedModel getModel()
    {
        return model;
    }

    public void setPosition(float x, float y, float z)
    {
        posX = x;
        posY = y;
        posZ = z;
        updateTransformationMatrix();
    }

    public void setRotation(float x, float y, float z)
    {
        rotX = x;
        rotY = y;
        rotZ = z;
        updateTransformationMatrix();
    }

    public void setScale(float x, float y, float z)
    {
        scaleX = x;
        scaleY = y;
        scaleZ = z;
        updateTransformationMatrix();
    }

    public void transformPosition(float x, float y, float z)
    {
        posX += x;
        posY += y;
        posZ += z;
        updateTransformationMatrix();
    }

    public void transformRotation(float x, float y, float z)
    {
        rotX += x;
        rotY += y;
        rotZ += z;
        updateTransformationMatrix();
    }

    public void transformScale(float x, float y, float z)
    {
        scaleX += x;
        scaleY += y;
        scaleZ += z;
        updateTransformationMatrix();
    }

    public float getPosX()
    {
        return posX;
    }

    public float getPosY()
    {
        return posY;
    }

    public float getPosZ()
    {
        return posZ;
    }

    public float getRotX()
    {
        return rotX;
    }

    public float getRotY()
    {
        return rotY;
    }

    public float getRotZ()
    {
        return rotZ;
    }

    public float getScaleX()
    {
        return scaleX;
    }

    public float getScaleY()
    {
        return scaleY;
    }

    public float getScaleZ()
    {
        return scaleZ;
    }

    public void setModel(TexturedModel model)
    {
        this.model = model;
    }

    public Mat4 getTransformationMatrix()
    {
        return transformationMatrix;
    }

    public void setColor(int red, int green, int blue, int alpha)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public float getRed()
    {
        return red;
    }

    public float getGreen()
    {
        return green;
    }

    public float getBlue()
    {
        return blue;
    }

    public void updateTransformationMatrix()
    {
        Engine.mainExecute(() -> transformationMatrix = GeomMath.createTransformationMatrix(posX, posY, posZ, rotX, rotY, rotZ, scaleX, scaleY, scaleZ));
    }

    public float getAlpha()
    {
        return alpha;
    }

}

package framework.textures;

import java.nio.IntBuffer;

public class Texture
{
    private int diffuseMap;
    private int normalMap;
    private int specularMap;

    private float shineDampening = 1;
    private float reflectivity = 0;
    private int numberOfRows = 1;

    private boolean hasTransparency;
    private boolean useFakeLighting;
    private boolean hasSpecularMap;
    private IntBuffer diffuseBuffer;
    private IntBuffer normalBuffer;
    private IntBuffer specularBuffer;

    public Texture(int id)
    {
        this.diffuseMap = id;
        this.normalMap = 0;
        this.specularMap = 0;

    }

    public Texture()
    {
        diffuseMap = normalMap = specularMap = 0;

    }

    public void setSpecularMap(int specularMap)
    {
        this.specularMap = specularMap;
        hasSpecularMap = true;
    }

    public boolean hasSpecularMap()
    {
        return hasSpecularMap;
    }

    public int getSpecularMap()
    {
        return specularMap;
    }

    public int getNormalMap()
    {
        return normalMap;
    }

    public void setNormalMap(int normalMap)
    {
        this.normalMap = normalMap;
    }

    public int getNumberOfRows()
    {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows)
    {
        this.numberOfRows = numberOfRows;
    }

    public boolean hasTransparency()
    {
        return hasTransparency;
    }

    public boolean isUseFakeLighting()
    {
        return useFakeLighting;
    }

    public void setUseFakeLighting(boolean useFakeLighting)
    {
        this.useFakeLighting = useFakeLighting;
    }

    public void setTransparency(boolean hasTransparency)
    {
        this.hasTransparency = hasTransparency;
    }

    public int getID()
    {
        return diffuseMap;
    }

    public float getShineDampening()
    {
        return shineDampening;
    }

    public Texture setShineDampening(float shineDampening)
    {
        this.shineDampening = shineDampening;
        return this;
    }

    public float getReflectivity()
    {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity)
    {
        this.reflectivity = reflectivity;
    }

    public int getTextureId()
    {
        return diffuseMap;
    }

    public void setTextureID(int textureId)
    {
        this.diffuseMap = textureId;
    }

    public void setDiffuseBuffer(IntBuffer diffuseBuffer)
    {
        this.diffuseBuffer = diffuseBuffer;
    }

    public IntBuffer getDiffuseBuffer()
    {
        return diffuseBuffer;
    }

    public void setNormalBuffer(IntBuffer normalBuffer)
    {
        this.normalBuffer = normalBuffer;
    }

    public IntBuffer getNormalBuffer()
    {
        return normalBuffer;
    }

    public void setSpecularBuffer(IntBuffer specularBuffer)
    {
        this.specularBuffer = specularBuffer;
    }

    public IntBuffer getSpecularBuffer()
    {
        return specularBuffer;
    }
}

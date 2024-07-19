package framework.textures;

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

    public Texture(int id)
    {
        this.diffuseMap = id;
    }

    public Texture() {}

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

    public void setTextureID(int textureID)
    {
        this.diffuseMap = textureID;
    }
}

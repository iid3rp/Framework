package texture;

public class Texture
{
    private int textureID;
    private float shineDampening = 1;
    private float reflectivity = 0;

    public Texture(int id)
    {
        this.textureID = id;
    }

    public int getID()
    {
        return textureID;
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
}

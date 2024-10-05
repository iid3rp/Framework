package framework.particles;

public class ParticleTexture
{
    private int textureID;
    private int numOfRows;
    private boolean additive;

    public ParticleTexture(int textureID, int numOfRows)
    {
        this.textureID = textureID;
        this.numOfRows = numOfRows;
    }

    public ParticleTexture(int textureID, int numOfRows, boolean additive)
    {
        this.textureID = textureID;
        this.numOfRows = numOfRows;
        this.additive = additive;
    }

    public boolean isAdditive()
    {
        return additive;
    }

    public void setAdditive(boolean additive)
    {
        this.additive = additive;
    }

    public int getTextureID()
    {
        return textureID;
    }

    public int getNumOfRows()
    {
        return numOfRows;
    }
}

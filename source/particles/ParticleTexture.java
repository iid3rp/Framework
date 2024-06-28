package particles;

public class ParticleTexture
{
    private int textureID;
    private int numOfRows;

    public ParticleTexture(int textureID, int numOfRows)
    {
        this.textureID = textureID;
        this.numOfRows = numOfRows;
    }

    public int getTextureID()
    {
        return textureID;
    }

    public int getNumParticles()
    {
        return numOfRows;
    }
}

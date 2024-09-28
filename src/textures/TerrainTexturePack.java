package textures;

public class TerrainTexturePack
{
    private TerrainTexture background;
    private TerrainTexture red;
    private TerrainTexture green;
    private TerrainTexture blue;
    private TerrainTexture blendMap;

    public TerrainTexturePack(TerrainTexture background, TerrainTexture red, TerrainTexture green, TerrainTexture blue)
    {
        this.background = background;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public TerrainTexture getBackground()
    {
        return background;
    }

    public TerrainTexture getRed()
    {
        return red;
    }

    public TerrainTexture getGreen()
    {
        return green;
    }

    public TerrainTexture getBlue()
    {
        return blue;
    }
}

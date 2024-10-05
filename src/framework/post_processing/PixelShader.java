package framework.post_processing;

import framework.lang.Vec2;
import framework.shader.GLShader;

public class PixelShader extends GLShader
{
    public static final String VERTEX_FILE = "pixelVertexShader.glsl";
    public static final String FRAGMENT_FILE = "pixelFragmentShader.glsl";
    private int locationTexture;
    private int locationPosition;
    //private int locationTextureSize;

    public PixelShader()
    {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    public void loadTexture(int id)
    {
        super.loadInt(locationTexture, id);
    }

    public void loadPosition(Vec2 position)
    {
        super.loadVector(locationPosition, position);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

    @Override
    protected void getAllUniformLocations()
    {
        locationTexture = super.getUniformLocation("screenTexture");
        locationPosition = super.getUniformLocation("position");
        //locationTextureSize = super.getUniformLocation("textureSize");
    }

    public void connectTextureUnits(){
    }
}

package framework.post_processing;

import framework.lang.Vec2;
import framework.shader.GLShader;

public class GodRayShader extends GLShader
{
    private static final String VERTEX_FILE = "horizontalBlurVertexShader.glsl";
    private static final String FRAGMENT_FILE = "blurFragmentShader.glsl";
    private int locationTexture;
    private int locationPosition;

    public GodRayShader()
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
    }

    public void connectTextureUnits(){
    }
}

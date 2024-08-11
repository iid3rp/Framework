package framework.fontExperiment;

import framework.shader.GLShader;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public class TextShader extends GLShader
{
    public static final String VERTEX_FILE = "textVertexShader.glsl";
    public static final String FRAGMENT_FILE = "textFragmentShader.glsl";
    private int locationTransformationMatrix;
    private int locationSize;
    private int locationPosition;
    private int locationScale;
    private int locationTexturePosition;
    private int locationDimension;
    private int locationOffset;
    private int locationScaleMatrix;

    public TextShader()
    {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations()
    {
        locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
        locationSize = super.getUniformLocation("size");
        locationPosition = super.getUniformLocation("pos");
        locationScale = super.getUniformLocation("scale");
        locationTexturePosition = super.getUniformLocation("texturePosition");
        locationDimension = super.getUniformLocation("dimension");
        locationOffset = super.getUniformLocation("offset");
        locationScaleMatrix = super.getUniformLocation("scaleMatrix");

    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        //super.bindAttribute(1, "textureCoords");
    }


    public void loadSize(Vector2f size)
    {
        super.loadVector(locationSize, size);
    }

    public void loadPosition(Vector2f position)
    {
        super.loadVector(locationPosition, position);
    }

    public void loadScale(Vector2f scale)
    {
        super.loadVector(locationScale, scale);
    }

    public void loadTransformation(Matrix4f matrix)
    {
        super.loadMatrix(locationTransformationMatrix, matrix);
    }

    public void loadTexturePosition(Vector2f normal)
    {
        super.loadVector(locationTexturePosition, normal);
    }

    public void loadOffset(Vector2f normal)
    {
        super.loadVector(locationOffset, normal);
    }

    public void loadScaleMatrix(Matrix4f scaleMatrix)
    {
        super.loadMatrix(locationScaleMatrix, scaleMatrix);
    }
}

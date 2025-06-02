package framework.font;

import framework.lang.Mat4;
import framework.lang.Vec2;
import framework.lang.Vec3;
import framework.shader.GLShader;

import java.awt.Color;

public class TextShader extends GLShader
{
    public static final String VERTEX_FILE = "textVertexShader.glsl";
    public static final String FRAGMENT_FILE = "textFragmentShader.glsl";
    private int locationTransformationMatrix;
    private int locationSize;
    private int locationPosition;
    private int locationScale;
    private int locationFontLocation;
    private int locationOffset;
    private int locationWidth;
    private int locationEdge;
    private int locationBorderWidth;
    private int locationBorderEdge;
    private int locationOutlineColor;
    private int locationForegroundColor;

    public TextShader()
    {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations()
    {
        // vertex shader uniforms
        locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
        locationFontLocation = super.getUniformLocation("fontLocation");
        locationSize = super.getUniformLocation("size");
        locationScale = super.getUniformLocation("scale");

        // fragment shader uniforms
        locationWidth = super.getUniformLocation("width");
        locationEdge = super.getUniformLocation("edge");
        locationBorderWidth = super.getUniformLocation("borderWidth");
        locationBorderEdge = super.getUniformLocation("borderEdge");
        locationOffset = super.getUniformLocation("offset");
        locationOutlineColor = super.getUniformLocation("outlineColor");
        locationForegroundColor = super.getUniformLocation("foregroundColor");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }


    public void loadSize(Vec2 size)
    {
        super.loadVector(locationSize, size);
    }

    public void loadPosition(Vec2 position)
    {
        super.loadVector(locationPosition, position);
    }

    public void loadScale(Vec2 scale)
    {
        super.loadVector(locationScale, scale);
    }

    public void loadTransformation(Mat4 matrix)
    {
        super.loadMatrix(locationTransformationMatrix, matrix);
    }

    public void loadFontLocation(Vec2 normal)
    {
        super.loadVector(locationFontLocation, normal);
    }

    public void loadOffset(Vec2 normal)
    {
        super.loadVector(locationOffset, normal);
    }

    public void loadBorderWidth(float width)
    {
        super.loadFloat(locationBorderWidth, width);
    }

    public void loadBorderEdge(float edge)
    {
        super.loadFloat(locationBorderEdge, edge);
    }

    public void loadEdge(float edge)
    {
        super.loadFloat(locationEdge, edge);
    }

    public void loadOutlineColor(Color color)
    {
        Vec3 c = new Vec3(
                color.getRed() / 255f,
                color.getGreen() / 255f,
                color.getBlue() / 255f
        );
        super.loadVector(locationOutlineColor, c);
    }

    public void loadForegroundColor(Color color)
    {
        Vec3 vec3 = new Vec3(
                color.getRed() / 255f,
                color.getGreen() / 255f,
                color.getBlue() / 255f
        );
        super.loadVector(locationForegroundColor, vec3);
    }

    public void loadWidth(float width)
    {
        super.loadFloat(locationWidth, width);
    }
}

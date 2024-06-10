package Shaders;

import Entities.Camera;
import Entities.Lighting;
import Toolbox.MatrixMultiplication;
import org.lwjgl.util.vector.Matrix4f;

public class StaticShader extends ShaderReader
{
    public static final String VERTEX_FILE = "src/Shaders/vertexShader.glsl";
    public static final String FRAGMENT_FILE = "src/Shaders/fragmentShader.glsl";
    private int locationTransform;
    private int locationProjection;
    private int locationView;
    private int locationLightPosition;
    private int locationLightColor;

    public StaticShader()
    {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations()
    {
        locationTransform = super.getUniformLocation("transform");
        locationProjection = super.getUniformLocation("projection");
        locationView = super.getUniformLocation("view");
        locationLightPosition = super.getUniformLocation("lightPosition");
        locationLightColor = super.getUniformLocation("lightColor");
    }

    @Override
    protected void bindAttributes()
    {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "normal");
    }

    public void loadLighting(Lighting light)
    {
        super.loadVector(locationLightPosition, light.getPosition());
        super.loadVector(locationLightColor, light.getColor());
    }

    public void loadTransformMatrix(Matrix4f matrix)
    {
        super.loadMatrix(locationTransform, matrix);
    }

    public void loadViewMatrix(Camera camera)
    {
        Matrix4f view = MatrixMultiplication.createViewMatrix(camera);
        super.loadMatrix(locationView, view);
    }

    public void loadProjectionMatrix(Matrix4f matrix)
    {
        super.loadMatrix(locationProjection, matrix);
    }
}

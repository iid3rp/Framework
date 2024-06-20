package shader;

import entity.Camera;
import entity.Lighting;
import org.lwjgl.util.vector.Matrix4f;
import toolbox.MatrixMultiplication;

public class TerrainShader extends GLShader
{
    public static final String VERTEX_FILE = "source/shader/terrainVertexShader.glsl";
    public static final String FRAGMENT_FILE = "source/shader/terrainFragmentShader.glsl";
    private int locationTransform;
    private int locationProjection;
    private int locationView;
    private int locationLightPosition;
    private int locationLightColor;
    private int locationDamper;
    private int locationReflectivity;

    public TerrainShader()
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
        locationDamper = super.getUniformLocation("damper");
        locationReflectivity = super.getUniformLocation("reflectivity");
    }

    public void loadShine(float damper, float reflectivity)
    {
        super.loadFloat(locationDamper, damper);
        super.loadFloat(locationReflectivity, reflectivity);
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

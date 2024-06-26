package shader;

import entity.Camera;
import entity.Lighting;
import toolbox.MatrixMultiplication;
import org.lwjgl.util.vector.Matrix4f;

public class Shader extends GLShader
{
    public static final String VERTEX_FILE = "src/shader/entityVertexShader.glsl";
    public static final String FRAGMENT_FILE = "src/shader/entityFragmentShader.glsl";
    private int locationTransform;
    private int locationProjection;
    private int locationView;
    private int locationLightPosition;
    private int locationLightColor;
    private int locationDamper;
    private int locationReflectivity;
    private int locationFakeLighting;

    public Shader()
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
        locationFakeLighting = super.getUniformLocation("useFakeLighting");
    }

    public void loadShine(float damper, float reflectivity)
    {
        super.loadFloat(locationDamper, damper);
        super.loadFloat(locationReflectivity, reflectivity);
    }

    public void loadFakeLighting(boolean value)
    {
        super.loadBoolean(locationFakeLighting, value);
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

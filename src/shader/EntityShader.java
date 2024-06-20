package shader;

import entity.Camera;
import entity.Lighting;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.GeomMath;
import org.lwjgl.util.vector.Matrix4f;

public class EntityShader extends GLShader
{
    public static final String VERTEX_FILE = "source/src.script/vertexShader.glsl";
    public static final String FRAGMENT_FILE = "source/src.script/fragmentShader.glsl";
    private int locationTransform;
    private int locationProjection;
    private int locationView;
    private int locationLightPosition;
    private int locationLightColor;
    private int locationDamper;
    private int locationReflectivity;
    private int locationFakeLighting;
    private int locationSkyColor;
    private int locationNumberOfRows;
    private int locationOffset;


    public EntityShader()
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
        locationSkyColor = super.getUniformLocation("skyColor");
        locationNumberOfRows = super.getUniformLocation("numberOfRows");
        locationOffset = super.getUniformLocation("offset");
    }

    public void loadNumberOfRows(int numberOfRows)
    {
        super.loadFloat(locationNumberOfRows, numberOfRows);
    }

    public void loadOffset(float x, float y)
    {
        super.loadVector2f(locationOffset, new Vector2f(x, y));
    }

    public void loadSkyColor(float r, float g, float b)
    {
        super.loadVector3f(locationSkyColor, new Vector3f(r, g, b));
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
        super.loadVector3f(locationLightPosition, light.getPosition());
        super.loadVector3f(locationLightColor, light.getColor());
    }

    public void loadTransformMatrix(Matrix4f matrix)
    {
        super.loadMatrix(locationTransform, matrix);
    }
    public void loadViewMatrix(Camera camera)
    {
        Matrix4f view = GeomMath.createViewMatrix(camera);
        super.loadMatrix(locationView, view);
    }
    public void loadProjectionMatrix(Matrix4f matrix)
    {
        super.loadMatrix(locationProjection, matrix);
    }
}

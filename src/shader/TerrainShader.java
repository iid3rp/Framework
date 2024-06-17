package shader;

import entity.Camera;
import entity.Lighting;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.GeomMath;

public class TerrainShader extends GLShader
{
    public static final String VERTEX_FILE = "src/script/terrainVertexShader.glsl";
    public static final String FRAGMENT_FILE = "src/script/terrainFragmentShader.glsl";
    private int locationTransform;
    private int locationProjection;
    private int locationView;
    private int locationLightPosition;
    private int locationLightColor;
    private int locationDamper;
    private int locationReflectivity;
    private int locationSkyColor;
    private int locationBackground;
    private int locationRed;
    private int locationGreen;
    private int locationBlue;
    private int blendMap;

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
        locationSkyColor = super.getUniformLocation("skyColor");
        locationBackground = super.getUniformLocation("background");
        locationRed = super.getUniformLocation("red");
        locationGreen = super.getUniformLocation("green");
        locationBlue = super.getUniformLocation("blue");
        blendMap = super.getUniformLocation("blendMap");
    }

    public void connectTextureUnits()
    {
        super.loadInteger(locationBackground, 0);
        super.loadInteger(locationRed, 1);
        super.loadInteger(locationGreen, 2);
        super.loadInteger(locationBlue, 3);
        super.loadInteger(blendMap, 4);
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
        super.loadVector3f(locationLightPosition, light.getPosition());
        super.loadVector3f(locationLightColor, light.getColor());
    }

    public void loadSkyColor(float r, float g, float b)
    {
        super.loadVector3f(locationSkyColor, new Vector3f(r, g, b));
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

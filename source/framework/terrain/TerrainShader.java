package framework.terrain;

import framework.entity.Camera;
import framework.entity.Light;
import framework.shader.GLShader;
import framework.toolbox.GeomMath;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.util.List;

public class TerrainShader extends GLShader
{
    public static final String VERTEX_FILE = "terrainVertexShader.glsl";
    public static final String FRAGMENT_FILE = "terrainFragmentShader.glsl";
    private int locationTransform;
    private int locationProjection;
    private int locationView;
    private int locationLightAmount;
    private int[] locationLightPosition;
    private int[] locationLightColor;
    private int[] locationAttenuation;
    private int locationDamper;
    private int locationReflectivity;
    private int locationSkyColor;
    private int locationBackground;
    private int locationRed;
    private int locationGreen;
    private int locationBlue;
    private int blendMap;
    private int locationPlane;
    private int locationToShadowMapSpace;
    private int locationShadowMap;


    public TerrainShader()
    {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations()
    {
        locationLightAmount = super.getUniformLocation("lightAmount");
        locationTransform = super.getUniformLocation("transform");
        locationProjection = super.getUniformLocation("projection");
        locationView = super.getUniformLocation("view");
        locationDamper = super.getUniformLocation("damper");
        locationReflectivity = super.getUniformLocation("reflectivity");
        locationBackground = super.getUniformLocation("background");
        locationRed = super.getUniformLocation("red");
        locationGreen = super.getUniformLocation("green");
        locationBlue = super.getUniformLocation("blue");
        blendMap = super.getUniformLocation("blendMap");
        locationPlane = super.getUniformLocation("plane");
        locationToShadowMapSpace = super.getUniformLocation("toShadowMapSpace");
        locationShadowMap = super.getUniformLocation("shadowMap");
        locationSkyColor = super.getUniformLocation("skyColor");

        locationLightPosition = new int[lights];
        locationLightColor = new int[lights];
        locationAttenuation = new int[lights];

        for(int i = 0; i < lights; i++)
        {
            locationLightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
            locationLightColor[i] = super.getUniformLocation("lightColor[" + i + "]");
            locationAttenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
        }
    }

    public void loadClipPlane(Vector4f plane)
    {
        super.loadVector4f(locationPlane, plane);
    }

    public void connectTextureUnits()
    {
        super.loadInteger(locationBackground, 0);
        super.loadInteger(locationRed, 1);
        super.loadInteger(locationGreen, 2);
        super.loadInteger(locationBlue, 3);
        super.loadInteger(blendMap, 4);
        super.loadInteger(locationShadowMap, 5);
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

    public void loadToShadowSpaceMatrix(Matrix4f matrix)
    {
        super.loadMatrix(locationToShadowMapSpace, matrix);
    }

    public void loadLights(List<Light> lights)
    {
        for(int i = 0; i < this.lights; i++)
        {
            if(i < lights.size())
            {
                super.loadVector3f(locationLightPosition[i], lights.get(i).getPosition());
                super.loadVector3f(locationLightColor[i], lights.get(i).getColor());
                super.loadVector3f(locationAttenuation[i], lights.get(i).getAttenuation());
            }
            else
            {
                super.loadVector3f(locationLightPosition[i], new Vector3f(0, 0, 0));
                super.loadVector3f(locationLightColor[i], new Vector3f(0, 0, 0));
                super.loadVector3f(locationAttenuation[i], new Vector3f(1, 0, 0));
            }
        }
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

    public void loadLightAmount(int size)
    {
        super.loadInteger(locationLightAmount, size);
    }
}

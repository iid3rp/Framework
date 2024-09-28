package framework.entity;

import framework.shader.GLShader;
import framework.toolbox.GeomMath;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import org.lwjgl.util.vector.Matrix4f;

import java.util.List;

public class EntityShader extends GLShader
{
    public static final String VERTEX_FILE = "entityVertexShader.glsl";
    public static final String FRAGMENT_FILE = "entityFragmentShader.glsl";
    private int locationTransform;
    private int locationProjection;
    private int locationView;
    private int locationLightAmount;
    private int[] locationLightPosition;
    private int[] locationLightColor;
    private int[] locationAttenuation;
    private int locationDamper;
    private int locationReflectivity;
    private int locationFakeLighting;
    private int locationNumberOfRows;
    private int locationOffset;
    private int locationPlane;
    private int locationSkyColor;
    private int locationToShadowMapSpace;
    private int locationTextureMap;
    private int locationShadowMap;
    private int locationSpecularMap;
    private int locationUsesSpecularMap;

    public EntityShader()
    {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations()
    {
        locationTextureMap = super.getUniformLocation("texture");
        locationSpecularMap = super.getUniformLocation("specular");
        locationUsesSpecularMap = super.getUniformLocation("usesSpecular");
        locationLightAmount = super.getUniformLocation("lightAmount");
        locationTransform = super.getUniformLocation("transform");
        locationProjection = super.getUniformLocation("projection");
        locationView = super.getUniformLocation("view");
        locationDamper = super.getUniformLocation("damper");
        locationReflectivity = super.getUniformLocation("reflectivity");
        locationFakeLighting = super.getUniformLocation("useFakeLighting");
        locationNumberOfRows = super.getUniformLocation("numberOfRows");
        locationOffset = super.getUniformLocation("offset");
        locationPlane = super.getUniformLocation("plane");
        locationSkyColor = super.getUniformLocation("skyColor");
        locationToShadowMapSpace = super.getUniformLocation("toShadowMapSpace");
        locationShadowMap = super.getUniformLocation("shadowMap");


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

    public void loadToShadowMapSpaceMatrix(Matrix4f matrix)
    {
        super.loadMatrix(locationToShadowMapSpace, matrix);
    }

    public void loadSkyColor(float r, float g, float b)
    {
        super.loadVector3f(locationSkyColor, new Vector3f(r, g, b));
    }

    public void loadClipPlane(Vector4f plane)
    {
        super.loadVector4f(locationPlane, plane);
    }

    public void loadNumberOfRows(int numberOfRows)
    {
        super.loadFloat(locationNumberOfRows, numberOfRows);
    }

    public void loadLightAmount(int lights)
    {
        super.loadInteger(locationLightAmount, lights);
    }

    public void loadOffset(float x, float y)
    {
        super.loadVector2f(locationOffset, new Vector2f(x, y));
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

    public void connectTextureUnits()
    {
        super.loadInteger(locationShadowMap, 5);
        super.loadInteger(locationTextureMap, 0);
        super.loadInteger(locationSpecularMap, 1);
    }

    public void loadUsesSpecularMap(boolean value)
    {
        super.loadBoolean(locationUsesSpecularMap, value);
    }

    @Override
    protected void bindAttributes()
    {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "framework/normal");
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

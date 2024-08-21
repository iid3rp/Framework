package framework.shader;

import framework.entity.Camera;
import framework.entity.Light;
import framework.util.GeomMath;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

public class TerrainShader extends GLShader
{
    private static final String VERTEX_FILE = "TerrainVertexShader.glsl";
    private static final String FRAGMENT_FILE = "TerrainFragmentShader.glsl";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int[] location_lightPosition;
    private int[] location_lightColor;
    private int location_shineDamper;
    private int location_reflectivity;
    private int location_skyColor;
    private int location_backgroundTexture;
    private int location_rTexture;
    private int location_gTexture;
    private int location_bTexture;
    private int location_blendMap;
    private int locationPlane;
    private int[] locationAttenuation;
    private int locationShadowMapMatrix;
    private int locationShadowMap;

    public TerrainShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "normal");
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_shineDamper = super.getUniformLocation("shineDamper");
        location_reflectivity = super.getUniformLocation("reflectivity");
        location_skyColor = super.getUniformLocation("skyColor");
        location_backgroundTexture = super.getUniformLocation("backgroundTexture");
        location_rTexture = super.getUniformLocation("rTexture");
        location_gTexture = super.getUniformLocation("gTexture");
        location_bTexture = super.getUniformLocation("bTexture");
        location_blendMap = super.getUniformLocation("blendMap");
        locationPlane = super.getUniformLocation("plane");
        locationShadowMapMatrix = super.getUniformLocation("toShadowMapSpace");
        locationShadowMap = super.getUniformLocation("shadowMap");

        location_lightPosition = new int[20];
        location_lightColor = new int[20];
        locationAttenuation = new int[20];

        for(int i = 0; i < 20; i++)
        {
            location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
            location_lightColor[i] = super.getUniformLocation("lightColor[" + i + "]");
            locationAttenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
        }
    }

    public void loadShadowMatrix(Matrix4f matrix)
    {
        super.loadMatrix(locationShadowMapMatrix, matrix);
    }
    public void loadClipPlane(Vector4f plane)
    {
        super.loadVector(locationPlane, plane);
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f projectionMatrix) {
        super.loadMatrix(location_projectionMatrix, projectionMatrix);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = GeomMath.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }

    public void loadLights(List<Light> lights) {
        for(int i = 0; i < 20; i++)
        {
            if(i < lights.size())
            {
                super.loadVector(location_lightPosition[i], lights.get(i).getPosition());
                super.loadVector(location_lightColor[i], lights.get(i).getColor());
                super.loadVector(locationAttenuation[i], lights.get(i).getAttenuation());
            }
            else
            {
                super.loadVector(location_lightPosition[i], new Vector3f(0, 0, 0));
                super.loadVector(location_lightColor[i], new Vector3f(0, 0, 0));
                super.loadVector(locationAttenuation[i], new Vector3f(1, 0, 0));
            }
        }
    }

    public void loadSpecularLight(float shineDamper, float reflectivity) {
        super.loadFloat(location_shineDamper, shineDamper);
        super.loadFloat(location_reflectivity, reflectivity);
    }

    public void loadSkyColor(float r, float g, float b) {
        super.loadVector(location_skyColor, new Vector3f(r, g, b));
    }

    public void connectTextureUnits() {
        super.loadInt(location_backgroundTexture, 0);
        super.loadInt(location_rTexture, 1);
        super.loadInt(location_gTexture, 2);
        super.loadInt(location_bTexture, 3);
        super.loadInt(location_blendMap, 4);
        super.loadInt(locationShadowMap, 5);
    }
}

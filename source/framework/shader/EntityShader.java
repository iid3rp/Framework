package framework.shader;

import framework.entity.Camera;
import framework.entity.Light;
import framework.utils.GeomMath;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

public class EntityShader extends GLShader
{
    private static final String VERTEX_FILE = "EntityVertexShader.glsl";
    private static final String FRAGMENT_FILE = "EntityFragmentShader.glsl";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int[] location_lightPosition;
    private int[] location_lightColor;
    private int location_shineDamper;
    private int location_reflectivity;
    private int location_useFakeLighting;
    private int location_skyColor;
    private int location_numberOfRowsInTextureAtlas;
    private int location_offset;
    private int[] locationAttenuation;
    private int locationPlane;

    public EntityShader() {
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
        location_useFakeLighting = super.getUniformLocation("useFakeLighting");
        location_skyColor = super.getUniformLocation("skyColor");
        location_numberOfRowsInTextureAtlas = super.getUniformLocation("numberOfRowsInTextureAtlas");   // texture atlas support
        location_offset = super.getUniformLocation("offset");   // texture atlas support
        locationPlane = super.getUniformLocation("plane");

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

    public void loadFakeLighting(boolean useFakeLighting) {
        super.loadBoolean(location_useFakeLighting, useFakeLighting);
    }

    public void loadSkyColor(float r, float g, float b) {
        super.loadVector(location_skyColor, new Vector3f(r, g, b));
    }

    public void loadNumberOfRowsInTextureAtlas(int numberOfRowsInTextureAtlas) {
        super.loadFloat(location_numberOfRowsInTextureAtlas, numberOfRowsInTextureAtlas);
    }

    public void loadOffset(float x, float y) {
        super.loadVector(location_offset, new Vector2f(x, y));
    }

    public void loadClipPlane(Vector4f vec4)
    {
        super.loadVector(locationPlane, vec4);
    }
}

package framework.shader;

import framework.entity.Camera;
import framework.entity.Light;
import framework.lang.Mat4;
import framework.lang.Vec2;
import framework.lang.Vec3;
import framework.lang.Vec4;
import framework.lang.Math;
import framework.util.LinkList;

import java.awt.Color;

public class EntityShader extends GLShader
{
    private static final String VERTEX_FILE = "EntityVertexShader.glsl";
    private static final String FRAGMENT_FILE = "EntityFragmentShader.glsl";

    public int locationHighlightColor;
    private int locationModelTexture;
    private int locationNormalMap;
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
    private int locationHasSpecularMap;
    private int locationSpecularMap;
    private int locationMouseColor;
    private int locationShadowMap;
    private int locationShadowMapSpace;

    public EntityShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "framework/normal");
        super.bindAttribute(3, "tangent");
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
        locationModelTexture = super.getUniformLocation("modelTexture");
        locationNormalMap = super.getUniformLocation("normalMap");
        locationHasSpecularMap = super.getUniformLocation("hasSpecularMap");
        locationSpecularMap = super.getUniformLocation("specularMap");
        locationMouseColor = super.getUniformLocation("mouseEventColor"); // mouse Event support
        locationHighlightColor = super.getUniformLocation("highlightColor");
        locationShadowMap = super.getUniformLocation("shadowMap");
        locationShadowMapSpace = super.getUniformLocation("toShadowMapSpace");

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

    public void loadTransformationMatrix(Mat4 matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadProjectionMatrix(Mat4 projectionMatrix) {
        super.loadMatrix(location_projectionMatrix, projectionMatrix);
    }

    public void loadViewMatrix(Camera camera) {
        Mat4 viewMatrix = Math.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }

    public void loadMouseEventColor(Color color)
    {
        float r = color.getRed() / 255.0f;
        float g = color.getGreen() / 255.0f;
        float b = color.getBlue() / 255.0f;
        float a = color.getAlpha() / 255.0f;
        Vec4 vec4 = new Vec4(r, g, b, a);
        super.loadVector(locationMouseColor, vec4);
    }

    public void loadLights(LinkList<Light> lights, Camera camera) {
        Mat4 viewMatrix = Math.createViewMatrix(camera);
        final int lightSize = lights.size();
        for(int i = 0; i < 20; i++)
        {
            if(i < lightSize)
            {
                Light light = lights.get(i);
                super.loadVector(location_lightPosition[i], getEyeSpacePosition(light, viewMatrix));
                super.loadVector(location_lightColor[i], light.getColor());
                super.loadVector(locationAttenuation[i], light.getAttenuation());
            }
            else
            {
                super.loadVector(location_lightPosition[i], new Vec3(0, 0, 0));
                super.loadVector(location_lightColor[i], new Vec3(0, 0, 0));
                super.loadVector(locationAttenuation[i], new Vec3(1, 0, 0));
            }
        }
    }

    private Vec3 getEyeSpacePosition(Light light, Mat4 viewMatrix){
        Vec3 position = light.getPosition();
        Vec4 eyeSpacePos = new Vec4(position.x,position.y, position.z, 1f);
        Mat4.transform(viewMatrix, eyeSpacePos, eyeSpacePos);
        return new Vec3(eyeSpacePos.x,eyeSpacePos.y,eyeSpacePos.z);
    }

    public void connectTextureUnits(){
        super.loadInt(locationModelTexture, 0);
        super.loadInt(locationNormalMap, 1);
        super.loadInt(locationSpecularMap, 2);
        super.loadInt(locationShadowMap, 5);
    }

    public void loadHasSpecularMap(boolean hasSpecularMap)
    {
        super.loadBoolean(locationHasSpecularMap, hasSpecularMap);
    }

    public void loadShineVariables(float shineDamper, float reflectivity) {
        super.loadFloat(location_shineDamper, shineDamper);
        super.loadFloat(location_reflectivity, reflectivity);
    }

    public void loadFakeLighting(boolean useFakeLighting) {
        super.loadBoolean(location_useFakeLighting, useFakeLighting);
    }

    public void loadSkyColor(float r, float g, float b)
    {
        super.loadVector(location_skyColor, new Vec3(r, g, b));
    }

    public void loadNumberOfRowsInTextureAtlas(int numberOfRowsInTextureAtlas) {
        super.loadFloat(location_numberOfRowsInTextureAtlas, numberOfRowsInTextureAtlas);
    }

    public void loadOffset(float x, float y)
    {
        super.loadVector(location_offset, new Vec2(x, y));
    }

    public void loadClipPlane(Vec4 vec4)
    {
        super.loadVector(locationPlane, vec4);
    }

    public void loadHighlightColor(Vec4 highlightColor)
    {
        super.loadVector(locationHighlightColor, highlightColor);
    }

    public void loadShadowMatrix(Mat4 shadow)
    {
        super.loadMatrix(locationShadowMapSpace, shadow);
    }
}



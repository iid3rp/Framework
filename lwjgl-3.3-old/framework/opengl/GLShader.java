package framework.opengl;
import framework.entity.Camera;
import framework.entity.Entity;
import framework.entity.Light;
import framework.lang.Mat4;
import framework.lang.Math;
import framework.lang.Vec2;
import framework.lang.Vec3;
import framework.lang.Vec4;
import framework.util.FileUtils;
import framework.util.LinkList;
import org.lwjgl.BufferUtils;

import java.awt.Color;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL46.*;

public class GLShader
{
    public static int currentProgram;
    private static final FloatBuffer matrixBuffer;
    private static final int lights = 200;

    static
    {
        matrixBuffer = BufferUtils.createFloatBuffer(16);
    }

    protected static int getUniformLocation(String uniformName)
    {
        return glGetUniformLocation(currentProgram, uniformName);
    }

    public static void bindAttribute(int attribute, String name)
    {
        glBindAttribLocation(currentProgram, attribute, name);
    }

    public static void loadInt(int location, int value)
    {
        glUniform1i(location, value);
    }

    public static void loadFloat(int location, float value)
    {
        glUniform1f(location, value);
    }

    public static void loadVector(int location, Vec2 value)
    {
        glUniform2f(location, value.x, value.y);
    }

    public static void loadVector(int location, Vec3 value)
    {
        glUniform3f(location, value.x, value.y, value.z);
    }

    public static void loadVector(int location, Vec4 value)
    {
        glUniform4f(location, value.x, value.y, value.z, value.w);
    }

    public static void loadMatrix(int location, Mat4 matrix)
    {
        matrix.store(matrixBuffer);
        matrixBuffer.flip();
        glUniformMatrix4fv(location, false, matrixBuffer);
    }

    private static void loadBoolean(int location, boolean flag)
    {
        glUniform1i(location, flag? 1 : 0);
    }

    public static class EntityShader
    {
        public static final String vertexFile;
        public static final String fragmentFile;
        private static int programId;
        private static int vertexShaderId;
        private static int fragmentShaderId;

        // values
        private static int locationHighlightColor;
        private static int locationModelTexture;
        private static int locationNormalMap;
        private static int location_transformationMatrix;
        private static int location_projectionMatrix;
        private static int location_viewMatrix;
        private static int[] location_lightPosition;
        private static int[] location_lightColor;
        private static int location_shineDamper;
        private static int location_reflectivity;
        private static int location_useFakeLighting;
        private static int location_skyColor;
        private static int location_numberOfRowsInTextureAtlas;
        private static int location_offset;
        private static int[] locationAttenuation;
        private static int locationPlane;
        private static int locationHasSpecularMap;
        private static int locationSpecularMap;
        private static int locationMouseColor;
        private static int locationShadowMap;
        private static int locationShadowMapSpace;

        static
        {
            vertexFile = FileUtils.loadShader("entityVertexShader.glsl");
            fragmentFile = FileUtils.loadShader("entityFragmentShader.glsl");

            programId = glCreateProgram();
            vertexShaderId = glCreateShader(GL_VERTEX_SHADER);

            glShaderSource(vertexShaderId, vertexFile);
            glCompileShader(vertexShaderId);

            if (glGetShaderi(vertexShaderId, GL_COMPILE_STATUS) == GL_FALSE)
                throw new RuntimeException("\nVertex Shader: " + glGetShaderInfoLog(vertexShaderId));

            fragmentShaderId = glCreateShader(GL_FRAGMENT_SHADER);

            glShaderSource(fragmentShaderId, fragmentFile);
            glCompileShader(fragmentShaderId);


            if (glGetShaderi(fragmentShaderId, GL_COMPILE_STATUS) == GL_FALSE)
                throw new RuntimeException("\nFragment Shader: " + glGetShaderInfoLog(fragmentShaderId));


            glAttachShader(programId, vertexShaderId);
            glAttachShader(programId, fragmentShaderId);
            bindAttributes();

            glLinkProgram(programId);
            if (glGetProgrami(programId, GL_LINK_STATUS) == GL_FALSE)
                throw new RuntimeException("Program Linking: " + glGetProgramInfoLog(programId));

            glValidateProgram(programId);

            if (glGetProgrami(programId, GL_VALIDATE_STATUS) == GL_FALSE)
                throw new RuntimeException("Program Validation: " + glGetProgramInfoLog(programId));

            getAllUniformLocations();
        }

        private static void getAllUniformLocations()
        {
            currentProgram = programId;
            location_transformationMatrix = getUniformLocation("transformationMatrix");
            location_projectionMatrix = getUniformLocation("projectionMatrix");
            location_viewMatrix = getUniformLocation("viewMatrix");
            location_shineDamper = getUniformLocation("shineDamper");
            location_reflectivity = getUniformLocation("reflectivity");
            location_useFakeLighting = getUniformLocation("useFakeLighting");
            location_skyColor = getUniformLocation("skyColor");
            location_numberOfRowsInTextureAtlas = getUniformLocation("numberOfRowsInTextureAtlas");   // texture atlas support
            location_offset = getUniformLocation("offset");   // texture atlas support
            locationPlane = getUniformLocation("plane");
            locationModelTexture = getUniformLocation("modelTexture");
            locationNormalMap = getUniformLocation("normalMap");
            locationHasSpecularMap = getUniformLocation("hasSpecularMap");
            locationSpecularMap = getUniformLocation("specularMap");
            locationMouseColor = getUniformLocation("mouseEventColor"); // mouse Event support
            locationHighlightColor = getUniformLocation("highlightColor");
            locationShadowMap = getUniformLocation("shadowMap");
            locationShadowMapSpace = getUniformLocation("toShadowMapSpace");

            location_lightPosition = new int[20];
            location_lightColor = new int[20];
            locationAttenuation = new int[20];

            for(int i = 0; i < 20; i++)
            {
                location_lightPosition[i] = getUniformLocation("lightPosition[" + i + "]");
                location_lightColor[i] = getUniformLocation("lightColor[" + i + "]");
                locationAttenuation[i] = getUniformLocation("attenuation[" + i + "]");
            }
        }

        public static void bindAttributes()
        {
            bindAttribute(0, "position");
            bindAttribute(1, "textureCoords");
            bindAttribute(2, "normal");
            bindAttribute(3, "tangent");
        }

        private static void loadEntity(Entity entity)
        {
            
        }

        private static void loadTransformationMatrix(Mat4 matrix)
        {
            loadMatrix(location_transformationMatrix, matrix);
        }

        private static void loadProjectionMatrix(Mat4 projectionMatrix)
        {
            loadMatrix(location_projectionMatrix, projectionMatrix);
        }

        private static void loadViewMatrix(Camera camera)
        {
            Mat4 viewMatrix = Math.createViewMatrix(camera);
            loadMatrix(location_viewMatrix, viewMatrix);
        }

        private static void loadMouseEventColor(Color color)
        {
            float r = color.getRed() / 255.0f;
            float g = color.getGreen() / 255.0f;
            float b = color.getBlue() / 255.0f;
            float a = color.getAlpha() / 255.0f;
            Vec4 vec4 = new Vec4(r, g, b, a);
            loadVector(locationMouseColor, vec4);
        }

        private static void loadLights(LinkList<Light> lights, Camera camera) {
            Mat4 viewMatrix = Math.createViewMatrix(camera);
            final int lightSize = lights.size();
            for(int i = 0; i < 20; i++)
            {
                if(i < lightSize)
                {
                    Light light = lights.get(i);
                    loadVector(location_lightPosition[i], getEyeSpacePosition(light, viewMatrix));
                    loadVector(location_lightColor[i], light.getColor());
                    loadVector(locationAttenuation[i], light.getAttenuation());
                }
                else
                {
                    loadVector(location_lightPosition[i], new Vec3(0, 0, 0));
                    loadVector(location_lightColor[i], new Vec3(0, 0, 0));
                    loadVector(locationAttenuation[i], new Vec3(1, 0, 0));
                }
            }
        }

        private static Vec3 getEyeSpacePosition(Light light, Mat4 viewMatrix){
            Vec3 position = light.getPosition();
            Vec4 eyeSpacePos = new Vec4(position.x,position.y, position.z, 1f);
            Mat4.transform(viewMatrix, eyeSpacePos, eyeSpacePos);
            return new Vec3(eyeSpacePos.x,eyeSpacePos.y,eyeSpacePos.z);
        }

        private static void connectTextureUnits(){
            loadInt(locationModelTexture, 0);
            loadInt(locationNormalMap, 1);
            loadInt(locationSpecularMap, 2);
            loadInt(locationShadowMap, 5);
        }

        private static void loadHasSpecularMap(boolean hasSpecularMap)
        {
            loadBoolean(locationHasSpecularMap, hasSpecularMap);
        }

        private static void loadShineVariables(float shineDamper, float reflectivity) {
            loadFloat(location_shineDamper, shineDamper);
            loadFloat(location_reflectivity, reflectivity);
        }

        private static void loadFakeLighting(boolean useFakeLighting) {
            loadBoolean(location_useFakeLighting, useFakeLighting);
        }

        private static void loadSkyColor(float r, float g, float b)
        {
            loadVector(location_skyColor, new Vec3(r, g, b));
        }

        private static void loadNumberOfRowsInTextureAtlas(int numberOfRowsInTextureAtlas) {
            loadFloat(location_numberOfRowsInTextureAtlas, numberOfRowsInTextureAtlas);
        }

        private static void loadOffset(float x, float y)
        {
            loadVector(location_offset, new Vec2(x, y));
        }

        private static void loadClipPlane(Vec4 vec4)
        {
            loadVector(locationPlane, vec4);
        }

        private static void loadHighlightColor(Vec4 highlightColor)
        {
            loadVector(locationHighlightColor, highlightColor);
        }

        private static void loadShadowMatrix(Mat4 shadow)
        {
            loadMatrix(locationShadowMapSpace, shadow);
        }
    }
}

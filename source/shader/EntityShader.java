package shader;

import entity.Camera;
import entity.Light;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import toolbox.MatrixMultiplication;
import org.lwjgl.util.vector.Matrix4f;

import java.util.List;

public class EntityShader extends GLShader
{
    public static final String VERTEX_FILE = "source/script/entityVertexShader.glsl";
    public static final String FRAGMENT_FILE = "source/script/entityFragmentShader.glsl";
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

    public EntityShader()
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
        locationFakeLighting = super.getUniformLocation("useFakeLighting");
        locationNumberOfRows = super.getUniformLocation("numberOfRows");
        locationOffset = super.getUniformLocation("offset");
        locationPlane = super.getUniformLocation("plane");


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

    @Override
    protected void bindAttributes()
    {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "normal");
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
        Matrix4f view = MatrixMultiplication.createViewMatrix(camera);
        super.loadMatrix(locationView, view);
    }

    public void loadProjectionMatrix(Matrix4f matrix)
    {
        super.loadMatrix(locationProjection, matrix);
    }
}

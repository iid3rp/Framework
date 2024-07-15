package framework.water;

import framework.entity.Camera;
import framework.entity.Light;
import framework.display.MasterRenderer;
import framework.shader.GLShader;
import framework.toolbox.GeomMath;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class WaterShader extends GLShader
{

	private final static String VERTEX_FILE = "waterVertexShader.glsl";
	private final static String FRAGMENT_FILE = "waterFragmentShader.glsl";
	private int location_modelMatrix;
	private int location_viewMatrix;
	private int location_projectionMatrix;
	private int locationReflection;
	private int locationRefraction;
	private int locationDuDv;
	private int locationMoveFactor;
	private int locationCameraPosition;
	private int locationNormals;
	private int locationLightColor;
	private int locationLightPosition;
	private int locationDepthMap;
	private int locationNear;
	private int locationFar;

	public WaterShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		location_viewMatrix = getUniformLocation("viewMatrix");
		location_modelMatrix = getUniformLocation("modelMatrix");
		locationReflection = getUniformLocation("reflection");
		locationRefraction = getUniformLocation("refraction");
		locationDuDv = getUniformLocation("duDv");
		locationMoveFactor = getUniformLocation("moveFactor");
		locationCameraPosition = getUniformLocation("cameraPosition");
		locationNormals = getUniformLocation("normals");
		locationLightColor = getUniformLocation("lightColor");
		locationLightPosition = getUniformLocation("lightPosition");
		locationDepthMap = getUniformLocation("depthMap");
		locationNear = getUniformLocation("near");
		locationFar = getUniformLocation("far");
	}

	public void loadLight(Light light)
	{
		super.loadVector3f(locationLightColor, light.getColor());
		super.loadVector3f(locationLightPosition, light.getPosition());
	}

	public void loadCameraPosition(Vector3f position)
	{
		super.loadVector3f(locationCameraPosition, position);
	}

	public void loadMoveFactor(float factor)
	{
		super.loadFloat(locationMoveFactor, factor);
	}

	public void connectTextureUnits()
	{
		super.loadInteger(locationReflection, 0);
		super.loadInteger(locationRefraction, 1);
		super.loadInteger(locationDuDv, 2);
		super.loadInteger(locationNormals, 3);
		super.loadInteger(locationDepthMap, 4);
	}

	public void loadProjectionMatrix(Matrix4f projection)
	{
		super.loadMatrix(location_projectionMatrix, projection);
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = GeomMath.createViewMatrix(camera);
		loadMatrix(location_viewMatrix, viewMatrix);
		loadCameraPosition(camera.getPosition());
		//loadCameraPosition(new Vector3f(camera.getPosition().x, 10, camera.getPosition().z));
		super.loadFloat(locationNear, MasterRenderer.nearPlane);
		super.loadFloat(locationFar, MasterRenderer.farPlane);
	}

	public void loadModelMatrix(Matrix4f modelMatrix){
		loadMatrix(location_modelMatrix, modelMatrix);
	}

}

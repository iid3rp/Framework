package framework.water;

import framework.entity.Camera;
import framework.entity.Light;
import framework.renderer.MasterRenderer;
import framework.shader.GLShader;
import framework.util.GeomMath;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class WaterShader extends GLShader
{

	private final static String VERTEX_FILE = "WaterVertexShader.glsl";
	private final static String FRAGMENT_FILE = "WaterFragmentShader.glsl";
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
		super.loadVector(locationLightColor, light.getColor());
		super.loadVector(locationLightPosition, light.getPosition());
	}

	public void loadCameraPosition(Vector3f position)
	{
		super.loadVector(locationCameraPosition, position);
	}

	public void loadMoveFactor(float factor)
	{
		super.loadFloat(locationMoveFactor, factor);
	}

	public void connectTextureUnits()
	{
		super.loadInt(locationReflection, 0);
		super.loadInt(locationRefraction, 1);
		super.loadInt(locationDuDv, 2);
		super.loadInt(locationNormals, 3);
		super.loadInt(locationDepthMap, 4);
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
		super.loadFloat(locationNear, MasterRenderer.NEAR_PLANE);
		super.loadFloat(locationFar, MasterRenderer.FAR_PLANE);
	}

	public void loadModelMatrix(Matrix4f modelMatrix){
		loadMatrix(location_modelMatrix, modelMatrix);
	}

}

package shader;

import entity.Camera;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import render.DisplayManager;
import toolbox.GeomMath;

public class SkyboxShader extends GLShader{

	private static final String VERTEX_FILE = "source/script/skyboxVertexShader.glsl";
	private static final String FRAGMENT_FILE = "source/script/skyboxFragmentShader.glsl";
	private static float rotationSpeed = 1f;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int locationCubeMap1;
	private int locationCubeMap2;
	private float rotation;
	private int locationFogColor;
	private int locationBlendFactor;

	public SkyboxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix){
		super.loadMatrix(location_projectionMatrix, matrix);
	}

	public void loadViewMatrix(Camera camera){
		Matrix4f matrix = GeomMath.createViewMatrix(camera);
		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;
		rotation += rotationSpeed * DisplayManager.getFrameTimeSeconds();
		Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0, 1, 0), matrix, matrix);
		super.loadMatrix(location_viewMatrix, matrix);
	}

	public void loadFogColor(float r, float g, float b)
	{
		super.loadVector3f(locationFogColor, new Vector3f(r, g, b));
	}

	public void connectTextureUnits()
	{
		super.loadInteger(locationCubeMap1, 0);
		super.loadInteger(locationCubeMap2, 1);
	}
	
	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		locationFogColor = super.getUniformLocation("fogColor");
		locationCubeMap1 = super.getUniformLocation("cubeMap1");
		locationCubeMap2 = super.getUniformLocation("cubeMap2");
		locationBlendFactor = super.getUniformLocation("blendFactor");
	}

	public void loadBlendFactor(float blend)
	{
		super.loadFloat(locationBlendFactor, blend);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}

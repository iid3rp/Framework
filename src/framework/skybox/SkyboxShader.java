package framework.skybox;

import framework.entity.Camera;
import framework.shader.GLShader;
import framework.toolkit.Display;
import framework.util.GeomMath;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class SkyboxShader extends GLShader
{

	private static final String VERTEX_FILE = "SkyboxVertexShader.glsl";
	private static final String FRAGMENT_FILE = "SkyboxFragmentShader.glsl";
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
		matrix.m30(0);
		matrix.m31(0);
		matrix.m32(0);
		rotation += rotationSpeed * Display.getDeltaInSeconds();
		matrix.rotate((float) Math.toRadians(rotation), new Vector3f(0, 1, 0));
		super.loadMatrix(location_viewMatrix, matrix);
	}

	public void loadFogColor(float r, float g, float b)
	{
		super.loadVector(locationFogColor, new Vector3f(r, g, b));
	}

	public void connectTextureUnits()
	{
		super.loadInt(locationCubeMap1, 0);
		super.loadInt(locationCubeMap2, 1);
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

package framework.particles;

import framework.lang.Matrix4f;
import framework.shader.GLShader;

public class ParticleShader extends GLShader
{

	private static final String VERTEX_FILE = "particleVertexShader.glsl";
	private static final String FRAGMENT_FILE = "particleFragmentShader.glsl";

	private int location_numberOfRows;
	private int location_projectionMatrix;

	public ParticleShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_numberOfRows = super.getUniformLocation("numOfRows");
	}

	protected void loadNumberOfRows(float numberOfRows)
	{
		super.loadFloat(location_numberOfRows, numberOfRows);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "modelViewMatrix");
		super.bindAttribute(5, "textureOffsets");
		super.bindAttribute(6, "blendFactor");
	}


	protected void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(location_projectionMatrix, projectionMatrix);
	}

}

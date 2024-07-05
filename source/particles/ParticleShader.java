package particles;

import org.lwjgl.util.vector.Matrix4f;

import shader.GLShader;

public class ParticleShader extends GLShader
{

	private static final String VERTEX_FILE = "source/particles/particleVertexShader.glsl";
	private static final String FRAGMENT_FILE = "source/particles/particleFragmentShader.glsl";

	private int location_numberOfRows;
	private int location_projectionMatrix;


	public ParticleShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_numberOfRows = super.getUniformLocation("numOfRows");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "modelViewMatrix");
		super.bindAttribute(5, "texOffset");
		super.bindAttribute(6, "blendFactor");
	}

	protected void loadNumberOfRows(float numberOfRows)
	{
		super.loadFloat(location_numberOfRows, numberOfRows);
	}

	protected void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(location_projectionMatrix, projectionMatrix);
	}

}

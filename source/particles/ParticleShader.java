package particles;

import org.lwjgl.util.vector.Matrix4f;

import shader.GLShader;

public class ParticleShader extends GLShader
{

	private static final String VERTEX_FILE = "source/particles/particleVertexShader.glsl";
	private static final String FRAGMENT_FILE = "source/particles/particleFragmentShader.glsl";

	private int location_modelViewMatrix;
	private int location_projectionMatrix;

	public ParticleShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_modelViewMatrix = super.getUniformLocation("modelViewMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	protected void loadModelViewMatrix(Matrix4f modelView) {
		super.loadMatrix(location_modelViewMatrix, modelView);
	}

	protected void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(location_projectionMatrix, projectionMatrix);
	}

}

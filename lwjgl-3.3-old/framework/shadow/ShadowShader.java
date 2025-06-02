package framework.shadow;

import framework.lang.Mat4;
import framework.shader.GLShader;

public class ShadowShader extends GLShader
{
	
	private static final String VERTEX_FILE = "shadowVertexShader.glsl";
	private static final String FRAGMENT_FILE = "shadowFragmentShader.glsl";
	
	private int location_mvpMatrix;

	protected ShadowShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_mvpMatrix = super.getUniformLocation("mvpMatrix");
		
	}
	
	protected void loadMvpMatrix(Mat4 mvpMatrix){
		super.loadMatrix(location_mvpMatrix, mvpMatrix);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "in_position");
	}

}
